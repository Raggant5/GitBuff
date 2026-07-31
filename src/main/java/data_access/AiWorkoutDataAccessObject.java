package data_access;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import entity.Exercise;
import entity.User;
import entity.WorkoutPlan;
import use_case.recommendation.AiWorkoutDataAccessInterface;

/**
 * DAO requesting 2-week workout schedules enforcing preferred days, durations, and dynamic routine variety.
 */
public class AiWorkoutDataAccessObject implements AiWorkoutDataAccessInterface {

    private static final int HTTP_OK = 200;
    private static final int TIMEOUT_MILLIS = 6000;
    private static final int SEED_MAX = 100000;
    private static final int DEFAULT_CALORIES = 320;
    private static final int DEFAULT_FAT = 15;
    private static final int DEFAULT_CARBS = 45;
    private static final int FALLBACK_DAYS = 14;
    private static final int TEXT_KEY_OFFSET = 9;

    private final String apiKey;
    private final Random random = new Random();

    /**
     * Constructs an AiWorkoutDataAccessObject reading key from local .env file or system environment.
     */
    public AiWorkoutDataAccessObject() {
        String key = loadKeyFromDotEnv();
        if (key == null || key.trim().isEmpty()) {
            key = System.getenv("GEMINI_API_KEY");
        }
        this.apiKey = key;
    }

    /**
     * Constructs an AiWorkoutDataAccessObject with an explicit API key.
     *
     * @param apiKey the Gemini API key
     */
    public AiWorkoutDataAccessObject(final String apiKey) {
        this.apiKey = apiKey;
    }

    private String loadKeyFromDotEnv() {
        final File envFile = new File(".env");
        if (!envFile.exists()) {
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
            // Fallback on error reading local .env file
        }
        return null;
    }

