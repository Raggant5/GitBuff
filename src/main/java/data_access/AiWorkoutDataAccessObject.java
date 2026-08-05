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

public class AiWorkoutDataAccessObject implements AiWorkoutDataAccessInterface {

    private static final Logger LOGGER = Logger.getLogger(AiWorkoutDataAccessObject.class.getName());

    private static final int HTTP_OK = 200;
    private static final int TIMEOUT_MILLIS = 15000;
    private static final int SEED_MAX = 100000;
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

    private static final String API_VERSION = "v1";
    private static final String GEMINI_MODEL = "gemini-3.5-flash-lite";

    // Map workout type to exercises, instructions, and search queries
    private static final Map<String, String[]> WORKOUT_EXERCISE_MAP = new HashMap<>();
    private static final Map<String, String[]> WORKOUT_INSTRUCTIONS_MAP = new HashMap<>();
    private static final Map<String, String[]> WORKOUT_SEARCH_MAP = new HashMap<>();
    private static final Map<String, String[]> WORKOUT_NUTRITION_MAP = new HashMap<>();
    private static final Map<String, String[]> WORKOUT_SUBCATEGORY_MAP = new HashMap<>();
    private static final Map<String, String[]> WORKOUT_INTENSITY_MAP = new HashMap<>();
    private static final Map<String, String[]> WORKOUT_EQUIPMENT_MAP = new HashMap<>();
    private static final Map<String, String[]> WORKOUT_MUSCLE_MAP = new HashMap<>();
    private static final Map<String, String[]> WORKOUT_CATEGORY_MAP = new HashMap<>();

    // Map FitnessGoal to workout type distribution
    private static final Map<FitnessGoal, String[]> GOAL_WORKOUT_TYPES = new HashMap<>();
    private static final Map<FitnessGoal, String[]> GOAL_WORKOUT_TITLES = new HashMap<>();
    private static final Map<FitnessGoal, String[]> GOAL_WORKOUT_DESCS = new HashMap<>();

