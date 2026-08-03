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
    private static final int DEFAULT_SETS = 3;
    private static final int DEFAULT_REPS = 12;
    private static final int DEFAULT_EX_DURATION = 10;

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

            final int targetMinutes = (user.getPreferredWorkoutDurationMinutes() > 0)
                    ? user.getPreferredWorkoutDurationMinutes()
                    : 45;

            final String equipmentStr = (user.getEquipment() == null || user.getEquipment().isEmpty())
                    ? "Bodyweight Only"
                    : user.getEquipment().toString();

            final String genderStr = user.getGender() != null ? user.getGender().toString() : "Unspecified";
            final String activityStr = user.getActivityLevel() != null
                    ? user.getActivityLevel().getDescription()
                    : "Moderately active";

            final int seed = this.random.nextInt(SEED_MAX);

            final String systemPrompt = String.format(
                    "Act as a elite personal trainer. Create a customized 14-day (2-week) workout schedule starting "
                            + "from %s. VARIETY SEED: %d.\\n"
                            + "STRICT USER PROFILE REQUIREMENTS:\\n"
                            + "- Goal: %s\\n"
                            + "- Weight: %.1f kg | Height: %.2f m | Gender: %s | Activity Level: %s\\n"
                            + "- Available Equipment: [%s] (ONLY prescribe exercises using available equipment!)\\n"
                            + "- Preferred Workout Days: [%s] (ONLY schedule active workouts on these days. "
                            + "All other days MUST be 'Rest & Recovery' with empty exercises [])\\n"
                            + "- Session Target Duration: %d minutes\\n"
                            + "Output ONLY a valid JSON array of 14 day blocks matching this exact schema:\\n"
                            + "[{\\\"date\\\": \\\"Monday, Aug 3\\\", \\\"title\\\": \\\"Custom Upper Body\\\", "
                            + "\\\"description\\\": \\\"Tailored hypertrophy for user goal\\\", "
                            + "\\\"estimatedCaloriesBurned\\\": 380, \\\"estimatedFatBurnedGrams\\\": 18, "
                            + "\\\"estimatedCarbsBurnedGrams\\\": 55, \\\"exercises\\\": "
                            + "[{\\\"name\\\": \\\"Push-Ups\\\", \\\"sets\\\": 3, \\\"reps\\\": 12, "
                            + "\\\"durationMinutes\\\": 10, \\\"targetMuscleGroup\\\": \\\"Chest\\\", "
                            + "\\\"equipmentRequired\\\": \\\"Bodyweight\\\", "
                            + "\\\"instructions\\\": \\\"Lower chest steadily.\\\", "
                            + "\\\"videoUrl\\\": \\\"https://www.youtube.com/results?search_query=Push-Ups\\\"}]}]",
                    startDateStr, seed, user.getGoal().toString(), user.getWeight(), user.getHeight(),
                    genderStr, activityStr, equipmentStr, preferredDaysStr, targetMinutes
            );

            final String jsonInputString = String.format(
                    "{\"contents\": [{\"parts\": [{\"text\": \"%s\"}]}]}",
                    systemPrompt.replace("\"", "\\\"")
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
                    if (!exBlock.contains("sets") && !exBlock.contains("instructions")) {
                        continue;
                    }
                    final String name = extractVal(exBlock, "name");
                    final int sets = extractInt(exBlock, "sets", DEFAULT_SETS);
                    final int reps = extractInt(exBlock, "reps", DEFAULT_REPS);
                    final int exDuration = extractInt(exBlock, "durationMinutes", DEFAULT_EX_DURATION);
                    final String targetMuscle = extractVal(exBlock, "targetMuscleGroup");
                    final String equipReq = extractVal(exBlock, "equipmentRequired");
                    final String inst = extractVal(exBlock, "instructions");
                    String vid = extractVal(exBlock, "videoUrl");
                    if (vid.isEmpty()) {
                        vid = "https://www.youtube.com/results?search_query=how+to+do+" + name.replace(" ", "+");
                    }
                    exercises.add(new Exercise(name, sets, reps, exDuration, targetMuscle,
                            equipReq, inst, vid));
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
                exercises.add(new Exercise("Bodyweight Squats", DEFAULT_SETS, 15,
                        10, "Legs", "Bodyweight",
                        "Keep feet shoulder-width apart, lower hips back and down.",
                        "https://www.youtube.com/results?search_query=how+to+do+bodyweight+squats"));
                exercises.add(new Exercise("Push-Ups", DEFAULT_SETS, 10,
                        10, "Chest", "Bodyweight",
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
}