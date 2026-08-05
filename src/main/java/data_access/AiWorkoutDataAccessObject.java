package data_access;

import java.io.BufferedReader;
import java.io.File;
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
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import entity.Equipment;
import entity.Exercise;
import entity.User;
import entity.WorkoutPlan;
import use_case.recommendation.AiWorkoutDataAccessInterface;

public class AiWorkoutDataAccessObject implements AiWorkoutDataAccessInterface {

    private static final int HTTP_OK = 200;
    private static final int TIMEOUT_MILLIS = 60000;
    private static final int SEED_MAX = 100000;
    private static final int DEFAULT_CALORIES = 320;
    private static final int DEFAULT_FAT = 15;
    private static final int DEFAULT_CARBS = 45;
    private static final int TOTAL_DAYS = 14;
    private static final int DEFAULT_SETS = 3;
    private static final int DEFAULT_REPS = 12;
    private static final int DEFAULT_EX_DURATION = 10;
    private static final int HEX_PAD_LEN = 4;
    private static final int JSON_TEXT_OFFSET = 9;
    private static final int DEFAULT_WORKOUT_DURATION = 45;

    private final String apiKey;
    private final Random random = new Random();

    public AiWorkoutDataAccessObject() {
        String key = loadKeyFromDotEnv();
        if (key == null || key.trim().isEmpty()) {
            key = System.getenv("GEMINI_API_KEY");
        }
        this.apiKey = key;
        logApiKeyStatus();
    }

    public AiWorkoutDataAccessObject(final String apiKey) {
        this.apiKey = apiKey;
        logApiKeyStatus();
    }

    private void logApiKeyStatus() {
        final boolean isValid = this.apiKey != null && !this.apiKey.isEmpty()
                && !"YOUR_API_KEY_HERE".equals(this.apiKey);
        System.out.println("API Key loaded: " + (isValid ? "Valid" : "Invalid/Missing"));
    }

    private String loadKeyFromDotEnv() {
        final File envFile = new File(".env");
        if (!envFile.exists()) {
            System.out.println(".env file not found");
            return null;
        }
        try (Scanner scanner = new Scanner(envFile, StandardCharsets.UTF_8)) {
            while (scanner.hasNextLine()) {
                final String line = scanner.nextLine().trim();
                if (line.startsWith("GEMINI_API_KEY=")) {
                    System.out.println("Found API key in .env file");
                    return line.substring("GEMINI_API_KEY=".length()).trim();
                }
            }
        }
        catch (final Exception ex) {
            System.out.println("Error reading .env file: " + ex.getMessage());
        }
        return null;
    }

    @Override
    public List<WorkoutPlan> generateWorkoutPlans(final User user) {
        System.out.println("generateWorkoutPlans called for user");

        if (!isUserValid(user)) {
            return getFallback2WeekPlans(user);
        }

        if (!isApiKeyValid()) {
            System.out.println("API key is invalid or missing, using fallback");
            return getFallback2WeekPlans(user);
        }

        try {
            final List<WorkoutPlan> plans = callGeminiApi(user);
            if (plans != null && !plans.isEmpty() && plans.size() >= TOTAL_DAYS) {
                System.out.println("Successfully parsed " + plans.size() + " workout plans");
                return plans;
            }
        }
        catch (final Throwable ex) {
            System.out.println("API call failed: " + ex.getMessage());
            ex.printStackTrace();
        }

        System.out.println("Using fallback plans");
        return getFallback2WeekPlans(user);
    }

    private boolean isUserValid(final User user) {
        return user != null && user.getGoal() != null;
    }

    private boolean isApiKeyValid() {
        return this.apiKey != null && !this.apiKey.trim().isEmpty()
                && !"YOUR_API_KEY_HERE".equals(this.apiKey);
    }