    static {
        // ====== CARDIO - RUNNING ======
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

        // ====== CARDIO - BIKING ======
        WORKOUT_EXERCISE_MAP.put("biking", new String[]{"Stationary Bike", "Hill Climbs", "Sprint Intervals"});
        WORKOUT_INSTRUCTIONS_MAP.put("biking", new String[]{
                "Pedal at moderate to high intensity. Maintain a steady cadence of 80-100 RPM.",
                "Increase resistance and pedal standing up. Simulate climbing a steep hill.",
                "Sprint at max effort for 30 seconds, then pedal easy for 60 seconds. Repeat 10 times."
        });
        WORKOUT_SEARCH_MAP.put("biking", new String[]{"stationary+bike+workout", "hill+climbs+bike", "bike+sprint+intervals"});
        WORKOUT_NUTRITION_MAP.put("biking", new String[]{"340", "10", "50"});
        WORKOUT_SUBCATEGORY_MAP.put("biking", new String[]{"BIKING", "BIKING", "BIKING"});
        WORKOUT_INTENSITY_MAP.put("biking", new String[]{"MEDIUM", "HIGH", "HIGH"});
        WORKOUT_EQUIPMENT_MAP.put("biking", new String[]{"STATIONARY_BIKE", "STATIONARY_BIKE", "STATIONARY_BIKE"});
        WORKOUT_MUSCLE_MAP.put("biking", new String[]{"LEGS", "LEGS", "LEGS"});
        WORKOUT_CATEGORY_MAP.put("biking", new String[]{"CARDIO", "CARDIO", "CARDIO"});

        // ====== STRENGTH - UPPER BODY ======
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

        // ====== STRENGTH - LOWER BODY ======
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

        // ====== STRENGTH - LEG ======
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

        // ====== HIIT ======
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

        // ====== CORE ======
        WORKOUT_EXERCISE_MAP.put("core", new String[]{"Planks", "Bicycle Crunches", "Leg Raises"});
        WORKOUT_INSTRUCTIONS_MAP.put("core", new String[]{
                "Hold a straight body line on your forearms. Keep your core tight and don't let your hips sag.",
                "Alternate bringing opposite elbow to knee. Move slowly and control the movement.",
                "Lie flat on your back, raise your legs to 90 degrees, lower with control. Keep your lower back on the floor."
        });
        WORKOUT_SEARCH_MAP.put("core", new String[]{"plank", "bicycle+crunches", "leg+raises"});
        WORKOUT_NUTRITION_MAP.put("core", new String[]{"200", "12", "30"});
        WORKOUT_SUBCATEGORY_MAP.put("core", new String[]{"CORE", "CORE", "CORE"});
        WORKOUT_INTENSITY_MAP.put("core", new String[]{"MEDIUM", "MEDIUM", "LOW"});
        WORKOUT_EQUIPMENT_MAP.put("core", new String[]{"BODYWEIGHT", "BODYWEIGHT", "BODYWEIGHT"});
        WORKOUT_MUSCLE_MAP.put("core", new String[]{"CORE", "CORE", "CORE"});
        WORKOUT_CATEGORY_MAP.put("core", new String[]{"CORE", "CORE", "CORE"});

        // ====== STRENGTH - GENERAL ======
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

        // ====== YOGA / FLEXIBILITY ======
        WORKOUT_EXERCISE_MAP.put("yoga", new String[]{"Downward Dog", "Warrior Pose", "Tree Pose"});
        WORKOUT_INSTRUCTIONS_MAP.put("yoga", new String[]{
                "Start on all fours, lift your hips up into an inverted V. Stretch your back and legs.",
                "Step into a lunge position, extend your arms. Keep your front knee at 90 degrees.",
                "Balance on one foot, place the other foot on your inner thigh. Focus on a point to maintain balance."
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
                "Balance on one foot, place the other foot on your inner thigh. Focus on a point to maintain balance."
        });
        WORKOUT_SEARCH_MAP.put("flexibility", new String[]{"downward+dog", "warrior+pose", "tree+pose"});
        WORKOUT_NUTRITION_MAP.put("flexibility", new String[]{"120", "4", "15"});
        WORKOUT_SUBCATEGORY_MAP.put("flexibility", new String[]{"YOGA", "YOGA", "YOGA"});
        WORKOUT_INTENSITY_MAP.put("flexibility", new String[]{"LOW", "LOW", "LOW"});
        WORKOUT_EQUIPMENT_MAP.put("flexibility", new String[]{"BODYWEIGHT", "BODYWEIGHT", "BODYWEIGHT"});
        WORKOUT_MUSCLE_MAP.put("flexibility", new String[]{"FULL_BODY", "LEGS", "CORE"});
        WORKOUT_CATEGORY_MAP.put("flexibility", new String[]{"FLEXIBILITY", "FLEXIBILITY", "FLEXIBILITY"});

        // ====== GOAL-BASED WORKOUT MAPPINGS ======
        GOAL_WORKOUT_TYPES.put(FitnessGoal.LOSE_WEIGHT, new String[]{"hiit", "running", "hiit", "biking", "hiit", "running", "strength"});
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

        GOAL_WORKOUT_TYPES.put(FitnessGoal.MUSCLE_AND_STRENGTH_GAIN, new String[]{"upper", "lower", "strength", "upper", "lower", "strength", "hiit"});
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

        GOAL_WORKOUT_TYPES.put(FitnessGoal.INCREASE_ENDURANCE, new String[]{"running", "biking", "hiit", "running", "biking", "hiit", "running"});
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

        GOAL_WORKOUT_TYPES.put(FitnessGoal.FLEXIBILITY_AND_MOBILITY, new String[]{"yoga", "flexibility", "yoga", "flexibility", "yoga", "flexibility", "yoga"});
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

        // Default for MAINTAIN_GENERAL_FITNESS
        GOAL_WORKOUT_TYPES.put(FitnessGoal.MAINTAIN_GENERAL_FITNESS, new String[]{"strength", "running", "upper", "biking", "hiit", "lower", "strength"});
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

    public AiWorkoutDataAccessObject() {
        String key = loadKeyFromDotEnv();
        if (key == null || key.trim().isEmpty()) {
            key = System.getenv("GEMINI_API_KEY");
        }
        this.apiKey = key;
        LOGGER.log(Level.INFO, "AiWorkoutDataAccessObject initialized. API Key present: {0}",
                this.apiKey != null && !this.apiKey.isEmpty() && !"YOUR_API_KEY_HERE".equals(this.apiKey));
        LOGGER.log(Level.INFO, "Using Gemini model: {0} with API version {1}", new Object[]{GEMINI_MODEL, API_VERSION});
    }

    public AiWorkoutDataAccessObject(final String apiKey) {
        this.apiKey = apiKey;
        LOGGER.log(Level.INFO, "AiWorkoutDataAccessObject initialized with provided API Key. Valid: {0}",
                this.apiKey != null && !this.apiKey.isEmpty() && !"YOUR_API_KEY_HERE".equals(this.apiKey));
        LOGGER.log(Level.INFO, "Using Gemini model: {0} with API version {1}", new Object[]{GEMINI_MODEL, API_VERSION});
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

        // Store user's preferred duration
        if (user.getPreferredWorkoutDurationMinutes() > 0) {
            this.lastUsedDuration = user.getPreferredWorkoutDurationMinutes();
        }

        // Try API call with shorter timeout
        if (this.apiKey != null && !this.apiKey.trim().isEmpty() && !"YOUR_API_KEY_HERE".equals(this.apiKey)) {
            try {
                List<WorkoutPlan> plans = callApiWithTimeout(user, numberOfDays);
                if (plans != null && !plans.isEmpty()) {
                    return plans;
                }
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "API call failed or timed out: {0}", e.getMessage());
            }
        }

        LOGGER.info("Using fallback plans");
        return getFallbackPlans(user, numberOfDays);
    }