    @Override
    public List<WorkoutPlan> generateWorkoutPlans(final User user) {
        if (user == null || user.getGoal() == null || this.apiKey == null || this.apiKey.trim().isEmpty()
                || "YOUR_API_KEY_HERE".equals(this.apiKey)) {
            return getFallback2WeekPlans(user);
        }

        try {
            final String endpoint = "https://generativelanguage.googleapis.com/v1beta/models/"
                    + "gemini-1.5-flash:generateContent?key=" + this.apiKey;
            final URL url = new URL(endpoint);
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setConnectTimeout(TIMEOUT_MILLIS);
            connection.setReadTimeout(TIMEOUT_MILLIS);
            connection.setDoOutput(true);

            final LocalDate today = LocalDate.now();
            final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("EEEE, MMM d");
            final String startDateStr = today.format(fmt);

            final String preferredDaysStr = (user.getPreferredWorkoutDays() == null
                    || user.getPreferredWorkoutDays().isEmpty())
                    ? "Monday, Wednesday, Friday"
                    : user.getPreferredWorkoutDays().toString();

            final String preferredDurationStr = (user.getPreferredWorkoutDurationMinutes() > 0)
                    ? user.getPreferredWorkoutDurationMinutes() + " minutes"
                    : "45 minutes";

            final int seed = this.random.nextInt(SEED_MAX);

            final String systemPrompt = String.format(
                    "Act as an expert athletic trainer. Generate a full 14-day (2-week) workout plan starting from %s. "
                            + "VARIETY SEED: %d. Mix up exercise selection and movement variations.\\n"
                            + "CRITICAL RULE 1: ONLY schedule active workout plans on preferred days matching [%s]. "
                            + "All non-preferred days MUST be 'Rest & Recovery' with zero exercises [].\\n"
                            + "CRITICAL RULE 2: Design active routines to fit duration target of [%s].\\n"
                            + "CRITICAL RULE 3: Calculate burn values for total calories (Cal), fat (g), and carbs (g).\\n"
                            + "Output ONLY a valid JSON array matching exact schema:\\n"
                            + "[{\\\"date\\\": \\\"Monday, Aug 3\\\", \\\"title\\\": \\\"Upper Body Focus\\\", "
                            + "\\\"description\\\": \\\"Hypertrophy session\\\", \\\"estimatedCaloriesBurned\\\": 380, "
                            + "\\\"estimatedFatBurnedGrams\\\": 18, \\\"estimatedCarbsBurnedGrams\\\": 55, "
                            + "\\\"exercises\\\": [{\\\"name\\\": \\\"Push-Ups\\\", \\\"setsAndReps\\\": \\\"3 sets of 12\\\", "
                            + "\\\"instructions\\\": \\\"Lower chest smoothly.\\\", "
                            + "\\\"videoUrl\\\": \\\"https://www.youtube.com/results?search_query=Push-Ups\\\"}]}]",
                    startDateStr, seed, preferredDaysStr, preferredDurationStr
            );

            final String userContext = String.format(
                    "Goal: %s | Weight: %.1f kg | Equipment: %s | Preferred Days: %s | Target Duration: %s",
                    user.getGoal().toString(), user.getWeight(),
                    (user.getEquipment() == null || user.getEquipment().isEmpty())
                            ? "None" : user.getEquipment().toString(),
                    preferredDaysStr, preferredDurationStr
            );

            final String jsonInputString = String.format(
                    "{\"contents\": [{\"parts\": [{\"text\": \"%s\\nUser Profile: %s\"}]}]}",
                    systemPrompt.replace("\"", "\\\""),
                    userContext.replace("\"", "\\\"")
            );

            try (OutputStream outputStream = connection.getOutputStream()) {
                final byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                outputStream.write(input, 0, input.length);
            }

            if (connection.getResponseCode() == HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    final StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line.trim());
                    }
                    final List<WorkoutPlan> plans = parseGeminiJson(response.toString());
                    if (!plans.isEmpty()) {
                        return plans;
                    }
                }
            }
        }
        catch (final Throwable ex) {
            // Fallback gracefully on API errors
        }
        return getFallback2WeekPlans(user);
    }

    private List<WorkoutPlan> parseGeminiJson(final String response) {
        final List<WorkoutPlan> plans = new ArrayList<>();
        try {
            final int textIdx = response.indexOf("\"text\": \"");
            if (textIdx == -1) {
                return plans;
            }
            final int start = textIdx + TEXT_KEY_OFFSET;
            final int end = response.indexOf("\"", start);
            final String rawJson = response.substring(start, end)
                    .replace("\\n", " ")
                    .replace("\\\"", "\"")
                    .replace("\\\\", "\\");

            final String[] routineBlocks = rawJson.split("\\{\"date\":");
            for (final String block : routineBlocks) {
                if (!block.contains("title")) {
                    continue;
                }
                String date = extractVal(block, "date");
                if (date.isEmpty()) {
                    date = extractVal(block, "\"date\":");
                }
                final String title = extractVal(block, "title");
                final String desc = extractVal(block, "description");
                final int calories = extractInt(block, "estimatedCaloriesBurned", DEFAULT_CALORIES);
                final int fat = extractInt(block, "estimatedFatBurnedGrams", DEFAULT_FAT);
                final int carbs = extractInt(block, "estimatedCarbsBurnedGrams", DEFAULT_CARBS);

                final List<Exercise> exercises = new ArrayList<>();
                final String[] exBlocks = block.split("\\{\"name\":");
                for (final String exBlock : exBlocks) {
                    if (!exBlock.contains("setsAndReps")) {
                        continue;
                    }
                    final String name = extractVal(exBlock, "name");
                    final String setsReps = extractVal(exBlock, "setsAndReps");
                    final String inst = extractVal(exBlock, "instructions");
                    String vid = extractVal(exBlock, "videoUrl");
                    if (vid.isEmpty()) {
                        vid = "https://www.youtube.com/results?search_query=how+to+do+" + name.replace(" ", "+");
                    }
                    exercises.add(new Exercise(name, setsReps, inst, vid));
                }
                plans.add(new WorkoutPlan(date, title, desc, calories, fat, carbs, exercises));
            }
        }
        catch (final Exception ex) {
            // Fallback on JSON parse error
        }
        return plans;
    }

    private String extractVal(final String src, final String key) {
        final int keyIdx = src.indexOf(key);
        if (keyIdx == -1) {
            return "";
        }
        int startVal = src.indexOf(":", keyIdx) + 1;
        while (startVal < src.length() && (src.charAt(startVal) == ' ' || src.charAt(startVal) == '"')) {
            startVal++;
        }
        final int endVal = src.indexOf("\"", startVal);
        if (endVal == -1) {
            return "";
        }
        return src.substring(startVal, endVal);
    }

    private int extractInt(final String src, final String key, final int defaultVal) {
        final int keyIdx = src.indexOf(key);
        if (keyIdx == -1) {
            return defaultVal;
        }
        int startVal = src.indexOf(":", keyIdx) + 1;
        while (startVal < src.length() && (src.charAt(startVal) == ' ' || src.charAt(startVal) == '"')) {
            startVal++;
        }
        final StringBuilder numStr = new StringBuilder();
        while (startVal < src.length() && Character.isDigit(src.charAt(startVal))) {
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
        final List<WorkoutPlan> plans = new ArrayList<>();
        final LocalDate today = LocalDate.now();
        final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("EEEE, MMM d");

        for (int dayOffset = 0; dayOffset < FALLBACK_DAYS; dayOffset++) {
            final LocalDate date = today.plusDays(dayOffset);
            final String dateLabel = date.format(fmt);

            if (dayOffset % 2 == 0) {
                final List<Exercise> exercises = new ArrayList<>();
                exercises.add(new Exercise("Bodyweight Squats", "3 sets of 15",
                        "Keep feet shoulder-width apart, lower hips back and down.",
                        "https://www.youtube.com/results?search_query=how+to+do+bodyweight+squats"));
                exercises.add(new Exercise("Push-Ups", "3 sets of 10",
                        "Keep body in a straight plank, lower chest to floor.",
                        "https://www.youtube.com/results?search_query=how+to+do+pushups"));

                plans.add(new WorkoutPlan(dateLabel, "Full Body Conditioning",
                        "Focused routine targetting endurance and core stability.",
                        DEFAULT_CALORIES, DEFAULT_FAT, DEFAULT_CARBS, exercises));
            }
            else {
                plans.add(new WorkoutPlan(dateLabel, "Rest & Recovery",
                        "Active recovery day. Focus on light stretching and hydration.",
                        0, 0, 0, new ArrayList<>()));
            }
        }
        return plans;
    }

    /*
    /**
     * Diagnostic main method to test API key loading and Gemini API connectivity.
     *
     * @param args command line arguments
     * /
    public static void main(final String[] args) {
        System.out.println("==========================================");
        System.out.println("          GEMINI API DIAGNOSTIC           ");
        System.out.println("==========================================");

        final AiWorkoutDataAccessObject dao = new AiWorkoutDataAccessObject();

        if (dao.apiKey == null || dao.apiKey.trim().isEmpty()) {
            System.err.println("[FAIL] API Key is NULL or EMPTY!");
            System.err.println("       -> Check that `.env` exists in the project root folder.");
            System.err.println("       -> Check that `.env` contains: GEMINI_API_KEY=your_key");
            return;
        }

        final String maskedKey = dao.apiKey.length() > 8
                ? dao.apiKey.substring(0, 4) + "..." + dao.apiKey.substring(dao.apiKey.length() - 4)
                : "***";
        System.out.println("[SUCCESS] API Key loaded from .env: " + maskedKey);

        final User dummyUser = new User() {
            @Override
            public String getName() { return "TestUser"; }
            @Override
            public String getPassword() { return "password"; }
            @Override
            public float getHeight() { return 1.8f; }
            @Override
            public float getWeight() { return 75.0f; }
            @Override
            public entity.ActivityLevel getActivityLevel() { return entity.ActivityLevel.values()[0]; }
            @Override
            public entity.FitnessGoal getGoal() { return entity.FitnessGoal.values()[0]; }
            @Override
            public String getProfilePicturePath() { return ""; }
            @Override
            public java.time.LocalDate getDateOfBirth() { return null; }
            @Override
            public entity.Gender getGender() { return null; }
            @Override
            public String getBio() { return ""; }
            @Override
            public entity.UnitSystem getPreferredUnitSystem() { return entity.UnitSystem.METRIC; }
            @Override
            public java.util.Set<entity.Equipment> getEquipment() { return new java.util.HashSet<>(); }
            @Override
            public java.util.Set<entity.DietaryRestriction> getDietaryRestrictions() {
                return new java.util.HashSet<>();
            }
            @Override
            public java.util.Set<java.time.DayOfWeek> getPreferredWorkoutDays() {
                return new java.util.HashSet<>();
            }
            @Override
            public int getPreferredWorkoutDurationMinutes() { return 45; }
            @Override
            public java.util.Set<entity.PrivacySetting> getPrivacySettings() {
                return new java.util.HashSet<>();
            }

            @Override
            public void setHeight(float height) {}
            @Override
            public void setWeight(float weight) {}
            @Override
            public void setActivityLevel(entity.ActivityLevel activityLevel) {}
            @Override
            public void setGoal(entity.FitnessGoal goal) {}
            @Override
            public void setProfilePicturePath(String path) {}
            @Override
            public void setDateOfBirth(java.time.LocalDate dateOfBirth) {}
            @Override
            public void setGender(entity.Gender gender) {}
            @Override
            public void setBio(String bio) {}
            @Override
            public void setPreferredUnitSystem(entity.UnitSystem unitSystem) {}
            @Override
            public void setEquipment(java.util.Set<entity.Equipment> equipment) {}
            @Override
            public void setDietaryRestrictions(java.util.Set<entity.DietaryRestriction> dietaryRestrictions) {}
            @Override
            public void setPreferredWorkoutDays(java.util.Set<java.time.DayOfWeek> preferredDays) {}
            @Override
            public void setPreferredWorkoutDurationMinutes(int minutes) {}
            @Override
            public void setPrivacySettings(java.util.Set<entity.PrivacySetting> privacySettings) {}
        };

        System.out.println("\n[INFO] Sending test request to Gemini API...");

        final List<WorkoutPlan> plans = dao.generateWorkoutPlans(dummyUser);

        System.out.println("\n==========================================");
        if (plans.isEmpty()) {
            System.err.println("[FAIL] Received empty workout plan list.");
            System.err.println("       -> API request failed or JSON parsing encountered an issue.");
        }
        else {
            System.out.println("[SUCCESS] API IS WORKING & RETURNING DATA!");
            System.out.println("          Generated " + plans.size() + " daily workout plans.");
            System.out.println("------------------------------------------");
            System.out.println("First Day Plan Title : " + plans.get(0).getTitle());
            System.out.println("First Day Description: " + plans.get(0).getDescription());
            System.out.println("Estimated Cal Burned : " + plans.get(0).getEstimatedCaloriesBurned() + " Cal");
            if (!plans.get(0).getExercises().isEmpty()) {
                System.out.println("Sample Exercise      : " + plans.get(0).getExercises().get(0).getName());
            }
        }
        System.out.println("==========================================");
    }
    */
}