    private List<WorkoutPlan> callGeminiApi(final User user) throws Exception {
        final String endpoint = buildEndpoint();
        System.out.println("Calling Gemini API with endpoint: " + endpoint);

        final HttpURLConnection connection = createConnection(endpoint);
        final String promptText = buildPromptText(user);
        final String jsonRequest = buildJsonRequest(promptText);

        sendRequest(connection, jsonRequest);
        final int responseCode = connection.getResponseCode();
        System.out.println("Response code: " + responseCode);

        if (responseCode == HTTP_OK) {
            return handleSuccessResponse(connection, user.getPreferredWorkoutDays());
        }
        else {
            handleErrorResponse(connection);
            return null;
        }
    }

    private String buildEndpoint() {
        return "https://generativelanguage.googleapis.com/v1beta/models/"
                + "gemini-3.5-flash:generateContent?key=" + this.apiKey;
    }

    private HttpURLConnection createConnection(final String endpoint) throws Exception {
        final URL url = new URL(endpoint);
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setConnectTimeout(TIMEOUT_MILLIS);
        connection.setReadTimeout(TIMEOUT_MILLIS);
        connection.setDoOutput(true);
        return connection;
    }

    private String buildPromptText(final User user) {
        final LocalDate today = LocalDate.now();
        final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("EEEE, MMM d");
        final String startDateStr = today.format(fmt);

        final Set<DayOfWeek> preferredDays = getPreferredDays(user);
        final int targetMinutes = getTargetMinutes(user);
        final String equipmentStr = getEquipmentString(user);
        final String genderStr = getGenderString(user);
        final String activityStr = getActivityString(user);
        final double bmi = calculateBmi(user);
        final int seed = this.random.nextInt(SEED_MAX);

        return buildPromptContent(startDateStr, user, preferredDays, targetMinutes,
                equipmentStr, genderStr, activityStr, bmi, seed);
    }

