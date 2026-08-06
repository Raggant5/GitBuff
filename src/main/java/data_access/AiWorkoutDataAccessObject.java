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
import java.util.logging.Level;
import java.util.logging.Logger;

import entity.Equipment;
import entity.Exercise;
import entity.FitnessGoal;
import entity.User;
import entity.WorkoutPlan;
import use_case.recommendation.AiWorkoutDataAccessInterface;

/**
 * Data access object for retrieving AI generated workout plans.
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
    private static final int JSON_TEXT_OFFSET = 9;
    private static final int DEFAULT_WORKOUT_DURATION = 45;
    private static final int MAX_EXERCISES_PER_WORKOUT = 3;
    private static final int CONNECT_TIMEOUT_MILLIS = 10000;
    private static final int READ_TIMEOUT_MILLIS = 15000;

    private static final String API_VERSION = "v1";
    private static final String GEMINI_MODEL = "gemini-3.5-flash-lite";

    private static final Map<String, String[]> WORKOUT_EXERCISE_MAP = new HashMap<>();
    private static final Map<String, String[]> WORKOUT_INSTRUCTIONS_MAP = new HashMap<>();
    private static final Map<String, String[]> WORKOUT_SEARCH_MAP = new HashMap<>();
    private static final Map<String, String[]> WORKOUT_NUTRITION_MAP = new HashMap<>();
    private static final Map<String, String[]> WORKOUT_SUBCATEGORY_MAP = new HashMap<>();
    private static final Map<String, String[]> WORKOUT_INTENSITY_MAP = new HashMap<>();
    private static final Map<String, String[]> WORKOUT_EQUIPMENT_MAP = new HashMap<>();
    private static final Map<String, String[]> WORKOUT_MUSCLE_MAP = new HashMap<>();
    private static final Map<String, String[]> WORKOUT_CATEGORY_MAP = new HashMap<>();

    private static final Map<FitnessGoal, String[]> GOAL_WORKOUT_TYPES = new HashMap<>();
    private static final Map<FitnessGoal, String[]> GOAL_WORKOUT_TITLES = new HashMap<>();
    private static final Map<FitnessGoal, String[]> GOAL_WORKOUT_DESCS = new HashMap<>();

    static {
        WORKOUT_EXERCISE_MAP.put("running", new String[]{"Running", "Sprints", "Jumping Jacks"});
        WORKOUT_INSTRUCTIONS_MAP.put("running", new String[]{
                "Jog or run at a steady pace with proper form. Keep your back straight and arms pumping.",
                "Sprint at maximum effort for 30 seconds, then walk back to recover. Repeat 8-10 times.",
                "Jump with arms and legs out, then back together. Keep a steady rhythm."
        });
        WORKOUT_SEARCH_MAP.put("running", new String[]{"running+form", "sprint+workout", "jumping+jacks"});
        WORKOUT_NUTRITION_MAP.put("running", new String[]{"350", "10", "55"});
        WORKOUT_SUBCATEGORY_MAP.put("running", new String[]{"RUNNING", "RUNNING", "RUNNING"});
        WORKOUT_INTENSITY_MAP.put("running", new String[]{"HIGH", "HIGH", "MEDIUM"});
        WORKOUT_EQUIPMENT_MAP.put("running", new String[]{"BODYWEIGHT", "BODYWEIGHT", "BODYWEIGHT"});
        WORKOUT_MUSCLE_MAP.put("running", new String[]{"LEGS", "LEGS", "FULL_BODY"});
        WORKOUT_CATEGORY_MAP.put("running", new String[]{"CARDIO", "CARDIO", "CARDIO"});

        WORKOUT_EXERCISE_MAP.put("biking", new String[]{"Stationary Bike", "Hill Climbs", "Sprint Intervals"});
        WORKOUT_INSTRUCTIONS_MAP.put("biking", new String[]{
                "Pedal at moderate to high intensity. Maintain a steady cadence of 80-100 RPM.",
                "Increase resistance and pedal standing up. Simulate climbing a steep hill.",
                "Sprint at max effort for 30 seconds, then pedal easy for 60 seconds. Repeat 10 times."
        });
        WORKOUT_SEARCH_MAP.put("biking", new String[]{
                "stationary+bike+workout", "hill+climbs+bike", "bike+sprint+intervals"
        });
        WORKOUT_NUTRITION_MAP.put("biking", new String[]{"340", "10", "50"});
        WORKOUT_SUBCATEGORY_MAP.put("biking", new String[]{"BIKING", "BIKING", "BIKING"});
        WORKOUT_INTENSITY_MAP.put("biking", new String[]{"MEDIUM", "HIGH", "HIGH"});
        WORKOUT_EQUIPMENT_MAP.put("biking", new String[]{"STATIONARY_BIKE", "STATIONARY_BIKE", "STATIONARY_BIKE"});
        WORKOUT_MUSCLE_MAP.put("biking", new String[]{"LEGS", "LEGS", "LEGS"});
        WORKOUT_CATEGORY_MAP.put("biking", new String[]{"CARDIO", "CARDIO", "CARDIO"});

        WORKOUT_EXERCISE_MAP.put("upper", new String[]{"Push-Ups", "Tricep Dips", "Pull-Ups"});
        WORKOUT_INSTRUCTIONS_MAP.put("upper", new String[]{
                "Lower your chest to the floor, push up explosively. Keep your body in a straight line.",
                "Lower your body until your arms are at 90 degrees, push up. Use a chair or bench if needed.",
                "Pull your chin above the bar, lower with control. Use a band if you can't do full pull-ups."
        });
        WORKOUT_SEARCH_MAP.put("upper", new String[]{"pushups", "tricep+dips", "pull+ups"});
        WORKOUT_NUTRITION_MAP.put("upper", new String[]{"300", "20", "35"});
        WORKOUT_SUBCATEGORY_MAP.put("upper", new String[]{"UPPER_BODY", "UPPER_BODY", "UPPER_BODY"});
        WORKOUT_INTENSITY_MAP.put("upper", new String[]{"MEDIUM", "MEDIUM", "HIGH"});
        WORKOUT_EQUIPMENT_MAP.put("upper", new String[]{"BODYWEIGHT", "BODYWEIGHT", "PULL_UP_BAR"});
        WORKOUT_MUSCLE_MAP.put("upper", new String[]{"CHEST", "ARMS", "BACK"});
        WORKOUT_CATEGORY_MAP.put("upper", new String[]{"STRENGTH", "STRENGTH", "STRENGTH"});

        WORKOUT_EXERCISE_MAP.put("lower", new String[]{"Squats", "Lunges", "Calf Raises"});
        WORKOUT_INSTRUCTIONS_MAP.put("lower", new String[]{
                "Keep your chest up, lower your hips back and down. Go to at least parallel.",
                "Step forward, lower your hips until both knees are bent at 90 degrees. Alternate legs.",
                "Rise up on your toes, hold for 2 seconds, lower slowly. Repeat 20-25 times."
        });
        WORKOUT_SEARCH_MAP.put("lower", new String[]{"squats", "lunges", "calf+raises"});
        WORKOUT_NUTRITION_MAP.put("lower", new String[]{"280", "22", "30"});
        WORKOUT_SUBCATEGORY_MAP.put("lower", new String[]{"LOWER_BODY", "LOWER_BODY", "LOWER_BODY"});
        WORKOUT_INTENSITY_MAP.put("lower", new String[]{"MEDIUM", "MEDIUM", "LOW"});
        WORKOUT_EQUIPMENT_MAP.put("lower", new String[]{"BODYWEIGHT", "BODYWEIGHT", "BODYWEIGHT"});
        WORKOUT_MUSCLE_MAP.put("lower", new String[]{"LEGS", "LEGS", "LEGS"});
        WORKOUT_CATEGORY_MAP.put("lower", new String[]{"STRENGTH", "STRENGTH", "STRENGTH"});

        WORKOUT_EXERCISE_MAP.put("leg", new String[]{"Squats", "Lunges", "Calf Raises"});
        WORKOUT_INSTRUCTIONS_MAP.put("leg", new String[]{
                "Keep your chest up, lower your hips back and down. Go to at least parallel.",
                "Step forward, lower your hips until both knees are bent at 90 degrees. Alternate legs.",
                "Rise up on your toes, hold for 2 seconds, lower slowly. Repeat 20-25 times."
        });
        WORKOUT_SEARCH_MAP.put("leg", new String[]{"squats", "lunges", "calf+raises"});
        WORKOUT_NUTRITION_MAP.put("leg", new String[]{"280", "22", "30"});
        WORKOUT_SUBCATEGORY_MAP.put("leg", new String[]{"LOWER_BODY", "LOWER_BODY", "LOWER_BODY"});
        WORKOUT_INTENSITY_MAP.put("leg", new String[]{"MEDIUM", "MEDIUM", "LOW"});
        WORKOUT_EQUIPMENT_MAP.put("leg", new String[]{"BODYWEIGHT", "BODYWEIGHT", "BODYWEIGHT"});
        WORKOUT_MUSCLE_MAP.put("leg", new String[]{"LEGS", "LEGS", "LEGS"});
        WORKOUT_CATEGORY_MAP.put("leg", new String[]{"STRENGTH", "STRENGTH", "STRENGTH"});

        WORKOUT_EXERCISE_MAP.put("hiit", new String[]{"Burpees", "Mountain Climbers", "Jump Squats"});
        WORKOUT_INSTRUCTIONS_MAP.put("hiit", new String[]{
                "Drop to a squat, kick your feet back, do a push-up, jump up. That's one rep.",
                "In plank position, alternate driving your knees to your chest. Keep your hips low.",
                "Squat down, then explode up into a jump. Land softly and go right into the next rep."
        });
        WORKOUT_SEARCH_MAP.put("hiit", new String[]{"burpees", "mountain+climbers", "jump+squats"});
        WORKOUT_NUTRITION_MAP.put("hiit", new String[]{"400", "15", "40"});
        WORKOUT_SUBCATEGORY_MAP.put("hiit", new String[]{"FULL_BODY", "FULL_BODY", "FULL_BODY"});
        WORKOUT_INTENSITY_MAP.put("hiit", new String[]{"HIGH", "HIGH", "HIGH"});
        WORKOUT_EQUIPMENT_MAP.put("hiit", new String[]{"BODYWEIGHT", "BODYWEIGHT", "BODYWEIGHT"});
        WORKOUT_MUSCLE_MAP.put("hiit", new String[]{"FULL_BODY", "FULL_BODY", "FULL_BODY"});
        WORKOUT_CATEGORY_MAP.put("hiit", new String[]{"HIIT", "HIIT", "HIIT"});

        WORKOUT_EXERCISE_MAP.put("core", new String[]{"Planks", "Bicycle Crunches", "Leg Raises"});
        WORKOUT_INSTRUCTIONS_MAP.put("core", new String[]{
                "Hold a straight body line on your forearms. Keep your core tight and don't let your hips sag.",
                "Alternate bringing opposite elbow to knee. Move slowly and control the movement.",
                "Lie flat on your back, raise your legs to 90 degrees, lower with control."
        });
        WORKOUT_SEARCH_MAP.put("core", new String[]{"plank", "bicycle+crunches", "leg+raises"});
        WORKOUT_NUTRITION_MAP.put("core", new String[]{"200", "12", "30"});
        WORKOUT_SUBCATEGORY_MAP.put("core", new String[]{"CORE", "CORE", "CORE"});
        WORKOUT_INTENSITY_MAP.put("core", new String[]{"MEDIUM", "MEDIUM", "LOW"});
        WORKOUT_EQUIPMENT_MAP.put("core", new String[]{"BODYWEIGHT", "BODYWEIGHT", "BODYWEIGHT"});
        WORKOUT_MUSCLE_MAP.put("core", new String[]{"CORE", "CORE", "CORE"});
        WORKOUT_CATEGORY_MAP.put("core", new String[]{"CORE", "CORE", "CORE"});

        WORKOUT_EXERCISE_MAP.put("strength", new String[]{"Push-Ups", "Squats", "Planks"});
        WORKOUT_INSTRUCTIONS_MAP.put("strength", new String[]{
                "Lower your chest to the floor, push up explosively. Keep your body in a straight line.",
                "Keep your chest up, lower your hips back and down. Go to at least parallel.",
                "Hold a straight body line on your forearms. Keep your core tight and don't let your hips sag."
        });
        WORKOUT_SEARCH_MAP.put("strength", new String[]{"pushups", "squats", "plank"});
        WORKOUT_NUTRITION_MAP.put("strength", new String[]{"320", "25", "35"});
        WORKOUT_SUBCATEGORY_MAP.put("strength", new String[]{"FULL_BODY", "FULL_BODY", "FULL_BODY"});
        WORKOUT_INTENSITY_MAP.put("strength", new String[]{"MEDIUM", "MEDIUM", "MEDIUM"});
        WORKOUT_EQUIPMENT_MAP.put("strength", new String[]{"BODYWEIGHT", "BODYWEIGHT", "BODYWEIGHT"});
        WORKOUT_MUSCLE_MAP.put("strength", new String[]{"CHEST", "LEGS", "CORE"});
        WORKOUT_CATEGORY_MAP.put("strength", new String[]{"STRENGTH", "STRENGTH", "STRENGTH"});

        WORKOUT_EXERCISE_MAP.put("yoga", new String[]{"Downward Dog", "Warrior Pose", "Tree Pose"});
        WORKOUT_INSTRUCTIONS_MAP.put("yoga", new String[]{
                "Start on all fours, lift your hips up into an inverted V. Stretch your back and legs.",
                "Step into a lunge position, extend your arms. Keep your front knee at 90 degrees.",
                "Balance on one foot, place the other foot on your inner thigh."
        });
        WORKOUT_SEARCH_MAP.put("yoga", new String[]{"downward+dog", "warrior+pose", "tree+pose"});
        WORKOUT_NUTRITION_MAP.put("yoga", new String[]{"120", "4", "15"});
        WORKOUT_SUBCATEGORY_MAP.put("yoga", new String[]{"YOGA", "YOGA", "YOGA"});
        WORKOUT_INTENSITY_MAP.put("yoga", new String[]{"LOW", "LOW", "LOW"});
        WORKOUT_EQUIPMENT_MAP.put("yoga", new String[]{"BODYWEIGHT", "BODYWEIGHT", "BODYWEIGHT"});
        WORKOUT_MUSCLE_MAP.put("yoga", new String[]{"FULL_BODY", "LEGS", "CORE"});
        WORKOUT_CATEGORY_MAP.put("yoga", new String[]{"FLEXIBILITY", "FLEXIBILITY", "FLEXIBILITY"});

        WORKOUT_EXERCISE_MAP.put("flexibility", new String[]{"Downward Dog", "Warrior Pose", "Tree Pose"});
        WORKOUT_INSTRUCTIONS_MAP.put("flexibility", new String[]{
                "Start on all fours, lift your hips up into an inverted V. Stretch your back and legs.",
                "Step into a lunge position, extend your arms. Keep your front knee at 90 degrees.",
                "Balance on one foot, place the other foot on your inner thigh."
        });
        WORKOUT_SEARCH_MAP.put("flexibility", new String[]{"downward+dog", "warrior+pose", "tree+pose"});
        WORKOUT_NUTRITION_MAP.put("flexibility", new String[]{"120", "4", "15"});
        WORKOUT_SUBCATEGORY_MAP.put("flexibility", new String[]{"YOGA", "YOGA", "YOGA"});
        WORKOUT_INTENSITY_MAP.put("flexibility", new String[]{"LOW", "LOW", "LOW"});
        WORKOUT_EQUIPMENT_MAP.put("flexibility", new String[]{"BODYWEIGHT", "BODYWEIGHT", "BODYWEIGHT"});
        WORKOUT_MUSCLE_MAP.put("flexibility", new String[]{"FULL_BODY", "LEGS", "CORE"});
        WORKOUT_CATEGORY_MAP.put("flexibility", new String[]{"FLEXIBILITY", "FLEXIBILITY", "FLEXIBILITY"});

        GOAL_WORKOUT_TYPES.put(FitnessGoal.LOSE_WEIGHT,
                new String[]{"hiit", "running", "hiit", "biking", "hiit", "running", "strength"});
        GOAL_WORKOUT_TITLES.put(FitnessGoal.LOSE_WEIGHT, new String[]{
                "HIIT Cardio", "Cardio Running", "Full Body HIIT",
                "Cardio Biking", "HIIT Circuit", "Cardio Running", "Full Body Strength"
        });
        GOAL_WORKOUT_DESCS.put(FitnessGoal.LOSE_WEIGHT, new String[]{
                "High intensity interval training to maximize calorie burn and boost metabolism.",
                "Steady state cardio running to improve endurance and burn fat.",
                "Full body HIIT workout combining strength and cardio for maximum calorie expenditure.",
                "Stationary biking workout for lower body endurance and fat burning.",
                "Circuit-style HIIT workout with short rest periods for maximum calorie burn.",
                "Interval running workout alternating between sprints and recovery jogs.",
                "Full body strength training to build muscle and increase resting metabolism."
        });

        GOAL_WORKOUT_TYPES.put(FitnessGoal.MUSCLE_AND_STRENGTH_GAIN,
                new String[]{"upper", "lower", "strength", "upper", "lower", "strength", "hiit"});
        GOAL_WORKOUT_TITLES.put(FitnessGoal.MUSCLE_AND_STRENGTH_GAIN, new String[]{
                "Upper Body Strength", "Lower Body Strength", "Full Body Strength",
                "Upper Body Strength", "Lower Body Strength", "Full Body Strength", "HIIT Cardio"
        });
        GOAL_WORKOUT_DESCS.put(FitnessGoal.MUSCLE_AND_STRENGTH_GAIN, new String[]{
                "Upper body strength training focusing on chest, back, and arms for muscle growth.",
                "Lower body strength training focusing on legs and glutes for muscle development.",
                "Full body strength workout with compound exercises for overall muscle building.",
                "Upper body push/pull workout for muscle hypertrophy and strength gains.",
                "Lower body strength session with squats and lunges for leg development.",
                "Full body strength training with progressive overload principles.",
                "HIIT cardio for cardiovascular health and conditioning between strength days."
        });

        GOAL_WORKOUT_TYPES.put(FitnessGoal.INCREASE_ENDURANCE,
                new String[]{"running", "biking", "hiit", "running", "biking", "hiit", "running"});
        GOAL_WORKOUT_TITLES.put(FitnessGoal.INCREASE_ENDURANCE, new String[]{
                "Cardio Running", "Cardio Biking", "HIIT Training",
                "Cardio Running", "Cardio Biking", "HIIT Training", "Cardio Running"
        });
        GOAL_WORKOUT_DESCS.put(FitnessGoal.INCREASE_ENDURANCE, new String[]{
                "Long distance running to build cardiovascular endurance and stamina.",
                "Steady state biking for cardiovascular conditioning and leg endurance.",
                "HIIT intervals to improve speed, recovery, and overall conditioning.",
                "Tempo running workout to improve lactate threshold and endurance.",
                "Hill climb biking for leg strength and cardiovascular endurance.",
                "HIIT cardio for speed work and recovery capacity.",
                "Aerobic capacity run focusing on maintaining steady pace over distance."
        });

        GOAL_WORKOUT_TYPES.put(FitnessGoal.FLEXIBILITY_AND_MOBILITY,
                new String[]{"yoga", "flexibility", "yoga", "flexibility", "yoga", "flexibility", "yoga"});
        GOAL_WORKOUT_TITLES.put(FitnessGoal.FLEXIBILITY_AND_MOBILITY, new String[]{
                "Yoga Flow", "Stretching & Mobility", "Yoga Flow",
                "Stretching & Mobility", "Yoga Flow", "Stretching & Mobility", "Yoga Flow"
        });
        GOAL_WORKOUT_DESCS.put(FitnessGoal.FLEXIBILITY_AND_MOBILITY, new String[]{
                "Full body yoga flow to improve flexibility, balance, and relaxation.",
                "Static and dynamic stretching routine to improve range of motion.",
                "Yoga flow with focus on balance, core stability, and flexibility.",
                "Mobility work for joints, tendons, and overall range of motion.",
                "Yoga flow for flexibility, relaxation, and stress relief.",
                "Full body stretching routine for flexibility and recovery.",
                "Restorative yoga focusing on deep stretching and relaxation."
        });

        GOAL_WORKOUT_TYPES.put(FitnessGoal.MAINTAIN_GENERAL_FITNESS,
                new String[]{"strength", "running", "upper", "biking", "hiit", "lower", "strength"});
        GOAL_WORKOUT_TITLES.put(FitnessGoal.MAINTAIN_GENERAL_FITNESS, new String[]{
                "Full Body Strength", "Cardio Running", "Upper Body Strength",
                "Cardio Biking", "HIIT Training", "Lower Body Strength", "Full Body Strength"
        });
        GOAL_WORKOUT_DESCS.put(FitnessGoal.MAINTAIN_GENERAL_FITNESS, new String[]{
                "Balanced full body strength workout for overall fitness.",
                "Steady state cardio run for heart health and endurance.",
                "Upper body strength and conditioning for functional fitness.",
                "Biking workout for lower body endurance and cardiovascular health.",
                "HIIT workout for fitness, conditioning, and metabolism boost.",
                "Lower body strength and stability workout for functional fitness.",
                "Full body strength workout to maintain muscle and overall fitness."
        });
    }

    private final String apiKey;
    private final Random random = new Random();
    private int lastUsedDuration = DEFAULT_WORKOUT_DURATION;

    /**
     * Default constructor for AiWorkoutDataAccessObject.
     */
    public AiWorkoutDataAccessObject() {
        String key = loadKeyFromDotEnv();
        if (key == null || key.trim().isEmpty()) {
            key = System.getenv("GEMINI_API_KEY");
        }
        this.apiKey = key;
        LOGGER.log(Level.INFO, "AiWorkoutDataAccessObject initialized. API Key present: {0}",
                this.apiKey != null && !this.apiKey.isEmpty() && !"YOUR_API_KEY_HERE".equals(this.apiKey));
    }

    /**
     * Constructs an instance with a provided API key.
     *
     * @param apiKey API key for Gemini
     */
    public AiWorkoutDataAccessObject(final String apiKey) {
        this.apiKey = apiKey;
        LOGGER.log(Level.INFO, "AiWorkoutDataAccessObject initialized with key. Valid: {0}",
                this.apiKey != null && !this.apiKey.isEmpty() && !"YOUR_API_KEY_HERE".equals(this.apiKey));
    }

    private String loadKeyFromDotEnv() {
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
        catch (final Exception ex) {
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
            return getFallbackPlans(user, numberOfDays);
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
            catch (final Exception ex) {
                LOGGER.log(Level.WARNING, "API call failed or timed out: {0}", ex.getMessage());
            }
        }

        LOGGER.info("Using fallback plans");
        return getFallbackPlans(user, numberOfDays);
    }

    private List<WorkoutPlan> callApiWithTimeout(final User user, final int numberOfDays) throws Exception {
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
                + "  }]\n"
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
            LOGGER.log(Level.WARNING, "API returned error code: {0}", responseCode);
        }
        return null;
    }

    private String buildPrompt(final User user, final int numberOfDays) {
        final Set<DayOfWeek> preferredDays = user.getPreferredWorkoutDays() != null
                && !user.getPreferredWorkoutDays().isEmpty()
                ? user.getPreferredWorkoutDays()
                : Set.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY);

        final FitnessGoal goal = user.getGoal();
        String goalWorkoutTypes = "";
        String goalTitles = "";
        if (GOAL_WORKOUT_TYPES.containsKey(goal)) {
            final String[] types = GOAL_WORKOUT_TYPES.get(goal);
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < Math.min(numberOfDays, types.length); i++) {
                sb.append("Day ").append(i + 1).append(": ").append(types[i]).append("\n");
            }
            goalWorkoutTypes = sb.toString();
        }

        if (GOAL_WORKOUT_TITLES.containsKey(goal)) {
            final String[] titles = GOAL_WORKOUT_TITLES.get(goal);
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < Math.min(numberOfDays, titles.length); i++) {
                sb.append("- Day ").append(i + 1).append(": \"").append(titles[i]).append("\"\n");
            }
            goalTitles = sb.toString();
        }

        return "Generate " + numberOfDays + "-day workout plan. Goal: " + goal + ".\n"
                + "Workout days: " + preferredDays + ". Duration: " + this.lastUsedDuration + "min.\n"
                + "Equipment: " + (user.getEquipment().isEmpty() ? "Bodyweight" : user.getEquipment()) + ".\n\n"
                + "SUGGESTED STRUCTURE:\n" + goalWorkoutTypes + "\n"
                + "SUGGESTED TITLES:\n" + goalTitles + "\n\n"
                + "Return JSON array with: date, title, description, estimatedCaloriesBurned, "
                + "estimatedFatBurnedGrams, estimatedCarbsBurnedGrams, "
                + "exercises (name, sets, reps, durationMinutes, targetMuscleGroup, "
                + "equipmentRequired, instructions, videoUrl).";
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

        try {
            String rawJson = response;
            if (response.contains("\"text\": \"")) {
                final int textIdx = response.indexOf("\"text\": \"");
                final int start = textIdx + JSON_TEXT_OFFSET;
                final int end = response.lastIndexOf("\"");
                if (start < end) {
                    rawJson = response.substring(start, end)
                            .replace("\\n", " ")
                            .replace("\\\"", "\"")
                            .replace("\\\\", "\\");
                }
            }

            final int arrayStart = rawJson.indexOf("[");
            final int arrayEnd = rawJson.lastIndexOf("]");
            if (arrayStart == -1 || arrayEnd == -1) {
                LOGGER.warning("Could not find JSON array in response");
                return plans;
            }
            final String jsonArray = rawJson.substring(arrayStart, arrayEnd + 1);

            int searchPos = 0;
            int dayCounter = 0;

            while (dayCounter < numberOfDays) {
                final int dateIdx = jsonArray.indexOf("\"date\"", searchPos);
                if (dateIdx == -1) {
                    break;
                }

                final int objStart = jsonArray.lastIndexOf("{", dateIdx);
                if (objStart == -1) {
                    break;
                }

                final int objEnd = jsonArray.indexOf("}", objStart);
                if (objEnd == -1) {
                    break;
                }

                final String block = jsonArray.substring(objStart, objEnd + 1);
                searchPos = objEnd + 1;

                final LocalDate currentDate = today.plusDays(dayCounter);
                String date = extractVal(block, "date");
                if (date.isEmpty() || date.matches("\\d+")) {
                    date = currentDate.format(fmt);
                }

                String title = extractVal(block, "title");
                if (title.isEmpty()) {
                    title = "Workout " + (dayCounter + 1);
                }

                final String dayOfWeek = currentDate.getDayOfWeek().toString();
                if (title.contains(dayOfWeek.substring(0, 3))) {
                    title = title.replaceAll("(?i)" + dayOfWeek, "").trim();
                    if (title.isEmpty()) {
                        title = "Workout " + (dayCounter + 1);
                    }
                }

                final DayOfWeek dow = currentDate.getDayOfWeek();
                final boolean isRestDay = !preferredDays.contains(dow);

                if (isRestDay) {
                    plans.add(new WorkoutPlan(date, "Rest & Recovery",
                            "Rest day. Focus on recovery, hydration, and light stretching.",
                            "REST", "REST", "LOW", "NONE", "BODYWEIGHT",
                            0, 0, 0, 0, new ArrayList<>()));
                }
                else {
                    String desc = extractVal(block, "description");
                    if (desc.isEmpty()) {
                        desc = title + " workout session.";
                    }

                    final int calories = extractInt(block, "estimatedCaloriesBurned", DEFAULT_CALORIES);
                    final int fat = extractInt(block, "estimatedFatBurnedGrams", DEFAULT_FAT);
                    final int carbs = extractInt(block, "estimatedCarbsBurnedGrams", DEFAULT_CARBS);

                    List<Exercise> exercises = getExercisesForWorkoutTitle(title, user);

                    final int exSearchPos = block.indexOf("exercises");
                    if (exSearchPos != -1) {
                        final int exArrayStart = block.indexOf("[", exSearchPos);
                        if (exArrayStart != -1) {
                            final int exArrayEnd = block.lastIndexOf("]");
                            if (exArrayEnd != -1 && exArrayEnd > exArrayStart) {
                                final String exArray = block.substring(exArrayStart + 1, exArrayEnd);
                                final String[] exBlocks = exArray.split("(?<=\\}),\\s*");
                                final List<Exercise> apiExercises = new ArrayList<>();

                                for (final String exBlock : exBlocks) {
                                    if (!exBlock.contains("name")) {
                                        continue;
                                    }
                                    final String name = extractVal(exBlock, "name");
                                    if (name.isEmpty()) {
                                        continue;
                                    }
                                    final int sets = extractInt(exBlock, "sets", DEFAULT_SETS);
                                    final int reps = extractInt(exBlock, "reps", DEFAULT_REPS);
                                    final int exDuration = extractInt(exBlock, "durationMinutes", DEFAULT_EX_DURATION);
                                    final String targetMuscle = extractVal(exBlock, "targetMuscleGroup");
                                    final String equipReq = extractVal(exBlock, "equipmentRequired");
                                    String inst = extractVal(exBlock, "instructions");
                                    String vid = extractVal(exBlock, "videoUrl");

                                    final String category = determineCategory(name);
                                    final String subCategory = determineSubCategory(name);
                                    final String intensity = determineIntensity(name);
                                    final String equipmentType = determineEquipment(name);

                                    if (inst.isEmpty()) {
                                        inst = getInstructionForExercise(name);
                                    }
                                    if (vid.isEmpty()) {
                                        vid = "https://www.youtube.com/results?search_query="
                                                + name.replace(" ", "+") + "+exercise+tutorial";
                                    }

                                    apiExercises.add(new Exercise(name, sets, reps, exDuration,
                                            targetMuscle.isEmpty() ? "Various" : targetMuscle,
                                            equipReq.isEmpty() ? "Bodyweight" : equipReq,
                                            inst, vid, category, subCategory, intensity, equipmentType));
                                }

                                if (!apiExercises.isEmpty()) {
                                    exercises = apiExercises;
                                }
                            }
                        }
                    }

                    while (exercises.size() < MAX_EXERCISES_PER_WORKOUT) {
                        final String fallbackName = "Bodyweight " + (exercises.size() + 1);
                        exercises.add(createExercise(fallbackName, title));
                    }

                    plans.add(new WorkoutPlan(date, title, desc,
                            "GENERAL", "GENERAL", "MEDIUM", "Various", "BODYWEIGHT",
                            this.lastUsedDuration, calories, fat, carbs, exercises));
                }
                dayCounter++;
            }

            LOGGER.log(Level.INFO, "Parsed {0} workout plans", plans.size());
        }
        catch (final Exception ex) {
            LOGGER.log(Level.SEVERE, "Error parsing JSON: {0}", ex.getMessage());
        }

        if (plans.size() < numberOfDays) {
            LOGGER.info("Not enough plans from API, using fallback");
            return getFallbackPlans(user, numberOfDays);
        }

        return plans;
    }

    private List<Exercise> getExercisesForWorkoutTitle(final String title, final User user) {
        final List<Exercise> exercises = new ArrayList<>();
        final String titleLower = title.toLowerCase();

        final String workoutType = determineWorkoutType(titleLower);

        if (WORKOUT_EXERCISE_MAP.containsKey(workoutType)) {
            final String[] exerciseNames = WORKOUT_EXERCISE_MAP.get(workoutType);
            final String[] instructions = WORKOUT_INSTRUCTIONS_MAP.get(workoutType);
            final String[] searchQueries = WORKOUT_SEARCH_MAP.get(workoutType);
            final String[] categories = WORKOUT_CATEGORY_MAP.get(workoutType);
            final String[] subCategories = WORKOUT_SUBCATEGORY_MAP.get(workoutType);
            final String[] intensities = WORKOUT_INTENSITY_MAP.get(workoutType);
            final String[] equipmentTypes = WORKOUT_EQUIPMENT_MAP.get(workoutType);

            for (int i = 0; i < Math.min(MAX_EXERCISES_PER_WORKOUT, exerciseNames.length); i++) {
                final String name = exerciseNames[i];
                final String inst = (instructions != null && i < instructions.length) ? instructions[i]
                        : "Perform " + name + " with proper form.";
                final String videoUrl = "https://www.youtube.com/results?search_query="
                        + (searchQueries != null && i < searchQueries.length
                        ? searchQueries[i] : name.replace(" ", "+")) + "+exercise+tutorial";

                String equipmentType = (equipmentTypes != null && i < equipmentTypes.length)
                        ? equipmentTypes[i] : "BODYWEIGHT";

                if (!userHasEquipment(user, equipmentType)) {
                    equipmentType = "BODYWEIGHT";
                }

                final String category = (categories != null && i < categories.length) ? categories[i] : "GENERAL";
                final String subCategory = (subCategories != null && i < subCategories.length)
                        ? subCategories[i] : "GENERAL";
                final String intensity = (intensities != null && i < intensities.length) ? intensities[i] : "MEDIUM";

                exercises.add(new Exercise(name, DEFAULT_SETS, DEFAULT_REPS,
                        DEFAULT_EX_DURATION, "Various", equipmentType,
                        inst, videoUrl, category, subCategory, intensity, equipmentType));
            }
        }

        if (exercises.isEmpty()) {
            exercises.add(createExercise("Push-Ups", title));
            exercises.add(createExercise("Squats", title));
            exercises.add(createExercise("Planks", title));
        }

        return exercises;
    }

    private boolean userHasEquipment(final User user, final String equipmentType) {
        if (equipmentType == null || "BODYWEIGHT".equals(equipmentType)) {
            return true;
        }
        if (user.getEquipment() == null || user.getEquipment().isEmpty()) {
            return false;
        }
        for (final Equipment eq : user.getEquipment()) {
            if (eq.name().equals(equipmentType) || eq.name().contains(equipmentType)) {
                return true;
            }
        }
        return false;
    }

    private String determineWorkoutType(final String title) {
        final String result;
        if (title.contains("yoga") || title.contains("stretch") || title.contains("flex")) {
            result = "yoga";
        }
        else if (title.contains("hiit") || title.contains("interval")) {
            result = "hiit";
        }
        else if (title.contains("cardio") || title.contains("run") || title.contains("running")) {
            result = "running";
        }
        else if (title.contains("bike") || title.contains("biking") || title.contains("cycle")) {
            result = "biking";
        }
        else if (title.contains("core") || title.contains("abs")) {
            result = "core";
        }
        else if (title.contains("upper") || title.contains("push") || title.contains("pull")) {
            result = "upper";
        }
        else if (title.contains("lower") || title.contains("leg") || title.contains("squat")) {
            result = "lower";
        }
        else {
            result = "strength";
        }
        return result;
    }

    private String determineCategory(final String exerciseName) {
        final String lower = exerciseName.toLowerCase();
        final String result;
        if (lower.contains("run") || lower.contains("sprint") || lower.contains("jog")
                || lower.contains("bike") || lower.contains("cycle") || lower.contains("pedal")) {
            result = "CARDIO";
        }
        else if (lower.contains("push") || lower.contains("pull") || lower.contains("press")
                || lower.contains("squat") || lower.contains("lunge") || lower.contains("deadlift")) {
            result = "STRENGTH";
        }
        else if (lower.contains("burpee") || lower.contains("mountain") || lower.contains("jump")) {
            result = "HIIT";
        }
        else if (lower.contains("plank") || lower.contains("crunch") || lower.contains("raise")) {
            result = "CORE";
        }
        else if (lower.contains("yoga") || lower.contains("downward") || lower.contains("warrior")
                || lower.contains("tree") || lower.contains("stretch")) {
            result = "FLEXIBILITY";
        }
        else {
            result = "GENERAL";
        }
        return result;
    }

    private String determineSubCategory(final String exerciseName) {
        final String lower = exerciseName.toLowerCase();
        final String result;
        if (lower.contains("run") || lower.contains("sprint") || lower.contains("jog")) {
            result = "RUNNING";
        }
        else if (lower.contains("bike") || lower.contains("cycle") || lower.contains("pedal")) {
            result = "BIKING";
        }
        else if (lower.contains("push") || lower.contains("pull") || lower.contains("press")) {
            result = "UPPER_BODY";
        }
        else if (lower.contains("squat") || lower.contains("lunge") || lower.contains("deadlift")
                || lower.contains("calf")) {
            result = "LOWER_BODY";
        }
        else if (lower.contains("burpee") || lower.contains("mountain") || lower.contains("jump")) {
            result = "FULL_BODY";
        }
        else if (lower.contains("plank") || lower.contains("crunch") || lower.contains("raise")) {
            result = "CORE";
        }
        else if (lower.contains("yoga") || lower.contains("downward") || lower.contains("warrior")
                || lower.contains("tree")) {
            result = "YOGA";
        }
        else {
            result = "GENERAL";
        }
        return result;
    }

    private String determineIntensity(final String exerciseName) {
        final String lower = exerciseName.toLowerCase();
        final String result;
        if (lower.contains("sprint") || lower.contains("burpee") || lower.contains("hiit")) {
            result = "HIGH";
        }
        else if (lower.contains("yoga") || lower.contains("stretch") || lower.contains("plank")) {
            result = "LOW";
        }
        else {
            result = "MEDIUM";
        }
        return result;
    }

    private String determineEquipment(final String exerciseName) {
        final String lower = exerciseName.toLowerCase();
        final String result;
        if (lower.contains("bike") || lower.contains("cycle")) {
            result = "STATIONARY_BIKE";
        }
        else if (lower.contains("pull") || lower.contains("chin")) {
            result = "PULL_UP_BAR";
        }
        else if (lower.contains("press") && lower.contains("dumb")) {
            result = "DUMBBELLS";
        }
        else if (lower.contains("deadlift") || lower.contains("barbell")) {
            result = "BARBELL";
        }
        else {
            result = "BODYWEIGHT";
        }
        return result;
    }

    private String getInstructionForExercise(final String name) {
        final String lower = name.toLowerCase();
        final String result;
        if (lower.contains("push")) {
            result = "Lower your chest to the floor, push up explosively. Keep your body in a straight line.";
        }
        else if (lower.contains("squat")) {
            result = "Keep your chest up, lower your hips back and down. Go to at least parallel.";
        }
        else if (lower.contains("plank")) {
            result = "Hold a straight body line on your forearms. Keep your core tight.";
        }
        else if (lower.contains("lunge")) {
            result = "Step forward, lower your hips until both knees are bent at 90 degrees.";
        }
        else if (lower.contains("run") || lower.contains("jog")) {
            result = "Jog or run at a steady pace with proper form. Keep your back straight.";
        }
        else if (lower.contains("bike") || lower.contains("cycle")) {
            result = "Pedal at moderate to high intensity. Maintain a steady cadence.";
        }
        else if (lower.contains("burpee")) {
            result = "Drop to squat, kick feet back, do push-up, jump up. That's one rep.";
        }
        else if (lower.contains("yoga") || lower.contains("downward") || lower.contains("warrior")) {
            result = "Perform the yoga pose with proper form and controlled breathing.";
        }
        else {
            result = "Perform " + name + " with proper form. Maintain controlled movement throughout.";
        }
        return result;
    }

    private Exercise createExercise(final String name, final String title) {
        final String category = determineCategory(name);
        final String subCategory = determineSubCategory(name);
        final String intensity = determineIntensity(name);
        final String equipment = determineEquipment(name);
        final String instruction = getInstructionForExercise(name);
        final String videoUrl = "https://www.youtube.com/results?search_query="
                + name.replace(" ", "+") + "+exercise+tutorial";

        return new Exercise(name, DEFAULT_SETS, DEFAULT_REPS,
                DEFAULT_EX_DURATION, "Various", equipment,
                instruction, videoUrl, category, subCategory, intensity, equipment);
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

    private List<WorkoutPlan> getFallbackPlans(final User user, final int numberOfDays) {
        LOGGER.info("Generating fallback plans");
        final List<WorkoutPlan> plans = new ArrayList<>();
        final LocalDate today = LocalDate.now();
        final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("EEEE, MMM d");

        Set<DayOfWeek> preferredDays = Set.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY);
        if (user != null && user.getPreferredWorkoutDays() != null && !user.getPreferredWorkoutDays().isEmpty()) {
            preferredDays = user.getPreferredWorkoutDays();
        }

        int targetDuration = this.lastUsedDuration;
        if (user != null && user.getPreferredWorkoutDurationMinutes() > 0) {
            targetDuration = user.getPreferredWorkoutDurationMinutes();
            this.lastUsedDuration = targetDuration;
        }

        final FitnessGoal goal = user != null ? user.getGoal() : FitnessGoal.MAINTAIN_GENERAL_FITNESS;
        final String[] workoutTypes = GOAL_WORKOUT_TYPES.getOrDefault(goal,
                new String[]{"strength", "running", "strength", "biking", "hiit", "running", "strength"});
        final String[] workoutTitles = GOAL_WORKOUT_TITLES.getOrDefault(goal,
                new String[]{"Full Body Strength", "Cardio Running", "Upper Body Strength",
                        "Cardio Biking", "HIIT Training", "Cardio Running", "Lower Body Strength"});
        final String[] workoutDescs = GOAL_WORKOUT_DESCS.getOrDefault(goal,
                new String[]{"Balanced full body strength workout.",
                        "Steady state cardio run for heart health.",
                        "Upper body strength and conditioning.",
                        "Biking workout for lower body endurance.",
                        "HIIT workout for fitness and conditioning.",
                        "Cardio running session for endurance.",
                        "Lower body strength and stability workout."});

        final int[][] nutritionValues = {
                {350, 15, 40}, {320, 12, 45}, {350, 15, 40},
                {320, 12, 45}, {380, 14, 38}, {320, 12, 45}, {330, 18, 35}
        };

        int workoutCounter = 0;
        for (int dayOffset = 0; dayOffset < numberOfDays; dayOffset++) {
            final LocalDate date = today.plusDays(dayOffset);
            final String dateLabel = date.format(fmt);
            final DayOfWeek dow = date.getDayOfWeek();

            if (preferredDays.contains(dow)) {
                final int typeIdx = workoutCounter % workoutTypes.length;
                workoutCounter++;
                final String type = workoutTypes[typeIdx];

                final List<Exercise> exercises = getExercisesForWorkoutType(type, user);

                final String title = workoutTitles[typeIdx];
                final String desc = workoutDescs[typeIdx];
                final int[] nutrition = nutritionValues[typeIdx % nutritionValues.length];

                plans.add(new WorkoutPlan(dateLabel, title, desc,
                        "GENERAL", "GENERAL", "MEDIUM", "Various", "BODYWEIGHT",
                        targetDuration, nutrition[0], nutrition[1], nutrition[2], exercises));
            }
            else {
                plans.add(new WorkoutPlan(dateLabel, "Rest & Recovery",
                        "Rest day. Focus on recovery, hydration, and light stretching.",
                        "REST", "REST", "LOW", "NONE", "BODYWEIGHT",
                        0, 0, 0, 0, new ArrayList<>()));
            }
        }

        LOGGER.log(Level.INFO, "Generated {0} fallback plans", plans.size());
        return plans;
    }

    private List<Exercise> getExercisesForWorkoutType(final String type, final User user) {
        final List<Exercise> exercises = new ArrayList<>();

        if (WORKOUT_EXERCISE_MAP.containsKey(type)) {
            final String[] exerciseNames = WORKOUT_EXERCISE_MAP.get(type);
            final String[] instructions = WORKOUT_INSTRUCTIONS_MAP.get(type);
            final String[] searchQueries = WORKOUT_SEARCH_MAP.get(type);
            final String[] categories = WORKOUT_CATEGORY_MAP.get(type);
            final String[] subCategories = WORKOUT_SUBCATEGORY_MAP.get(type);
            final String[] intensities = WORKOUT_INTENSITY_MAP.get(type);
            final String[] equipmentTypes = WORKOUT_EQUIPMENT_MAP.get(type);

            for (int i = 0; i < Math.min(MAX_EXERCISES_PER_WORKOUT, exerciseNames.length); i++) {
                final String name = exerciseNames[i];
                final String inst = (instructions != null && i < instructions.length) ? instructions[i]
                        : "Perform " + name + " with proper form.";
                final String videoUrl = "https://www.youtube.com/results?search_query="
                        + (searchQueries != null && i < searchQueries.length
                        ? searchQueries[i] : name.replace(" ", "+")) + "+exercise+tutorial";

                String equipmentType = (equipmentTypes != null && i < equipmentTypes.length)
                        ? equipmentTypes[i] : "BODYWEIGHT";

                if (!userHasEquipment(user, equipmentType)) {
                    equipmentType = "BODYWEIGHT";
                }

                final String category = (categories != null && i < categories.length) ? categories[i] : "GENERAL";
                final String subCategory = (subCategories != null && i < subCategories.length)
                        ? subCategories[i] : "GENERAL";
                final String intensity = (intensities != null && i < intensities.length) ? intensities[i] : "MEDIUM";

                exercises.add(new Exercise(name, DEFAULT_SETS, DEFAULT_REPS,
                        DEFAULT_EX_DURATION, "Various", equipmentType,
                        inst, videoUrl, category, subCategory, intensity, equipmentType));
            }
        }

        if (exercises.isEmpty()) {
            exercises.add(createExercise("Push-Ups", type));
            exercises.add(createExercise("Squats", type));
            exercises.add(createExercise("Planks", type));
        }

        return exercises;
    }
}