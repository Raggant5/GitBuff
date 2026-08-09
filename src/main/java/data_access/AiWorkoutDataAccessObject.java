package data_access;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import entity.Exercise;
import entity.FitnessGoal;
import entity.User;
import entity.WorkoutPlan;
import use_case.recommendation.AiWorkoutDataAccessInterface;
import use_case.recommendation.WorkoutPlanGenerationStrategy;

/**
 * Data access object for retrieving AI generated workout plans.
 *
 * <p>This is an Adapter (Adapter design pattern): it adapts the Gemini generative-language HTTP
 * API to the {@link AiWorkoutDataAccessInterface} the use case layer depends on. It previously
 * also owned ~250 lines of static exercise reference data and every piece of "which exercises
 * make up a workout" categorization logic - five responsibilities in one 1,100+ line class.
 * That data and logic has moved to {@link ExerciseKnowledgeBase} (pure reference data and
 * lookups, no I/O), and the deterministic no-API fallback has moved to
 * {@link FallbackWorkoutPlanStrategy}, a {@link WorkoutPlanGenerationStrategy} this class
 * delegates to (Strategy design pattern) whenever the Gemini API is unconfigured, unreachable,
 * or returns an unusable response. What remains here is exactly one responsibility: call
 * Gemini, and parse what it returns.
 */
public class AiWorkoutDataAccessObject implements AiWorkoutDataAccessInterface {

    private static final Logger LOGGER = Logger.getLogger(AiWorkoutDataAccessObject.class.getName());

    private static final int HTTP_OK = 200;
    private static final int DEFAULT_CALORIES = 320;
    private static final int DEFAULT_FAT = 15;
    private static final int DEFAULT_CARBS = 45;
    private static final int DEFAULT_DAYS = 7;
    private static final int DEFAULT_SETS = 3;
    private static final int DEFAULT_REPS = 12;
    private static final int DEFAULT_EX_DURATION = 10;
    private static final int HEX_PAD_LEN = 4;
    private static final int DEFAULT_WORKOUT_DURATION = 45;
    private static final int MAX_EXERCISES_PER_WORKOUT = 3;
    private static final int CONNECT_TIMEOUT_MILLIS = 12000;
    private static final int READ_TIMEOUT_MILLIS = 25000;

    private static final String API_VERSION = "v1";
    private static final String GEMINI_MODEL = "gemini-3.5-flash-lite";

    private static final String[] DEFAULT_PARSE_TITLES =
            {"Full Body Functional Strength", "Cardio Health Run", "Upper Body Conditioning"};
    private static final String[] DEFAULT_PARSE_DESCRIPTIONS = {
            "Balanced full body functional resistance workout for overall fitness.",
            "Steady state cardio run for heart health.",
            "Upper body strength and conditioning.",
    };

    private final String apiKey;
    private final WorkoutPlanGenerationStrategy fallbackStrategy;
    private int lastUsedDuration = DEFAULT_WORKOUT_DURATION;

    /**
     * Default constructor for AiWorkoutDataAccessObject. Uses {@link FallbackWorkoutPlanStrategy}
     * whenever the Gemini API can't be used.
     */
    public AiWorkoutDataAccessObject() {
        this(loadApiKeyFromEnvironment(), new FallbackWorkoutPlanStrategy());
    }

    /**
     * Constructs an instance with a provided API key. Uses {@link FallbackWorkoutPlanStrategy}
     * whenever the Gemini API can't be used.
     *
     * @param apiKey API key for Gemini
     */
    public AiWorkoutDataAccessObject(final String apiKey) {
        this(apiKey, new FallbackWorkoutPlanStrategy());
    }

    /**
     * Constructs an instance with a provided API key and fallback strategy.
     *
     * @param apiKey API key for Gemini
     * @param fallbackStrategy strategy used whenever the Gemini API is unconfigured, unreachable,
     *        or returns an unusable response
     */
    public AiWorkoutDataAccessObject(final String apiKey, final WorkoutPlanGenerationStrategy fallbackStrategy) {
        this.apiKey = apiKey;
        this.fallbackStrategy = fallbackStrategy;
        LOGGER.log(Level.INFO, "AiWorkoutDataAccessObject initialized. API Key present: {0}",
                this.apiKey != null && !this.apiKey.isEmpty() && !"YOUR_API_KEY_HERE".equals(this.apiKey));
    }