    private Set<DayOfWeek> getPreferredDays(final User user) {
        final Set<DayOfWeek> days = user.getPreferredWorkoutDays();
        if (days == null || days.isEmpty()) {
            return Set.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY);
        }
        return days;
    }

    private int getTargetMinutes(final User user) {
        final int minutes = user.getPreferredWorkoutDurationMinutes();
        return minutes > 0 ? minutes : DEFAULT_WORKOUT_DURATION;
    }

    private String getEquipmentString(final User user) {
        final Set<Equipment> equipmentSet = user.getEquipment();
        if (equipmentSet == null || equipmentSet.isEmpty()) {
            return "Bodyweight only";
        }

        // Convert Equipment enum to readable string
        final StringBuilder sb = new StringBuilder();
        for (final Equipment eq : equipmentSet) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            // Convert enum name to readable format (e.g., DUMBBELL -> Dumbbell)
            final String name = eq.name().toLowerCase().replace("_", " ");
            sb.append(Character.toUpperCase(name.charAt(0)) + name.substring(1));
        }
        return sb.toString();
    }

    private String getGenderString(final User user) {
        return user.getGender() != null ? user.getGender().toString() : "Unspecified";
    }

    private String getActivityString(final User user) {
        return user.getActivityLevel() != null
                ? user.getActivityLevel().getDescription()
                : "Moderately active";
    }

    private double calculateBmi(final User user) {
        if (user.getWeight() > 0 && user.getHeight() > 0) {
            return user.getWeight() / (user.getHeight() * user.getHeight());
        }
        return 0;
    }

    private String buildPromptContent(final String startDateStr, final User user,
                                      final Set<DayOfWeek> preferredDays, final int targetMinutes,
                                      final String equipmentStr, final String genderStr,
                                      final String activityStr, final double bmi, final int seed) {
        final String daysList = preferredDays.toString();
        final String goalStr = user.getGoal().toString();
        final double weight = user.getWeight();
        final double height = user.getHeight();

        return "Create a 14-day workout plan. User profile:\n"
                + "- Goal: " + goalStr + "\n"
                + "- Weight: " + weight + "kg, Height: " + height + "m, BMI: "
                + String.format("%.1f", bmi) + ", Gender: " + genderStr + "\n"
                + "- Activity Level: " + activityStr + "\n"
                + "- Equipment available: " + equipmentStr + "\n"
                + "- Workout days: " + daysList + "\n"
                + "- Session duration: " + targetMinutes + " minutes\n\n"

                + "CRITICAL RULE: The exercises MUST match the workout title. Examples:\n"
                + "  - Title Cardio Running -> exercises: Running, Sprints, Jogging\n"
                + "  - Title Biking -> exercises: Stationary Bike, Hill Climbs\n"
                + "  - Title Upper Body Strength -> exercises: Push-Ups, Dips, Pull-Ups\n"
                + "  - Title Leg Day -> exercises: Squats, Lunges, Calf Raises\n"
                + "  - Title Full Body -> exercises: Burpees, Mountain Climbers, Jumping Jacks\n"
                + "  - Title Core Workout -> exercises: Planks, Crunches, Leg Raises\n"
                + "  - Title Yoga -> exercises: Downward Dog, Warrior, Tree Pose\n"
                + "  - Title Swimming -> exercises: Freestyle, Breaststroke, Backstroke\n\n"

                + "NEVER put push-ups or squats in a cardio workout.\n"
                + "NEVER put running or biking in a strength workout.\n"
                + "The exercises MUST be directly related to the workout type in the title.\n\n"

                + "Requirements:\n"
                + "1. Only schedule workouts on: " + daysList + ". All other days are rest days.\n"
                + "2. Each active workout has 3-5 exercises.\n"
                + "3. Use only equipment available: " + equipmentStr + "\n"
                + "4. Every exercise must have instructions.\n"
                + "5. Every exercise must have a YouTube search URL.\n\n"

                + "Return a JSON array of exactly 14 objects with fields:\n"
                + "date, title, description, estimatedCaloriesBurned, estimatedFatBurnedGrams, \n"
                + "estimatedCarbsBurnedGrams, exercises (array with: name, sets, reps, \n"
                + "durationMinutes, targetMuscleGroup, equipmentRequired, instructions, videoUrl)";
    }

    private String buildJsonRequest(final String promptText) {
        final String escapedPrompt = sanitizeJsonString(promptText);
        return "{\n"
                + "  \"contents\": [{\n"
                + "    \"parts\": [{\"text\": " + escapedPrompt + "}]\n"
                + "  }],\n"
                + "  \"system_instruction\": {\n"
                + "    \"parts\": [{\"text\": \"You are a personal trainer. "
                + "The exercises MUST match the workout title. "
                + "If title says Cardio, all exercises must be cardio. "
                + "If title says Strength, all exercises must be strength. "
                + "If title says Yoga, all exercises must be yoga poses. "
                + "Do NOT mix different workout types in one session. "
                + "Respond with valid JSON only. No explanations. "
                + "Return exactly 14 objects in the array.\"}]\n"
                + "  }\n"
                + "}";
    }

    private void sendRequest(final HttpURLConnection connection, final String jsonRequest) throws Exception {
        System.out.println("Sending request to Gemini API...");
        try (OutputStream outputStream = connection.getOutputStream()) {
            final byte[] input = jsonRequest.getBytes(StandardCharsets.UTF_8);
            outputStream.write(input, 0, input.length);
        }
    }

    private List<WorkoutPlan> handleSuccessResponse(final HttpURLConnection connection,
                                                    final Set<DayOfWeek> preferredDays) throws Exception {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            final StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line.trim());
            }
            final String responseStr = response.toString();
            System.out.println("API Response received, length: " + responseStr.length());

            return parseGeminiJsonResponse(responseStr, preferredDays, LocalDate.now());
        }
    }

    private void handleErrorResponse(final HttpURLConnection connection) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8))) {
            final StringBuilder errorResponse = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                errorResponse.append(line.trim());
            }
            System.out.println("API Error Response: " + errorResponse.toString());
        }
        catch (final Exception ex) {
            System.out.println("Could not read error response: " + ex.getMessage());
        }
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

    private List<WorkoutPlan> parseGeminiJsonResponse(final String response,
                                                      final Set<DayOfWeek> preferredDays,
                                                      final LocalDate startDate) {
        final List<WorkoutPlan> plans = new ArrayList<>();
        final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("EEEE, MMM d");

        try {
            final String rawJson = extractJsonFromResponse(response);
            final String jsonArray = extractJsonArray(rawJson);

            if (jsonArray.isEmpty()) {
                System.out.println("Could not find JSON array in response");
                return plans;
            }

            int searchPos = 0;
            int dayCounter = 0;

            while (dayCounter < TOTAL_DAYS) {
                final String block = findNextBlock(jsonArray, searchPos);
                if (block == null) {
                    break;
                }

                searchPos = jsonArray.indexOf("}", searchPos) + 1;
                final WorkoutPlan plan = parseSingleDay(block, dayCounter, startDate,
                        preferredDays, fmt);
                if (plan != null) {
                    plans.add(plan);
                }
                dayCounter++;
            }

            System.out.println("Parsed " + plans.size() + " workout plans");
        }
        catch (final Exception ex) {
            System.out.println("Error parsing JSON: " + ex.getMessage());
            ex.printStackTrace();
        }

        if (plans.size() < TOTAL_DAYS) {
            System.out.println("Not enough plans parsed, using fallback");
            return getFallback2WeekPlans(null);
        }
        return plans;
    }

    private String extractJsonFromResponse(final String response) {
        if (!response.contains("\"text\": \"")) {
            return response;
        }

        final int textIdx = response.indexOf("\"text\": \"");
        final int start = textIdx + JSON_TEXT_OFFSET;
        final int end = response.lastIndexOf("\"");
        if (start >= end) {
            return response;
        }

        return response.substring(start, end)
                .replace("\\n", " ")
                .replace("\\\"", "\"")
                .replace("\\\\", "\\");
    }

    private String extractJsonArray(final String rawJson) {
        final int arrayStart = rawJson.indexOf("[");
        final int arrayEnd = rawJson.lastIndexOf("]");
        if (arrayStart == -1 || arrayEnd == -1) {
            return "";
        }
        return rawJson.substring(arrayStart, arrayEnd + 1);
    }

    private String findNextBlock(final String jsonArray, final int searchPos) {
        final int dateIdx = jsonArray.indexOf("\"date\"", searchPos);
        if (dateIdx == -1) {
            return null;
        }

        final int objStart = jsonArray.lastIndexOf("{", dateIdx);
        if (objStart == -1) {
            return null;
        }

        final int objEnd = jsonArray.indexOf("}", objStart);
        if (objEnd == -1) {
            return null;
        }

        return jsonArray.substring(objStart, objEnd + 1);
    }

    private WorkoutPlan parseSingleDay(final String block, final int dayCounter,
                                       final LocalDate startDate, final Set<DayOfWeek> preferredDays,
                                       final DateTimeFormatter fmt) {
        final LocalDate currentDate = startDate.plusDays(dayCounter);
        final String date = extractDate(block, currentDate, fmt);
        final String title = extractVal(block, "title");
        final String description = extractDescription(block, date);
        final int calories = extractInt(block, "estimatedCaloriesBurned", DEFAULT_CALORIES);
        final int fat = extractInt(block, "estimatedFatBurnedGrams", DEFAULT_FAT);
        final int carbs = extractInt(block, "estimatedCarbsBurnedGrams", DEFAULT_CARBS);

        final DayOfWeek dow = currentDate.getDayOfWeek();
        final boolean isPreferredDay = preferredDays.contains(dow);

        if (!isPreferredDay) {
            return new WorkoutPlan(date, "Rest & Recovery",
                    "Rest day. Focus on recovery, hydration, and light stretching.",
                    0, 0, 0, new ArrayList<>());
        }

        final List<Exercise> exercises = parseExercises(block, title, dayCounter);
        return new WorkoutPlan(date, title, description, calories, fat, carbs, exercises);
    }

    private String extractDate(final String block, final LocalDate currentDate,
                               final DateTimeFormatter fmt) {
        String date = extractVal(block, "date");
        if (date.isEmpty()) {
            return currentDate.format(fmt);
        }

        if (date.matches("\\d+")) {
            final int dayNum = Integer.parseInt(date);
            if (dayNum > 0 && dayNum <= TOTAL_DAYS) {
                return currentDate.plusDays(dayNum - 1).format(fmt);
            }
        }
        return currentDate.format(fmt);
    }

    private String extractDescription(final String block, final String date) {
        final String desc = extractVal(block, "description");
        return desc.isEmpty() ? "Custom workout for " + date : desc;
    }

    private List<Exercise> parseExercises(final String block, final String title,
                                          final int dayCounter) {
        final List<Exercise> exercises = new ArrayList<>();

        int exSearchPos = block.indexOf("exercises");
        if (exSearchPos != -1) {
            final int exArrayStart = block.indexOf("[", exSearchPos);
            if (exArrayStart != -1) {
                final int exArrayEnd = block.lastIndexOf("]");
                if (exArrayEnd != -1 && exArrayEnd > exArrayStart) {
                    final String exArray = block.substring(exArrayStart + 1, exArrayEnd);
                    final String[] exBlocks = exArray.split("(?<=\\}),\\s*");

                    for (String exBlock : exBlocks) {
                        final Exercise exercise = parseSingleExercise(exBlock);
                        if (exercise != null) {
                            exercises.add(exercise);
                        }
                    }
                }
            }
        }

        if (exercises.isEmpty()) {
            System.out.println("No exercises parsed for day " + dayCounter + ", using fallback");
            return getFallbackExercisesForTitle(title, dayCounter);
        }

        return exercises;
    }

    private Exercise parseSingleExercise(final String exBlock) {
        if (!exBlock.contains("name")) {
            return null;
        }

        final String name = extractVal(exBlock, "name");
        if (name.isEmpty()) {
            return null;
        }

        final int sets = extractInt(exBlock, "sets", DEFAULT_SETS);
        final int reps = extractInt(exBlock, "reps", DEFAULT_REPS);
        final int exDuration = extractInt(exBlock, "durationMinutes", DEFAULT_EX_DURATION);
        final String targetMuscle = extractVal(exBlock, "targetMuscleGroup");
        final String equipReq = extractVal(exBlock, "equipmentRequired");
        String instructions = extractVal(exBlock, "instructions");
        String videoUrl = extractVal(exBlock, "videoUrl");

        if (instructions.isEmpty()) {
            instructions = "Perform " + name + " with proper form.";
        }
        if (videoUrl.isEmpty()) {
            videoUrl = "https://www.youtube.com/results?search_query="
                    + name.replace(" ", "+") + "+tutorial";
        }

        return new Exercise(name, sets, reps, exDuration, targetMuscle,
                equipReq, instructions, videoUrl);
    }

    private List<Exercise> getFallbackExercisesForTitle(final String title, final int dayCounter) {
        final List<Exercise> exercises = new ArrayList<>();

        final String lowerTitle = title.toLowerCase();
        if (lowerTitle.contains("cardio") || lowerTitle.contains("run")
                || lowerTitle.contains("bike") || lowerTitle.contains("cycle")) {
            exercises.add(createExercise("Running", "Jog or run at a steady pace.",
                    "running+tutorial"));
            exercises.add(createExercise("Sprints", "Sprint at maximum effort, then rest.",
                    "sprints+tutorial"));
            exercises.add(createExercise("Jumping Jacks",
                    "Jump with arms and legs out, then back together.",
                    "jumping+jacks+tutorial"));
        }
        else if (lowerTitle.contains("strength") || lowerTitle.contains("upper")
                || lowerTitle.contains("lower") || lowerTitle.contains("leg")
                || lowerTitle.contains("arm") || lowerTitle.contains("chest")) {
            exercises.add(createExercise("Push-Ups", "Lower chest to floor, push up.",
                    "pushups+tutorial"));
            exercises.add(createExercise("Squats", "Lower hips back and down.",
                    "squats+tutorial"));
            exercises.add(createExercise("Plank", "Hold straight body line.",
                    "plank+tutorial"));
        }
        else if (lowerTitle.contains("yoga") || lowerTitle.contains("stretch")) {
            exercises.add(createExercise("Downward Dog",
                    "Inverted V position, stretch back and legs.",
                    "downward+dog+tutorial"));
            exercises.add(createExercise("Warrior", "Lunge position with arms extended.",
                    "warrior+pose+tutorial"));
            exercises.add(createExercise("Tree Pose",
                    "Balance on one foot, other foot on inner thigh.",
                    "tree+pose+tutorial"));
        }
        else {
            if (dayCounter % 3 == 0) {
                exercises.add(createExercise("Push-Ups", "Lower chest to floor, push up.",
                        "pushups+tutorial"));
                exercises.add(createExercise("Squats", "Lower hips back and down.",
                        "squats+tutorial"));
                exercises.add(createExercise("Plank", "Hold straight body line.",
                        "plank+tutorial"));
            }
            else if (dayCounter % 3 == 1) {
                exercises.add(createExercise("Lunges", "Step forward, lower hips.",
                        "lunges+tutorial"));
                exercises.add(createExercise("Dips", "Lower body, push up.",
                        "dips+tutorial"));
                exercises.add(createExercise("Glute Bridges", "Lift hips up.",
                        "glute+bridge+tutorial"));
            }
            else {
                exercises.add(createExercise("Burpees",
                        "Drop to squat, kick back, push-up, jump.",
                        "burpees+tutorial"));
                exercises.add(createExercise("Mountain Climbers",
                        "Alternate knees to chest.",
                        "mountain+climbers+tutorial"));
                exercises.add(createExercise("Crunches", "Lift shoulders off floor.",
                        "crunches+tutorial"));
            }
        }

        return exercises;
    }

    private Exercise createExercise(final String name, final String instructions,
                                    final String searchQuery) {
        return new Exercise(name, DEFAULT_SETS, DEFAULT_REPS, DEFAULT_EX_DURATION,
                "Full Body", "Bodyweight", instructions,
                "https://www.youtube.com/results?search_query=" + searchQuery);
    }

    private String extractVal(final String src, final String key) {
        if (src == null || key == null) {
            return "";
        }
        final int keyIdx = src.indexOf(key);
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
        final int keyIdx = src.indexOf(key);
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
        catch (final Exception ex) {
            return defaultVal;
        }
    }

    private List<WorkoutPlan> getFallback2WeekPlans(final User user) {
        System.out.println("Generating fallback 2-week plans with varied exercises");
        final List<WorkoutPlan> plans = new ArrayList<>();
        final LocalDate today = LocalDate.now();
        final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("EEEE, MMM d");

        Set<DayOfWeek> preferredDays = Set.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY);
        if (user != null && user.getPreferredWorkoutDays() != null
                && !user.getPreferredWorkoutDays().isEmpty()) {
            preferredDays = user.getPreferredWorkoutDays();
            System.out.println("Using preferred days: " + preferredDays);
        }

        final String[] titles = {
                "Cardio Running",
                "Upper Body Strength",
                "Leg Day",
                "Full Body HIIT",
                "Core Workout",
                "Cardio Biking",
                "Strength & Conditioning"
        };
        final String[] descs = {
                "High intensity running intervals",
                "Build upper body strength",
                "Lower body power and endurance",
                "Full body high intensity workout",
                "Core stability and strength",
                "Endurance biking session",
                "Complete strength and conditioning"
        };

        final String[][] exerciseNames = {
                {"Running", "Sprints", "Jumping Jacks"},
                {"Push-Ups", "Tricep Dips", "Pull-Ups"},
                {"Squats", "Lunges", "Calf Raises"},
                {"Burpees", "Mountain Climbers", "Jump Squats"},
                {"Planks", "Bicycle Crunches", "Leg Raises"},
                {"Stationary Bike", "Hill Climbs", "Sprint Intervals"},
                {"Deadlifts", "Overhead Press", "Rows"}
        };

        final String[][] exerciseInstructions = {
                {"Jog or run at a steady pace with proper form.",
                        "Sprint at maximum effort, walk back to recover.",
                        "Jump with arms and legs out, then back together."},
                {"Lower chest to floor, push up explosively.",
                        "Lower body until arms are at 90 degrees, push up.",
                        "Pull chin above bar, lower with control."},
                {"Keep chest up, lower hips back and down.",
                        "Step forward, lower hips until both knees are bent at 90 degrees.",
                        "Rise up on toes, hold, lower slowly."},
                {"Drop to squat, kick feet back, do push-up, jump up.",
                        "In plank position, alternate driving knees to chest.",
                        "Squat down, explode up into a jump."},
                {"Hold a straight body line on forearms.",
                        "Alternate bringing opposite elbow to knee.",
                        "Lie flat, raise legs to 90 degrees, lower with control."},
                {"Pedal at moderate to high intensity.",
                        "Increase resistance, pedal standing up.",
                        "Sprint at max effort for 30 seconds, rest."},
                {"Hinge at hips, keep back straight, lift weight.",
                        "Press weight overhead, lower with control.",
                        "Pull weight towards chest, squeeze shoulder blades."}
        };

        final String[][] searchQueries = {
                {"running+technique+tutorial", "sprint+drills+tutorial", "jumping+jacks+tutorial"},
                {"pushups+tutorial", "tricep+dips+tutorial", "pull+ups+tutorial"},
                {"squats+tutorial", "lunges+tutorial", "calf+raises+tutorial"},
                {"burpees+tutorial", "mountain+climbers+tutorial", "jump+squats+tutorial"},
                {"plank+tutorial", "bicycle+crunches+tutorial", "leg+raises+tutorial"},
                {"stationary+bike+tutorial", "hill+climb+bike+tutorial", "bike+sprint+intervals+tutorial"},
                {"deadlift+tutorial", "overhead+press+tutorial", "rows+tutorial"}
        };

        for (int dayOffset = 0; dayOffset < TOTAL_DAYS; dayOffset++) {
            final LocalDate date = today.plusDays(dayOffset);
            final String dateLabel = date.format(fmt);
            final DayOfWeek dow = date.getDayOfWeek();

            if (preferredDays.contains(dow)) {
                final List<Exercise> exercises = new ArrayList<>();
                final int workoutType = dayOffset % 7;

                for (int i = 0; i < 3; i++) {
                    final Exercise exercise = new Exercise(
                            exerciseNames[workoutType][i],
                            DEFAULT_SETS,
                            DEFAULT_REPS,
                            DEFAULT_EX_DURATION,
                            "Various",
                            "Bodyweight",
                            exerciseInstructions[workoutType][i],
                            "https://www.youtube.com/results?search_query=" + searchQueries[workoutType][i]
                    );
                    exercises.add(exercise);
                }

                plans.add(new WorkoutPlan(dateLabel, titles[workoutType],
                        descs[workoutType],
                        DEFAULT_CALORIES, DEFAULT_FAT, DEFAULT_CARBS, exercises));
            }
            else {
                plans.add(new WorkoutPlan(dateLabel, "Rest & Recovery",
                        "Rest day. Focus on recovery, hydration, and light stretching.",
                        0, 0, 0, new ArrayList<>()));
            }
        }

        System.out.println("Generated " + plans.size() + " fallback plans");
        return plans;
    }
}