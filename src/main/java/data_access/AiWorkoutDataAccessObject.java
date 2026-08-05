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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import entity.Equipment;
import entity.Exercise;
import entity.User;
import entity.WorkoutPlan;
import use_case.recommendation.AiWorkoutDataAccessInterface;

/**
 * Data Access Object for generating AI workout plans using Gemini API.
 */
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
    private static final int MAX_EXERCISES_PER_WORKOUT = 3;
    private static final int NUTRITION_RANDOM_BOUND = 150;
    private static final int FAT_RANDOM_BOUND = 12;
    private static final int CARBS_RANDOM_BOUND = 25;

    private static final Map<String, String[]> WORKOUT_EXERCISE_MAP = new HashMap<>();
    private static final Map<String, String[]> WORKOUT_INSTRUCTIONS_MAP = new HashMap<>();
    private static final Map<String, String[]> WORKOUT_SEARCH_MAP = new HashMap<>();
    private static final Map<String, String[]> WORKOUT_NUTRITION_MAP = new HashMap<>();
    private static final Map<String, String[]> WORKOUT_SUBCATEGORY_MAP = new HashMap<>();
    private static final Map<String, String[]> WORKOUT_INTENSITY_MAP = new HashMap<>();
    private static final Map<String, String[]> WORKOUT_EQUIPMENT_MAP = new HashMap<>();

    static {
        WORKOUT_EXERCISE_MAP.put("running", new String[]{"Running", "Sprints", "Interval Runs"});
        WORKOUT_INSTRUCTIONS_MAP.put("running", new String[]{
                "Jog or run at a steady pace with proper form.",
                "Sprint at maximum effort, then walk back to recover.",
                "Alternate between sprinting and jogging intervals."
        });
        WORKOUT_SEARCH_MAP.put("running", new String[]{"running", "sprints", "interval+running"});
        WORKOUT_NUTRITION_MAP.put("running", new String[]{"350", "10", "55"});
        WORKOUT_SUBCATEGORY_MAP.put("running", new String[]{"RUNNING", "RUNNING", "RUNNING"});
        WORKOUT_INTENSITY_MAP.put("running", new String[]{"HIGH", "HIGH", "HIGH"});
        WORKOUT_EQUIPMENT_MAP.put("running", new String[]{"BODYWEIGHT", "BODYWEIGHT", "BODYWEIGHT"});

        WORKOUT_EXERCISE_MAP.put("biking", new String[]{"Stationary Bike", "Hill Climbs", "Sprint Intervals"});
        WORKOUT_INSTRUCTIONS_MAP.put("biking", new String[]{
                "Pedal at moderate to high intensity.",
                "Increase resistance and pedal standing up.",
                "Sprint at max effort for 30 seconds, then rest."
        });
        WORKOUT_SEARCH_MAP.put("biking", new String[]{"stationary+bike", "hill+climbs+bike", "bike+sprint+intervals"});
        WORKOUT_NUTRITION_MAP.put("biking", new String[]{"340", "10", "50"});
        WORKOUT_SUBCATEGORY_MAP.put("biking", new String[]{"BIKING", "BIKING", "BIKING"});
        WORKOUT_INTENSITY_MAP.put("biking", new String[]{"MEDIUM", "HIGH", "HIGH"});
        WORKOUT_EQUIPMENT_MAP.put("biking", new String[]{"BODYWEIGHT", "BODYWEIGHT", "BODYWEIGHT"});

        WORKOUT_EXERCISE_MAP.put("cycling", new String[]{"Stationary Bike", "Hill Climbs", "Sprint Intervals"});
        WORKOUT_INSTRUCTIONS_MAP.put("cycling", new String[]{
                "Pedal at moderate to high intensity.",
                "Increase resistance and pedal standing up.",
                "Sprint at max effort for 30 seconds, then rest."
        });
        WORKOUT_SEARCH_MAP.put("cycling", new String[]{"stationary+bike", "hill+climbs+bike", "bike+sprint+intervals"});
        WORKOUT_NUTRITION_MAP.put("cycling", new String[]{"340", "10", "50"});
        WORKOUT_SUBCATEGORY_MAP.put("cycling", new String[]{"BIKING", "BIKING", "BIKING"});
        WORKOUT_INTENSITY_MAP.put("cycling", new String[]{"MEDIUM", "HIGH", "HIGH"});
        WORKOUT_EQUIPMENT_MAP.put("cycling", new String[]{"BODYWEIGHT", "BODYWEIGHT", "BODYWEIGHT"});

        WORKOUT_EXERCISE_MAP.put("upper", new String[]{"Push-Ups", "Tricep Dips", "Pull-Ups"});
        WORKOUT_INSTRUCTIONS_MAP.put("upper", new String[]{
                "Lower chest to floor, push up explosively.",
                "Lower body until arms are at 90 degrees, push up.",
                "Pull chin above bar, lower with control."
        });
        WORKOUT_SEARCH_MAP.put("upper", new String[]{"pushups", "tricep+dips", "pull+ups"});
        WORKOUT_NUTRITION_MAP.put("upper", new String[]{"300", "20", "35"});
        WORKOUT_SUBCATEGORY_MAP.put("upper", new String[]{"UPPER_BODY", "UPPER_BODY", "UPPER_BODY"});
        WORKOUT_INTENSITY_MAP.put("upper", new String[]{"MEDIUM", "MEDIUM", "HIGH"});
        WORKOUT_EQUIPMENT_MAP.put("upper", new String[]{"BODYWEIGHT", "BODYWEIGHT", "BODYWEIGHT"});

        WORKOUT_EXERCISE_MAP.put("lower", new String[]{"Squats", "Lunges", "Calf Raises"});
        WORKOUT_INSTRUCTIONS_MAP.put("lower", new String[]{
                "Keep chest up, lower hips back and down.",
                "Step forward, lower hips until both knees are bent at 90 degrees.",
                "Rise up on toes, hold, lower slowly."
        });
        WORKOUT_SEARCH_MAP.put("lower", new String[]{"squats", "lunges", "calf+raises"});
        WORKOUT_NUTRITION_MAP.put("lower", new String[]{"280", "22", "30"});
        WORKOUT_SUBCATEGORY_MAP.put("lower", new String[]{"LOWER_BODY", "LOWER_BODY", "LOWER_BODY"});
        WORKOUT_INTENSITY_MAP.put("lower", new String[]{"MEDIUM", "MEDIUM", "LOW"});
        WORKOUT_EQUIPMENT_MAP.put("lower", new String[]{"BODYWEIGHT", "BODYWEIGHT", "BODYWEIGHT"});

        WORKOUT_EXERCISE_MAP.put("leg", new String[]{"Squats", "Lunges", "Calf Raises"});
        WORKOUT_INSTRUCTIONS_MAP.put("leg", new String[]{
                "Keep chest up, lower hips back and down.",
                "Step forward, lower hips until both knees are bent at 90 degrees.",
                "Rise up on toes, hold, lower slowly."
        });
        WORKOUT_SEARCH_MAP.put("leg", new String[]{"squats", "lunges", "calf+raises"});
        WORKOUT_NUTRITION_MAP.put("leg", new String[]{"280", "22", "30"});
        WORKOUT_SUBCATEGORY_MAP.put("leg", new String[]{"LOWER_BODY", "LOWER_BODY", "LOWER_BODY"});
        WORKOUT_INTENSITY_MAP.put("leg", new String[]{"MEDIUM", "MEDIUM", "LOW"});
        WORKOUT_EQUIPMENT_MAP.put("leg", new String[]{"BODYWEIGHT", "BODYWEIGHT", "BODYWEIGHT"});

        WORKOUT_EXERCISE_MAP.put("hiit", new String[]{"Burpees", "Mountain Climbers", "Jump Squats"});
        WORKOUT_INSTRUCTIONS_MAP.put("hiit", new String[]{
                "Drop to squat, kick feet back, do push-up, jump up.",
                "In plank position, alternate driving knees to chest.",
                "Squat down, explode up into a jump."
        });
        WORKOUT_SEARCH_MAP.put("hiit", new String[]{"burpees", "mountain+climbers", "jump+squats"});
        WORKOUT_NUTRITION_MAP.put("hiit", new String[]{"400", "15", "40"});
        WORKOUT_SUBCATEGORY_MAP.put("hiit", new String[]{"FULL_BODY", "FULL_BODY", "FULL_BODY"});
        WORKOUT_INTENSITY_MAP.put("hiit", new String[]{"HIGH", "HIGH", "HIGH"});
        WORKOUT_EQUIPMENT_MAP.put("hiit", new String[]{"BODYWEIGHT", "BODYWEIGHT", "BODYWEIGHT"});

        WORKOUT_EXERCISE_MAP.put("core", new String[]{"Planks", "Bicycle Crunches", "Leg Raises"});
        WORKOUT_INSTRUCTIONS_MAP.put("core", new String[]{
                "Hold a straight body line on forearms.",
                "Alternate bringing opposite elbow to knee.",
                "Lie flat, raise legs to 90 degrees, lower with control."
        });
        WORKOUT_SEARCH_MAP.put("core", new String[]{"plank", "bicycle+crunches", "leg+raises"});
        WORKOUT_NUTRITION_MAP.put("core", new String[]{"200", "12", "30"});
        WORKOUT_SUBCATEGORY_MAP.put("core", new String[]{"CORE", "CORE", "CORE"});
        WORKOUT_INTENSITY_MAP.put("core", new String[]{"MEDIUM", "MEDIUM", "LOW"});
        WORKOUT_EQUIPMENT_MAP.put("core", new String[]{"BODYWEIGHT", "BODYWEIGHT", "BODYWEIGHT"});

        WORKOUT_EXERCISE_MAP.put("strength", new String[]{"Deadlifts", "Overhead Press", "Rows"});
        WORKOUT_INSTRUCTIONS_MAP.put("strength", new String[]{
                "Hinge at hips, keep back straight, lift weight.",
                "Press weight overhead, lower with control.",
                "Pull weight towards chest, squeeze shoulder blades."
        });
        WORKOUT_SEARCH_MAP.put("strength", new String[]{"deadlifts", "overhead+press", "rows"});
        WORKOUT_NUTRITION_MAP.put("strength", new String[]{"320", "25", "35"});
        WORKOUT_SUBCATEGORY_MAP.put("strength", new String[]{"FULL_BODY", "UPPER_BODY", "BACK"});
        WORKOUT_INTENSITY_MAP.put("strength", new String[]{"HIGH", "MEDIUM", "MEDIUM"});
        WORKOUT_EQUIPMENT_MAP.put("strength", new String[]{"BARBELL", "DUMBBELLS", "DUMBBELLS"});

        WORKOUT_EXERCISE_MAP.put("yoga", new String[]{"Downward Dog", "Warrior Pose", "Tree Pose"});
        WORKOUT_INSTRUCTIONS_MAP.put("yoga", new String[]{
                "Inverted V position, stretch back and legs.",
                "Lunge position with arms extended.",
                "Balance on one foot, other foot on inner thigh."
        });
        WORKOUT_SEARCH_MAP.put("yoga", new String[]{"downward+dog", "warrior+pose", "tree+pose"});
        WORKOUT_NUTRITION_MAP.put("yoga", new String[]{"120", "4", "15"});
        WORKOUT_SUBCATEGORY_MAP.put("yoga", new String[]{"YOGA", "YOGA", "YOGA"});
        WORKOUT_INTENSITY_MAP.put("yoga", new String[]{"LOW", "LOW", "LOW"});
        WORKOUT_EQUIPMENT_MAP.put("yoga", new String[]{"BODYWEIGHT", "BODYWEIGHT", "BODYWEIGHT"});

        WORKOUT_EXERCISE_MAP.put("flexibility", new String[]{"Downward Dog", "Warrior Pose", "Tree Pose"});
        WORKOUT_INSTRUCTIONS_MAP.put("flexibility", new String[]{
                "Inverted V position, stretch back and legs.",
                "Lunge position with arms extended.",
                "Balance on one foot, other foot on inner thigh."
        });
        WORKOUT_SEARCH_MAP.put("flexibility", new String[]{"downward+dog", "warrior+pose", "tree+pose"});
        WORKOUT_NUTRITION_MAP.put("flexibility", new String[]{"120", "4", "15"});
        WORKOUT_SUBCATEGORY_MAP.put("flexibility", new String[]{"YOGA", "YOGA", "YOGA"});
        WORKOUT_INTENSITY_MAP.put("flexibility", new String[]{"LOW", "LOW", "LOW"});
        WORKOUT_EQUIPMENT_MAP.put("flexibility", new String[]{"BODYWEIGHT", "BODYWEIGHT", "BODYWEIGHT"});
    }

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

        final StringBuilder sb = new StringBuilder();
        for (final Equipment eq : equipmentSet) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
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

                + "CRITICAL RULES:\n"
                + "1. Every workout MUST have a category (CARDIO, STRENGTH, HIIT, CORE, FLEXIBILITY)\n"
                + "2. Every workout MUST have a subCategory (RUNNING, BIKING, UPPER_BODY, LOWER_BODY)\n"
                + "3. The category must match the user's goal\n"
                + "4. The exercises MUST match the category and subCategory\n\n"

                + "Examples:\n"
                + "- Category: CARDIO, SubCategory: BIKING"
                + " -> Exercises: Stationary Bike, Hill Climbs, Sprint Intervals\n"
                + "- Category: STRENGTH, SubCategory: UPPER_BODY"
                + " -> Exercises: Push-Ups, Dips, Pull-Ups\n\n"

                + "Return JSON array of 14 objects with:\n"
                + "date, title, description, category, subCategory, intensityLevel,\n"
                + "targetMuscleGroup, equipmentType, estimatedDurationMinutes,\n"
                + "estimatedCaloriesBurned, estimatedFatBurnedGrams, estimatedCarbsBurnedGrams,\n"
                + "exercises (name, sets, reps, durationMinutes, targetMuscleGroup,\n"
                + "equipmentRequired, instructions, videoUrl, category, subCategory,\n"
                + "intensityLevel, equipmentType)";
    }

    private String buildJsonRequest(final String promptText) {
        final String escapedPrompt = sanitizeJsonString(promptText);
        return "{\n"
                + "  \"contents\": [{\n"
                + "    \"parts\": [{\"text\": " + escapedPrompt + "}]\n"
                + "  }],\n"
                + "  \"system_instruction\": {\n"
                + "    \"parts\": [{\"text\": \""
                + "CRITICAL: Every workout must have category and subCategory. "
                + "Every exercise must have matching category and subCategory. "
                + "Return valid JSON only.\"}]\n"
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
                                       final LocalDate startDate,
                                       final Set<DayOfWeek> preferredDays,
                                       final DateTimeFormatter fmt) {
        final LocalDate currentDate = startDate.plusDays(dayCounter);
        final String date = extractDate(block, currentDate, fmt);
        final String title = extractVal(block, "title");
        final String description = extractDescription(block, date);

        final String category = extractCategory(block, title);
        final String subCategory = extractSubCategory(block, title);
        final String intensityLevel = extractIntensity(block);
        final String targetMuscleGroup = extractVal(block, "targetMuscleGroup");
        final String equipmentType = extractEquipmentType(block);

        final int[] nutrition = getNutritionFromCategory(category);
        final int calories = nutrition[0];
        final int fat = nutrition[1];
        final int carbs = nutrition[2];
        final int durationMinutes = getTargetMinutes(null);

        final DayOfWeek dow = currentDate.getDayOfWeek();
        final boolean isPreferredDay = preferredDays.contains(dow);

        if (!isPreferredDay) {
            return new WorkoutPlan(date, "Rest & Recovery",
                    "Rest day. Focus on recovery, hydration, and light stretching.",
                    "REST", "REST", "LOW", "NONE", "NONE",
                    0, 0, 0, 0, new ArrayList<>());
        }

        final List<Exercise> exercises = getExercisesForWorkout(subCategory, category);

        return new WorkoutPlan(date, title, description, category, subCategory,
                intensityLevel, targetMuscleGroup, equipmentType, durationMinutes,
                calories, fat, carbs, exercises);
    }

    private String extractCategory(final String block, final String title) {
        String category = extractVal(block, "category");
        if (category == null || category.isEmpty()) {
            final String lowerTitle = title.toLowerCase();
            if (lowerTitle.contains("cardio") || lowerTitle.contains("run")
                    || lowerTitle.contains("bike") || lowerTitle.contains("cycle")) {
                return "CARDIO";
            }
            else if (lowerTitle.contains("strength") || lowerTitle.contains("upper")
                    || lowerTitle.contains("lower") || lowerTitle.contains("leg")) {
                return "STRENGTH";
            }
            else if (lowerTitle.contains("hiit") || lowerTitle.contains("interval")) {
                return "HIIT";
            }
            else if (lowerTitle.contains("core") || lowerTitle.contains("abs")) {
                return "CORE";
            }
            else if (lowerTitle.contains("yoga") || lowerTitle.contains("stretch")
                    || lowerTitle.contains("flex")) {
                return "FLEXIBILITY";
            }
            else {
                return "GENERAL";
            }
        }
        return category.toUpperCase();
    }

    private String extractSubCategory(final String block, final String title) {
        String subCategory = extractVal(block, "subCategory");
        if (subCategory == null || subCategory.isEmpty()) {
            final String lowerTitle = title.toLowerCase();
            if (lowerTitle.contains("running") || lowerTitle.contains("run")) {
                return "RUNNING";
            }
            else if (lowerTitle.contains("bike") || lowerTitle.contains("cycle")
                    || lowerTitle.contains("cycling")) {
                return "BIKING";
            }
            else if (lowerTitle.contains("upper")) {
                return "UPPER_BODY";
            }
            else if (lowerTitle.contains("lower") || lowerTitle.contains("leg")) {
                return "LOWER_BODY";
            }
            else if (lowerTitle.contains("hiit") || lowerTitle.contains("interval")) {
                return "FULL_BODY";
            }
            else if (lowerTitle.contains("core") || lowerTitle.contains("abs")) {
                return "CORE";
            }
            else if (lowerTitle.contains("yoga")) {
                return "YOGA";
            }
            else {
                return "GENERAL";
            }
        }
        return subCategory.toUpperCase();
    }

    private String extractIntensity(final String block) {
        String intensity = extractVal(block, "intensityLevel");
        if (intensity == null || intensity.isEmpty()) {
            return "MEDIUM";
        }
        return intensity.toUpperCase();
    }

    private String extractEquipmentType(final String block) {
        String equipment = extractVal(block, "equipmentType");
        if (equipment == null || equipment.isEmpty()) {
            return "BODYWEIGHT";
        }
        return equipment.toUpperCase();
    }

    private int[] getNutritionFromCategory(final String category) {
        final String key = category.toLowerCase();
        if (WORKOUT_NUTRITION_MAP.containsKey(key)) {
            final String[] vals = WORKOUT_NUTRITION_MAP.get(key);
            return new int[]{
                    Integer.parseInt(vals[0]),
                    Integer.parseInt(vals[1]),
                    Integer.parseInt(vals[2])
            };
        }
        return new int[]{DEFAULT_CALORIES, DEFAULT_FAT, DEFAULT_CARBS};
    }

    private List<Exercise> getExercisesForWorkout(final String subCategory, final String category) {
        final List<Exercise> exercises = new ArrayList<>();
        final String key = subCategory.toLowerCase();

        String mapKey = key;
        if (!WORKOUT_EXERCISE_MAP.containsKey(mapKey)) {
            mapKey = category.toLowerCase();
        }

        if (WORKOUT_EXERCISE_MAP.containsKey(mapKey)) {
            final String[] exerciseNames = WORKOUT_EXERCISE_MAP.get(mapKey);
            final String[] instructions = WORKOUT_INSTRUCTIONS_MAP.get(mapKey);
            final String[] searchQueries = WORKOUT_SEARCH_MAP.get(mapKey);
            final String[] subCategories = WORKOUT_SUBCATEGORY_MAP.get(mapKey);
            final String[] intensities = WORKOUT_INTENSITY_MAP.get(mapKey);
            final String[] equipmentTypes = WORKOUT_EQUIPMENT_MAP.get(mapKey);

            for (int i = 0; i < Math.min(MAX_EXERCISES_PER_WORKOUT, exerciseNames.length); i++) {
                final String videoUrl = "https://www.youtube.com/results?search_query="
                        + searchQueries[i] + "+exercise+tutorial";
                final Exercise exercise = new Exercise(
                        exerciseNames[i],
                        DEFAULT_SETS,
                        DEFAULT_REPS,
                        DEFAULT_EX_DURATION,
                        "Various",
                        "Bodyweight",
                        instructions[i],
                        videoUrl,
                        category,
                        subCategories[i],
                        intensities[i],
                        equipmentTypes[i]
                );
                exercises.add(exercise);
            }
        }
        else {
            exercises.add(createExerciseWithAttributes("Push-Ups", "Lower chest to floor, push up.",
                    "pushups", category, subCategory, "MEDIUM", "BODYWEIGHT"));
            exercises.add(createExerciseWithAttributes("Squats", "Lower hips back and down.",
                    "squats", category, subCategory, "MEDIUM", "BODYWEIGHT"));
            exercises.add(createExerciseWithAttributes("Plank", "Hold straight body line.",
                    "plank", category, subCategory, "LOW", "BODYWEIGHT"));
        }

        return exercises;
    }

    private Exercise createExerciseWithAttributes(final String name, final String instructions,
                                                  final String searchQuery, final String category,
                                                  final String subCategory, final String intensity,
                                                  final String equipmentType) {
        return new Exercise(name, DEFAULT_SETS, DEFAULT_REPS, DEFAULT_EX_DURATION,
                "Various", "Bodyweight", instructions,
                "https://www.youtube.com/results?search_query=" + searchQuery + "+exercise+tutorial",
                category, subCategory, intensity, equipmentType);
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
                "Yoga Flow"
        };

        final String[] categories = {"CARDIO", "STRENGTH", "STRENGTH", "HIIT", "CORE", "CARDIO", "FLEXIBILITY"};
        final String[] subCategories = {"RUNNING", "UPPER_BODY", "LOWER_BODY", "FULL_BODY", "CORE", "BIKING", "YOGA"};
        final String[] intensities = {"HIGH", "MEDIUM", "MEDIUM", "HIGH", "MEDIUM", "MEDIUM", "LOW"};
        final String[] targetMuscles = {"LEGS", "CHEST", "LEGS", "FULL_BODY", "CORE", "LEGS", "FULL_BODY"};
        final String[] equipmentTypes = {"BODYWEIGHT", "BODYWEIGHT", "BODYWEIGHT", "BODYWEIGHT", "BODYWEIGHT", "BODYWEIGHT", "BODYWEIGHT"};

        final String[] descs = {
                "Cardio running workout with Running, Sprints, and Jumping Jacks.",
                "Upper body strength workout with Push-Ups, Tricep Dips, and Pull-Ups.",
                "Leg workout with Squats, Lunges, and Calf Raises.",
                "Full body HIIT workout with Burpees, Mountain Climbers, and Jump Squats.",
                "Core workout with Planks, Bicycle Crunches, and Leg Raises.",
                "Cardio biking workout with Stationary Bike, Hill Climbs, and Sprint Intervals.",
                "Yoga flow workout with Downward Dog, Warrior Pose, and Tree Pose."
        };

        final int[][] nutritionValues = {
                {350, 10, 55},
                {300, 20, 35},
                {280, 22, 30},
                {400, 15, 40},
                {200, 12, 30},
                {340, 10, 50},
                {120, 4, 15}
        };

        final String[][] exerciseNames = {
                {"Running", "Sprints", "Jumping Jacks"},
                {"Push-Ups", "Tricep Dips", "Pull-Ups"},
                {"Squats", "Lunges", "Calf Raises"},
                {"Burpees", "Mountain Climbers", "Jump Squats"},
                {"Planks", "Bicycle Crunches", "Leg Raises"},
                {"Stationary Bike", "Hill Climbs", "Sprint Intervals"},
                {"Downward Dog", "Warrior Pose", "Tree Pose"}
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
                {"Inverted V position, stretch back and legs.",
                        "Lunge position with arms extended.",
                        "Balance on one foot, other foot on inner thigh."}
        };

        final String[][] searchQueries = {
                {"running", "sprints", "jumping+jacks"},
                {"pushups", "tricep+dips", "pull+ups"},
                {"squats", "lunges", "calf+raises"},
                {"burpees", "mountain+climbers", "jump+squats"},
                {"plank", "bicycle+crunches", "leg+raises"},
                {"stationary+bike", "hill+climbs+bike", "bike+sprint+intervals"},
                {"downward+dog", "warrior+pose", "tree+pose"}
        };

        for (int dayOffset = 0; dayOffset < TOTAL_DAYS; dayOffset++) {
            final LocalDate date = today.plusDays(dayOffset);
            final String dateLabel = date.format(fmt);
            final DayOfWeek dow = date.getDayOfWeek();

            if (preferredDays.contains(dow)) {
                final List<Exercise> exercises = new ArrayList<>();
                final int workoutType = dayOffset % 7;
                final String category = categories[workoutType];
                final String subCategory = subCategories[workoutType];
                final String intensity = intensities[workoutType];
                final String equipmentType = equipmentTypes[workoutType];

                for (int i = 0; i < MAX_EXERCISES_PER_WORKOUT; i++) {
                    final Exercise exercise = new Exercise(
                            exerciseNames[workoutType][i],
                            DEFAULT_SETS,
                            DEFAULT_REPS,
                            DEFAULT_EX_DURATION,
                            "Various",
                            "Bodyweight",
                            exerciseInstructions[workoutType][i],
                            "https://www.youtube.com/results?search_query="
                                    + searchQueries[workoutType][i] + "+exercise+tutorial",
                            category,
                            subCategory,
                            intensity,
                            equipmentType
                    );
                    exercises.add(exercise);
                }

                plans.add(new WorkoutPlan(dateLabel, titles[workoutType],
                        descs[workoutType],
                        category,
                        subCategory,
                        intensity,
                        targetMuscles[workoutType],
                        equipmentType,
                        DEFAULT_WORKOUT_DURATION,
                        nutritionValues[workoutType][0],
                        nutritionValues[workoutType][1],
                        nutritionValues[workoutType][2],
                        exercises));
            }
            else {
                plans.add(new WorkoutPlan(dateLabel, "Rest & Recovery",
                        "Rest day. Focus on recovery, hydration, and light stretching.",
                        "REST", "REST", "LOW", "NONE", "NONE",
                        0, 0, 0, 0, new ArrayList<>()));
            }
        }

        System.out.println("Generated " + plans.size() + " fallback plans");
        return plans;
    }
}