    private static String loadApiKeyFromEnvironment() {
        String key = loadKeyFromDotEnv();
        if (key == null || key.trim().isEmpty()) {
            key = System.getenv("GEMINI_API_KEY");
        }
        return key;
    }

    private static String loadKeyFromDotEnv() {
        final File envFile = new File(".env");
        if (!envFile.exists()) {
            LOGGER.warning(".env file not found");
            return null;
        }
        try (Scanner scanner = new Scanner(envFile, StandardCharsets.UTF_8)) {
            while (scanner.hasNextLine()) {
                final String line = scanner.nextLine().trim();
                if (line.startsWith("GEMINI_API_KEY=")) {
                    final String key = line.substring("GEMINI_API_KEY=".length()).trim();
                    LOGGER.info("API Key loaded from .env file");
                    return key;
                }
            }
        }
        catch (final IOException ex) {
            LOGGER.log(Level.WARNING, "Error reading .env file: {0}", ex.getMessage());
        }
        return null;
    }

    @Override
    public List<WorkoutPlan> generateWorkoutPlans(final User user) {
        return generateWorkoutPlans(user, DEFAULT_DAYS);
    }

    @Override
    public List<WorkoutPlan> generateWorkoutPlans(final User user, final int numberOfDays) {
        LOGGER.log(Level.INFO, "Generating {0} workout plans for user", numberOfDays);

        if (user == null || user.getGoal() == null) {
            LOGGER.warning("User or user goal is null, using fallback");
            return this.fallbackStrategy.generatePlans(user, numberOfDays);
        }

        if (user.getPreferredWorkoutDurationMinutes() > 0) {
            this.lastUsedDuration = user.getPreferredWorkoutDurationMinutes();
        }

        if (this.apiKey != null && !this.apiKey.trim().isEmpty() && !"YOUR_API_KEY_HERE".equals(this.apiKey)) {
            try {
                final List<WorkoutPlan> plans = callApiWithTimeout(user, numberOfDays);
                if (plans != null && !plans.isEmpty()) {
                    return plans;
                }
            }
            catch (final IOException ex) {
                LOGGER.log(Level.WARNING, "API call failed or timed out: {0}", ex.getMessage());
            }
        }

        LOGGER.info("Using fallback plans");
        return this.fallbackStrategy.generatePlans(user, numberOfDays);
    }

    private List<WorkoutPlan> callApiWithTimeout(final User user, final int numberOfDays) throws IOException {
        final String endpoint = "https://generativelanguage.googleapis.com/" + API_VERSION + "/models/"
                + GEMINI_MODEL + ":generateContent?key=" + this.apiKey;

        final URL url = new URL(endpoint);
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setConnectTimeout(CONNECT_TIMEOUT_MILLIS);
        connection.setReadTimeout(READ_TIMEOUT_MILLIS);
        connection.setDoOutput(true);

        final String promptText = buildPrompt(user, numberOfDays);

        final String jsonInputString = "{\n"
                + "  \"contents\": [{\n"
                + "    \"parts\": [{\"text\": " + sanitizeJsonString(promptText) + "}]\n"
                + "  }],\n"
                + "  \"generationConfig\": {\n"
                + "    \"responseMimeType\": \"application/json\",\n"
                + "    \"maxOutputTokens\": 2048,\n"
                + "    \"temperature\": 0.3\n"
                + "  }\n"
                + "}";

        try (OutputStream outputStream = connection.getOutputStream()) {
            final byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            outputStream.write(input, 0, input.length);
        }

        final int responseCode = connection.getResponseCode();
        LOGGER.log(Level.INFO, "API Response code: {0}", responseCode);

        if (responseCode == HTTP_OK) {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                final StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line.trim());
                }
                final String responseStr = response.toString();
                LOGGER.log(Level.INFO, "API Response received, length: {0}", responseStr.length());