    private List<WorkoutPlan> callApiWithTimeout(User user, int numberOfDays) throws Exception {
        // Use a shorter timeout for faster response
        final String endpoint = "https://generativelanguage.googleapis.com/" + API_VERSION + "/models/"
                + GEMINI_MODEL + ":generateContent?key=" + this.apiKey;

        final URL url = new URL(endpoint);
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(15000);
        connection.setDoOutput(true);

        // Build a shorter, more focused prompt
        String promptText = buildPrompt(user, numberOfDays);

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
        } else {
            LOGGER.log(Level.WARNING, "API returned error code: {0}", responseCode);
        }
        return null;
    }

    private String buildPrompt(User user, int numberOfDays) {
        final LocalDate today = LocalDate.now();
        final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("EEEE, MMM d");
        final String startDateStr = today.format(fmt);

        final Set<DayOfWeek> preferredDays = user.getPreferredWorkoutDays() != null
                && !user.getPreferredWorkoutDays().isEmpty()
                ? user.getPreferredWorkoutDays()
                : Set.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY);

        // Get goal-based workout suggestions
        FitnessGoal goal = user.getGoal();
        String goalWorkoutTypes = "";
        String goalTitles = "";
        if (GOAL_WORKOUT_TYPES.containsKey(goal)) {
            String[] types = GOAL_WORKOUT_TYPES.get(goal);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < Math.min(numberOfDays, types.length); i++) {
                sb.append("Day ").append(i + 1).append(": ").append(types[i]).append("\n");
            }
            goalWorkoutTypes = sb.toString();
        }

        if (GOAL_WORKOUT_TITLES.containsKey(goal)) {
            String[] titles = GOAL_WORKOUT_TITLES.get(goal);
            StringBuilder sb = new StringBuilder();
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

    private List<WorkoutPlan> parseGeminiJsonResponse(final String response, User user, int numberOfDays) {
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
                } else {
                    String desc = extractVal(block, "description");
                    if (desc.isEmpty()) {
                        desc = title + " workout session.";
                    }

                    final int calories = extractInt(block, "estimatedCaloriesBurned", DEFAULT_CALORIES);
                    final int fat = extractInt(block, "estimatedFatBurnedGrams", DEFAULT_FAT);
                    final int carbs = extractInt(block, "estimatedCarbsBurnedGrams", DEFAULT_CARBS);

                    // Get exercises based on the title
                    List<Exercise> exercises = getExercisesForWorkoutTitle(title, user);

                    // If we got exercises from the API, use those instead
                    final int exSearchPos = block.indexOf("exercises");
                    if (exSearchPos != -1) {
                        final int exArrayStart = block.indexOf("[", exSearchPos);
                        if (exArrayStart != -1) {
                            final int exArrayEnd = block.lastIndexOf("]");
                            if (exArrayEnd != -1 && exArrayEnd > exArrayStart) {
                                final String exArray = block.substring(exArrayStart + 1, exArrayEnd);
                                final String[] exBlocks = exArray.split("(?<=\\}),\\s*");
                                List<Exercise> apiExercises = new ArrayList<>();

                                for (String exBlock : exBlocks) {
                                    if (!exBlock.contains("name")) {
                                        continue;
                                    }
                                    final String name = extractVal(exBlock, "name");
                                    if (name.isEmpty()) {
                                        continue;
                                    }
                                    final int sets = extractInt(exBlock, "sets", DEFAULT_SETS);
                                    final int reps = extractInt(exBlock, "reps", DEFAULT_REPS);
                                    final int exDuration = extractInt(exBlock, "durationMinutes",
                                            DEFAULT_EX_DURATION);
                                    final String targetMuscle = extractVal(exBlock, "targetMuscleGroup");
                                    final String equipReq = extractVal(exBlock, "equipmentRequired");
                                    String inst = extractVal(exBlock, "instructions");
                                    String vid = extractVal(exBlock, "videoUrl");

                                    // Use mapped values if API didn't provide them
                                    String category = determineCategory(name);
                                    String subCategory = determineSubCategory(name);
                                    String intensity = determineIntensity(name);
                                    String equipmentType = determineEquipment(name);

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

                    // Ensure we have exactly 3 exercises
                    while (exercises.size() < MAX_EXERCISES_PER_WORKOUT) {
                        String fallbackName = "Bodyweight " + (exercises.size() + 1);
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
            ex.printStackTrace();
        }

        // If we didn't get enough plans, use fallback
        if (plans.size() < numberOfDays) {
            LOGGER.info("Not enough plans from API, using fallback");
            return getFallbackPlans(user, numberOfDays);
        }

        return plans;
    }

    private List<Exercise> getExercisesForWorkoutTitle(String title, User user) {
        List<Exercise> exercises = new ArrayList<>();
        String titleLower = title.toLowerCase();

        // Determine workout type from title
        String workoutType = determineWorkoutType(titleLower);

        // Get exercises for this type
        if (WORKOUT_EXERCISE_MAP.containsKey(workoutType)) {
            String[] exerciseNames = WORKOUT_EXERCISE_MAP.get(workoutType);
            String[] instructions = WORKOUT_INSTRUCTIONS_MAP.get(workoutType);
            String[] searchQueries = WORKOUT_SEARCH_MAP.get(workoutType);
            String[] categories = WORKOUT_CATEGORY_MAP.get(workoutType);
            String[] subCategories = WORKOUT_SUBCATEGORY_MAP.get(workoutType);
            String[] intensities = WORKOUT_INTENSITY_MAP.get(workoutType);
            String[] equipmentTypes = WORKOUT_EQUIPMENT_MAP.get(workoutType);

            for (int i = 0; i < Math.min(MAX_EXERCISES_PER_WORKOUT, exerciseNames.length); i++) {
                String name = exerciseNames[i];
                String inst = (instructions != null && i < instructions.length) ? instructions[i]
                        : "Perform " + name + " with proper form.";
                String videoUrl = "https://www.youtube.com/results?search_query="
                        + (searchQueries != null && i < searchQueries.length ? searchQueries[i] : name.replace(" ", "+"))
                        + "+exercise+tutorial";

                // Check if user has equipment for this exercise
                String equipmentType = (equipmentTypes != null && i < equipmentTypes.length)
                        ? equipmentTypes[i] : "BODYWEIGHT";

                // If user doesn't have the required equipment, switch to bodyweight
                if (!userHasEquipment(user, equipmentType)) {
                    equipmentType = "BODYWEIGHT";
                }

                String category = (categories != null && i < categories.length) ? categories[i] : "GENERAL";
                String subCategory = (subCategories != null && i < subCategories.length) ? subCategories[i] : "GENERAL";
                String intensity = (intensities != null && i < intensities.length) ? intensities[i] : "MEDIUM";

                exercises.add(new Exercise(name, DEFAULT_SETS, DEFAULT_REPS,
                        DEFAULT_EX_DURATION, "Various", equipmentType,
                        inst, videoUrl, category, subCategory, intensity, equipmentType));
            }
        }

        // If we got no exercises, use fallback
        if (exercises.isEmpty()) {
            exercises.add(createExercise("Push-Ups", title));
            exercises.add(createExercise("Squats", title));
            exercises.add(createExercise("Planks", title));
        }

        return exercises;
    }

    private boolean userHasEquipment(User user, String equipmentType) {
        if (equipmentType == null || equipmentType.equals("BODYWEIGHT")) {
            return true;
        }
        if (user.getEquipment() == null || user.getEquipment().isEmpty()) {
            return false;
        }
        for (Equipment eq : user.getEquipment()) {
            if (eq.name().equals(equipmentType) || eq.name().contains(equipmentType)) {
                return true;
            }
        }
        return false;
    }

    private String determineWorkoutType(String title) {
        if (title.contains("yoga") || title.contains("stretch") || title.contains("flex")) {
            return "yoga";
        } else if (title.contains("hiit") || title.contains("interval")) {
            return "hiit";
        } else if (title.contains("cardio") || title.contains("run") || title.contains("running")) {
            return "running";
        } else if (title.contains("bike") || title.contains("biking") || title.contains("cycle")) {
            return "biking";
        } else if (title.contains("core") || title.contains("abs")) {
            return "core";
        } else if (title.contains("upper") || title.contains("push") || title.contains("pull")) {
            return "upper";
        } else if (title.contains("lower") || title.contains("leg") || title.contains("squat")) {
            return "lower";
        } else if (title.contains("strength") || title.contains("full body")) {
            return "strength";
        }
        return "strength";
    }

    private String determineCategory(String exerciseName) {
        String lower = exerciseName.toLowerCase();
        if (lower.contains("run") || lower.contains("sprint") || lower.contains("jog")) {
            return "CARDIO";
        } else if (lower.contains("bike") || lower.contains("cycle") || lower.contains("pedal")) {
            return "CARDIO";
        } else if (lower.contains("push") || lower.contains("pull") || lower.contains("press")
                || lower.contains("squat") || lower.contains("lunge") || lower.contains("deadlift")) {
            return "STRENGTH";
        } else if (lower.contains("burpee") || lower.contains("mountain") || lower.contains("jump")) {
            return "HIIT";
        } else if (lower.contains("plank") || lower.contains("crunch") || lower.contains("raise")) {
            return "CORE";
        } else if (lower.contains("yoga") || lower.contains("downward") || lower.contains("warrior")
                || lower.contains("tree") || lower.contains("stretch")) {
            return "FLEXIBILITY";
        }
        return "GENERAL";
    }

    private String determineSubCategory(String exerciseName) {
        String lower = exerciseName.toLowerCase();
        if (lower.contains("run") || lower.contains("sprint") || lower.contains("jog")) {
            return "RUNNING";
        } else if (lower.contains("bike") || lower.contains("cycle") || lower.contains("pedal")) {
            return "BIKING";
        } else if (lower.contains("push") || lower.contains("pull") || lower.contains("press")) {
            return "UPPER_BODY";
        } else if (lower.contains("squat") || lower.contains("lunge") || lower.contains("deadlift")
                || lower.contains("calf")) {
            return "LOWER_BODY";
        } else if (lower.contains("burpee") || lower.contains("mountain") || lower.contains("jump")) {
            return "FULL_BODY";
        } else if (lower.contains("plank") || lower.contains("crunch") || lower.contains("raise")) {
            return "CORE";
        } else if (lower.contains("yoga") || lower.contains("downward") || lower.contains("warrior")
                || lower.contains("tree")) {
            return "YOGA";
        }
        return "GENERAL";
    }

    private String determineIntensity(String exerciseName) {
        String lower = exerciseName.toLowerCase();
        if (lower.contains("sprint") || lower.contains("burpee") || lower.contains("hiit")) {
            return "HIGH";
        } else if (lower.contains("yoga") || lower.contains("stretch") || lower.contains("plank")) {
            return "LOW";
        }
        return "MEDIUM";
    }

    private String determineEquipment(String exerciseName) {
        String lower = exerciseName.toLowerCase();
        if (lower.contains("bike") || lower.contains("cycle")) {
            return "STATIONARY_BIKE";
        } else if (lower.contains("pull") || lower.contains("chin")) {
            return "PULL_UP_BAR";
        } else if (lower.contains("press") && lower.contains("dumb")) {
            return "DUMBBELLS";
        } else if (lower.contains("deadlift") || lower.contains("barbell")) {
            return "BARBELL";
        }
        return "BODYWEIGHT";
    }

    private String getInstructionForExercise(String name) {
        String lower = name.toLowerCase();
        if (lower.contains("push")) {
            return "Lower your chest to the floor, push up explosively. Keep your body in a straight line.";
        } else if (lower.contains("squat")) {
            return "Keep your chest up, lower your hips back and down. Go to at least parallel.";
        } else if (lower.contains("plank")) {
            return "Hold a straight body line on your forearms. Keep your core tight.";
        } else if (lower.contains("lunge")) {
            return "Step forward, lower your hips until both knees are bent at 90 degrees.";
        } else if (lower.contains("run") || lower.contains("jog")) {
            return "Jog or run at a steady pace with proper form. Keep your back straight.";
        } else if (lower.contains("bike") || lower.contains("cycle")) {
            return "Pedal at moderate to high intensity. Maintain a steady cadence.";
        } else if (lower.contains("burpee")) {
            return "Drop to squat, kick feet back, do push-up, jump up. That's one rep.";
        } else if (lower.contains("yoga") || lower.contains("downward") || lower.contains("warrior")) {
            return "Perform the yoga pose with proper form and controlled breathing.";
        }
        return "Perform " + name + " with proper form. Maintain controlled movement throughout.";
    }

    private Exercise createExercise(String name, String title) {
        String category = determineCategory(name);
        String subCategory = determineSubCategory(name);
        String intensity = determineIntensity(name);
        String equipment = determineEquipment(name);
        String instruction = getInstructionForExercise(name);
        String videoUrl = "https://www.youtube.com/results?search_query="
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

        // Get goal-based workout types and titles
        FitnessGoal goal = user != null ? user.getGoal() : FitnessGoal.MAINTAIN_GENERAL_FITNESS;
        String[] workoutTypes = GOAL_WORKOUT_TYPES.getOrDefault(goal,
                new String[]{"strength", "running", "strength", "biking", "hiit", "running", "strength"});
        String[] workoutTitles = GOAL_WORKOUT_TITLES.getOrDefault(goal,
                new String[]{"Full Body Strength", "Cardio Running", "Upper Body Strength",
                        "Cardio Biking", "HIIT Training", "Cardio Running", "Lower Body Strength"});
        String[] workoutDescs = GOAL_WORKOUT_DESCS.getOrDefault(goal,
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

                // Get exercises for this workout type
                List<Exercise> exercises = getExercisesForWorkoutType(type, user);

                String title = workoutTitles[typeIdx];
                String desc = workoutDescs[typeIdx];
                int[] nutrition = nutritionValues[typeIdx % nutritionValues.length];

                plans.add(new WorkoutPlan(dateLabel, title, desc,
                        "GENERAL", "GENERAL", "MEDIUM", "Various", "BODYWEIGHT",
                        targetDuration, nutrition[0], nutrition[1], nutrition[2], exercises));
            } else {
                plans.add(new WorkoutPlan(dateLabel, "Rest & Recovery",
                        "Rest day. Focus on recovery, hydration, and light stretching.",
                        "REST", "REST", "LOW", "NONE", "BODYWEIGHT",
                        0, 0, 0, 0, new ArrayList<>()));
            }
        }

        LOGGER.log(Level.INFO, "Generated {0} fallback plans", plans.size());
        return plans;
    }

    private List<Exercise> getExercisesForWorkoutType(String type, User user) {
        List<Exercise> exercises = new ArrayList<>();

        if (WORKOUT_EXERCISE_MAP.containsKey(type)) {
            String[] exerciseNames = WORKOUT_EXERCISE_MAP.get(type);
            String[] instructions = WORKOUT_INSTRUCTIONS_MAP.get(type);
            String[] searchQueries = WORKOUT_SEARCH_MAP.get(type);
            String[] categories = WORKOUT_CATEGORY_MAP.get(type);
            String[] subCategories = WORKOUT_SUBCATEGORY_MAP.get(type);
            String[] intensities = WORKOUT_INTENSITY_MAP.get(type);
            String[] equipmentTypes = WORKOUT_EQUIPMENT_MAP.get(type);

            for (int i = 0; i < Math.min(MAX_EXERCISES_PER_WORKOUT, exerciseNames.length); i++) {
                String name = exerciseNames[i];
                String inst = (instructions != null && i < instructions.length) ? instructions[i]
                        : "Perform " + name + " with proper form.";
                String videoUrl = "https://www.youtube.com/results?search_query="
                        + (searchQueries != null && i < searchQueries.length ? searchQueries[i] : name.replace(" ", "+"))
                        + "+exercise+tutorial";

                String equipmentType = (equipmentTypes != null && i < equipmentTypes.length)
                        ? equipmentTypes[i] : "BODYWEIGHT";

                // Check if user has equipment
                if (!userHasEquipment(user, equipmentType)) {
                    equipmentType = "BODYWEIGHT";
                }

                String category = (categories != null && i < categories.length) ? categories[i] : "GENERAL";
                String subCategory = (subCategories != null && i < subCategories.length) ? subCategories[i] : "GENERAL";
                String intensity = (intensities != null && i < intensities.length) ? intensities[i] : "MEDIUM";

                exercises.add(new Exercise(name, DEFAULT_SETS, DEFAULT_REPS,
                        DEFAULT_EX_DURATION, "Various", equipmentType,
                        inst, videoUrl, category, subCategory, intensity, equipmentType));
            }
        }

        // Fallback exercises
        if (exercises.isEmpty()) {
            exercises.add(createExercise("Push-Ups", type));
            exercises.add(createExercise("Squats", type));
            exercises.add(createExercise("Planks", type));
        }

        return exercises;
    }
}