                return parseGeminiJsonResponse(responseStr, user, numberOfDays);
            }
        }
        else {
            try (BufferedReader errReader = new BufferedReader(
                    new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8))) {
                final StringBuilder errResponse = new StringBuilder();
                String errLine;
                while ((errLine = errReader.readLine()) != null) {
                    errResponse.append(errLine.trim());
                }
                LOGGER.log(Level.WARNING, "API returned error code: {0}, message: {1}",
                        new Object[]{responseCode, errResponse.toString()});
            }
        }
        return null;
    }

    private String buildPrompt(final User user, final int numberOfDays) {
        final Set<DayOfWeek> preferredDays = user.getPreferredWorkoutDays() != null
                && !user.getPreferredWorkoutDays().isEmpty()
                ? user.getPreferredWorkoutDays()
                : Set.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY);

        final String equipmentListStr;
        if (user.getEquipment() == null || user.getEquipment().isEmpty()) {
            equipmentListStr = "Bodyweight only (No gym equipment)";
        }
        else {
            final List<String> eqNames = new ArrayList<>();
            for (final Object eq : user.getEquipment()) {
                if (eq != null) {
                    eqNames.add(eq.toString());
                }
            }
            equipmentListStr = eqNames.isEmpty() ? "Bodyweight only" : String.join(", ", eqNames);
        }

        final FitnessGoal goal = user.getGoal();
        final String goalName = (goal != null) ? goal.toString() : "Maintain Weight & General Fitness";
        final String goalFocus = (goal != null) ? goal.getWorkoutFocus() : "Balanced full-body training";
        final String genderStr = (user.getGender() != null) ? user.getGender().toString() : "Prefer Not to Say";
        final String activityStr = (user.getActivityLevel() != null)
                ? user.getActivityLevel().getDescription() : "Moderately Active";

        return "You are a professional strength and conditioning coach. Generate active workout plans ONLY for the days "
                + "listed under Preferred Workout Days. DO NOT generate workout blocks for rest days.\n"
                + "CLIENT PROFILE:\n"
                + "- Client Name: " + user.getName() + "\n"
                + "- Gender: " + genderStr + "\n"
                + "- Primary Fitness Goal: " + goalName + " (" + goalFocus + ")\n"
                + "- Activity Level: " + activityStr + "\n"
                + "- Preferred Workout Days: " + preferredDays + "\n"
                + "- Target Duration: " + this.lastUsedDuration + " minutes\n"
                + "- STRICT EQUIPMENT ACCESSIBILITY: " + equipmentListStr + "\n\n"
                + "STRICT TITLE & DESCRIPTION RULES:\n"
                + "1. NEVER output generic titles like 'Workout 1', 'Workout 2', or 'Workout N'. Every workout MUST have "
                + "an expressive, descriptive title (e.g., 'Upper Body Power & Hypertrophy', 'Lower Body Strength Split', "
                + "'HIIT Fat Oxidation').\n"
                + "2. Every workout MUST have a thorough 2-3 sentence description explaining the workout's focus, "
                + "muscle targets, and strategy.\n"
                + "3. DO NOT leave title or description blank or generic for any day, including later days in the "
                + "schedule.\n\n"
                + "FORMAT REQUIREMENTS:\n"
                + "1. DO NOT suggest exercises requiring equipment missing from STRICT EQUIPMENT ACCESSIBILITY.\n"
                + "2. Match exercises to category, subCategory, and target muscle groups relevant to " + goalName + ".\n"
                + "3. Output MUST be a raw JSON array of objects for ACTIVE WORKOUT DAYS only with keys: "
                + "date, title, description, category, subCategory, intensityLevel, targetMuscleGroup, "
                + "equipmentType, estimatedDurationMinutes, estimatedCaloriesBurned, estimatedFatBurnedGrams, "
                + "estimatedCarbsBurnedGrams, and exercises (array of objects: name, sets, reps, "
                + "durationMinutes, targetMuscleGroup, equipmentRequired, instructions, videoUrl, "
                + "category, subCategory, intensityLevel, equipmentType).";
    }

    private String sanitizeJsonString(final String text) {
        if (text == null) {
            return "\"\"";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append('"');
        for (int i = 0; i < text.length(); i++) {
            final char c = text.charAt(i);
            switch (c) {
                case '"':
                    sb.append("\\\"");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                default:
                    if (c < ' ') {
                        final String t = "000" + Integer.toHexString(c);
                        sb.append("\\u").append(t.substring(t.length() - HEX_PAD_LEN));
                    }
                    else {
                        sb.append(c);
                    }
            }
        }
        sb.append('"');
        return sb.toString();
    }

    private List<WorkoutPlan> parseGeminiJsonResponse(final String response, final User user,
                                                      final int numberOfDays) {
        final List<WorkoutPlan> plans = new ArrayList<>();
        final LocalDate today = LocalDate.now();
        final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("EEEE, MMM d");
        final Set<DayOfWeek> preferredDays = user.getPreferredWorkoutDays() != null
                && !user.getPreferredWorkoutDays().isEmpty()
                ? user.getPreferredWorkoutDays()
                : Set.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY);

        final FitnessGoal goal = user.getGoal();
        final String[] fallbackTitles =
                ExerciseKnowledgeBase.getWorkoutTitlesForGoal(goal, DEFAULT_PARSE_TITLES);
        final String[] fallbackDescs =
                ExerciseKnowledgeBase.getWorkoutDescriptionsForGoal(goal, DEFAULT_PARSE_DESCRIPTIONS);

        try {
            String rawJson = response;
            if (response.contains("\"text\":")) {
                final int textIdx = response.indexOf("\"text\":");
                final int startQuote = response.indexOf("\"", textIdx + 7);
                if (startQuote != -1) {
                    int endQuote = startQuote + 1;
                    while (endQuote < response.length()) {
                        if (response.charAt(endQuote) == '"' && response.charAt(endQuote - 1) != '\\') {
                            break;
                        }
                        endQuote++;
                    }
                    if (endQuote < response.length() && endQuote > startQuote) {
                        rawJson = response.substring(startQuote + 1, endQuote)
                                .replace("\\n", " ")
                                .replace("\\\"", "\"")
                                .replace("\\\\", "\\");
                    }
                }
            }

            final int arrayStart = rawJson.indexOf("[");
            final int arrayEnd = rawJson.lastIndexOf("]");
            if (arrayStart == -1 || arrayEnd == -1) {
                LOGGER.warning("Could not find JSON array in response");
                return plans;
            }
            final String jsonArray = rawJson.substring(arrayStart, arrayEnd + 1);

            final List<String> activeDayBlocks = extractJsonObjects(jsonArray);
            int activeBlockIndex = 0;

            for (int i = 0; i < numberOfDays; i++) {
                final LocalDate currentDate = today.plusDays(i);
                final String dateStr = currentDate.format(fmt);
                final DayOfWeek dow = currentDate.getDayOfWeek();
                final boolean isPreferredDay = preferredDays.contains(dow);

                if (isPreferredDay) {
                    final int titleIndex = activeBlockIndex % fallbackTitles.length;

                    if (activeBlockIndex < activeDayBlocks.size()) {
                        final String block = activeDayBlocks.get(activeBlockIndex);
                        activeBlockIndex++;

                        String title = extractVal(block, "title");
                        if (title.isEmpty() || title.contains("Rest") || title.matches("(?i)workout\\s*\\d+")) {
                            title = fallbackTitles[titleIndex];
                        }

                        String desc = extractVal(block, "description");
                        if (desc.isEmpty() || desc.length() < 10) {
                            desc = fallbackDescs[titleIndex];
                        }

                        final String category = extractVal(block, "category").isEmpty()
                                ? "STRENGTH" : extractVal(block, "category");
                        final String subCategory = extractVal(block, "subCategory").isEmpty()
                                ? "FULL_BODY" : extractVal(block, "subCategory");
                        final String intensity = extractVal(block, "intensityLevel").isEmpty()
                                ? "MEDIUM" : extractVal(block, "intensityLevel");
                        final String targetMuscle = extractVal(block, "targetMuscleGroup").isEmpty()
                                ? "Various" : extractVal(block, "targetMuscleGroup");
                        final String equipType = extractVal(block, "equipmentType").isEmpty()
                                ? "BODYWEIGHT" : extractVal(block, "equipmentType");

                        final int calories = extractInt(block, "estimatedCaloriesBurned", DEFAULT_CALORIES);
                        final int fat = extractInt(block, "estimatedFatBurnedGrams", DEFAULT_FAT);
                        final int carbs = extractInt(block, "estimatedCarbsBurnedGrams", DEFAULT_CARBS);
                        final int duration = extractInt(block, "estimatedDurationMinutes", this.lastUsedDuration);

                        List<Exercise> exercises = new ArrayList<>();
                        final int exArrayStart = block.indexOf("\"exercises\"");
                        if (exArrayStart != -1) {
                            final int bracketStart = block.indexOf("[", exArrayStart);
                            final int bracketEnd = block.lastIndexOf("]");
                            if (bracketStart != -1 && bracketEnd > bracketStart) {
                                final String exSubJson = block.substring(bracketStart, bracketEnd + 1);
                                final List<String> exBlocks = extractJsonObjects(exSubJson);

                                for (final String exBlock : exBlocks) {
                                    final String name = extractVal(exBlock, "name");
                                    if (name.isEmpty()) {
                                        continue;
                                    }
                                    final int sets = extractInt(exBlock, "sets", DEFAULT_SETS);
                                    final int reps = extractInt(exBlock, "reps", DEFAULT_REPS);
                                    final int exDuration = extractInt(exBlock, "durationMinutes", DEFAULT_EX_DURATION);
                                    final String exTarget = extractVal(exBlock, "targetMuscleGroup");
                                    String equipReq = extractVal(exBlock, "equipmentRequired");
                                    String inst = extractVal(exBlock, "instructions");
                                    String vid = extractVal(exBlock, "videoUrl");

                                    if (equipReq.isEmpty() || !ExerciseKnowledgeBase.userHasEquipment(user, equipReq)) {
                                        equipReq = "Bodyweight";
                                    }

                                    final String exCat = extractVal(exBlock, "category").isEmpty()
                                            ? category : extractVal(exBlock, "category");
                                    final String exSubCat = extractVal(exBlock, "subCategory").isEmpty()
                                            ? subCategory : extractVal(exBlock, "subCategory");
                                    final String exInt = extractVal(exBlock, "intensityLevel").isEmpty()
                                            ? intensity : extractVal(exBlock, "intensityLevel");
                                    final String exEquip = extractVal(exBlock, "equipmentType").isEmpty()
                                            ? equipType : extractVal(exBlock, "equipmentType");

                                    if (inst.isEmpty()) {
                                        inst = ExerciseKnowledgeBase.getInstructionForExercise(name);
                                    }
                                    if (vid.isEmpty()) {
                                        vid = "https://www.youtube.com/results?search_query="
                                                + name.replace(" ", "+") + "+exercise+tutorial";
                                    }

                                    exercises.add(new Exercise(name, sets, reps, exDuration,
                                            exTarget.isEmpty() ? targetMuscle : exTarget,
                                            equipReq, inst, vid, exCat, exSubCat,
                                            exInt, exEquip));
                                }
                            }
                        }

                        if (exercises.isEmpty()) {
                            exercises = new ArrayList<>(ExerciseKnowledgeBase.exercisesForType(
                                    ExerciseKnowledgeBase.determineWorkoutType(title.toLowerCase()), user));
                        }

                        while (exercises.size() < MAX_EXERCISES_PER_WORKOUT) {
                            final String fallbackName = "Bodyweight " + (exercises.size() + 1);
                            exercises.add(ExerciseKnowledgeBase.createExercise(fallbackName));
                        }

                        plans.add(new WorkoutPlan(dateStr, title, desc,
                                category, subCategory, intensity, targetMuscle, equipType,
                                duration, calories, fat, carbs, exercises));
                    }
                    else {
                        final String defaultTitle = fallbackTitles[titleIndex];
                        final String defaultDesc = fallbackDescs[titleIndex];
                        final List<Exercise> fallbackEx = ExerciseKnowledgeBase.exercisesForType(
                                ExerciseKnowledgeBase.determineWorkoutType(defaultTitle.toLowerCase()), user);
                        plans.add(new WorkoutPlan(dateStr, defaultTitle, defaultDesc,
                                "STRENGTH", "FULL_BODY", "MEDIUM", "Various", "BODYWEIGHT",
                                this.lastUsedDuration, DEFAULT_CALORIES, DEFAULT_FAT, DEFAULT_CARBS, fallbackEx));
                        activeBlockIndex++;
                    }
                }
                else {
                    plans.add(new WorkoutPlan(dateStr, "Rest & Recovery",
                            "Rest day. Focus on recovery, hydration, and light stretching.",
                            "REST", "REST", "LOW", "NONE", "BODYWEIGHT",
                            0, 0, 0, 0, new ArrayList<>()));
                }
            }

            LOGGER.log(Level.INFO, "Parsed {0} workout plans from API", plans.size());
        }
        // Deliberately broad catch: this parses free-form text from an external API with no
        // enforced schema, so a malformed field can throw any of several unchecked exception
        // types (IndexOutOfBoundsException, NullPointerException, PatternSyntaxException, ...).
        // Narrowing this would leave some of those uncaught, crashing recommendation generation
        // on an unexpected AI response shape instead of falling back gracefully below. This is a
        // known, intentional exception to the project's IllegalCatch rule.
        catch (final RuntimeException ex) {
            LOGGER.log(Level.SEVERE, "Error parsing JSON: {0}", ex.getMessage());
        }

        if (plans.size() < numberOfDays) {
            LOGGER.info("Not enough plans parsed from API, using fallback");
            return this.fallbackStrategy.generatePlans(user, numberOfDays);
        }

        return plans;
    }

    private List<String> extractJsonObjects(final String jsonArray) {
        final List<String> objects = new ArrayList<>();
        int depth = 0;
        int start = -1;
        boolean inQuotes = false;

        for (int i = 0; i < jsonArray.length(); i++) {
            final char c = jsonArray.charAt(i);
            if (c == '"' && (i == 0 || jsonArray.charAt(i - 1) != '\\')) {
                inQuotes = !inQuotes;
            }
            if (!inQuotes) {
                if (c == '{') {
                    if (depth == 0) {
                        start = i;
                    }
                    depth++;
                }
                else if (c == '}') {
                    depth--;
                    if (depth == 0 && start != -1) {
                        objects.add(jsonArray.substring(start, i + 1));
                        start = -1;
                    }
                }
            }
        }
        return objects;
    }

    private String extractVal(final String src, final String key) {
        if (src == null || key == null) {
            return "";
        }
        final int keyIdx = src.indexOf("\"" + key + "\"");
        if (keyIdx == -1) {
            return "";
        }
        int startVal = src.indexOf(":", keyIdx) + 1;
        if (startVal == 0) {
            return "";
        }
        while (startVal < src.length() && (src.charAt(startVal) == ' ' || src.charAt(startVal) == '"')) {
            startVal++;
        }

        if (startVal >= src.length() || src.startsWith("null", startVal)) {
            return "";
        }

        int endVal = startVal;
        while (endVal < src.length()) {
            final char c = src.charAt(endVal);
            if (c == '"' && (endVal == 0 || src.charAt(endVal - 1) != '\\')) {
                break;
            }
            endVal++;
        }

        if (endVal >= src.length() || endVal == startVal) {
            return "";
        }

        return src.substring(startVal, endVal);
    }

    private int extractInt(final String src, final String key, final int defaultVal) {
        if (src == null || key == null) {
            return defaultVal;
        }
        final int keyIdx = src.indexOf("\"" + key + "\"");
        if (keyIdx == -1) {
            return defaultVal;
        }
        int startVal = src.indexOf(":", keyIdx) + 1;
        if (startVal == 0) {
            return defaultVal;
        }
        while (startVal < src.length() && (src.charAt(startVal) == ' ' || src.charAt(startVal) == '"')) {
            startVal++;
        }

        if (startVal >= src.length() || src.startsWith("null", startVal)) {
            return defaultVal;
        }

        final StringBuilder numStr = new StringBuilder();
        while (startVal < src.length() && (Character.isDigit(src.charAt(startVal))
                || src.charAt(startVal) == '-')) {
            numStr.append(src.charAt(startVal));
            startVal++;
        }
        try {
            return Integer.parseInt(numStr.toString());
        }
        catch (final NumberFormatException ex) {
            return defaultVal;
        }
    }
}
