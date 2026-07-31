package data_access;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import entity.User;
import use_case.recommendation.AiWorkoutDataAccessInterface;

/**
 * Service Adapter for generating custom workout routines using Google's Gemini API (1.5 Flash).
 */
public class AiWorkoutDataAccessObject implements AiWorkoutDataAccessInterface {

    private static final int HTTP_OK = 200;
    private final String apiKey;

    public AiWorkoutDataAccessObject() {
        this.apiKey = System.getenv("GEMINI_API_KEY");
    }

    public AiWorkoutDataAccessObject(final String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public String generateWorkoutPlan(final User user) {
        if (user == null || user.getGoal() == null) {
            return "Please complete your profile to view AI recommendations.";
        }

        if (this.apiKey == null || this.apiKey.trim().isEmpty() || "YOUR_API_KEY_HERE".equals(this.apiKey)) {
            return "Note: AI API Key is not set. Defaulting to focus: "
                    + user.getGoal().getWorkoutFocus();
        }

        try {
            final String endpoint = "https://generativelanguage.googleapis.com/v1beta/models/"
                    + "gemini-1.5-flash:generateContent?key=" + this.apiKey;
            final URL url = new URL(endpoint);
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);
            connection.setDoOutput(true);

            final String prompt = String.format(
                    "Act as an expert fitness trainer. Create a concise workout plan for:\\n"
                            + "- Goal: %s\\n- Activity: %s\\n- Equipment: %s\\n- Days: %s\\n- Duration: %d mins\\n"
                            + "Keep the output clean, motivational, and formatted in 3-4 bullet points.",
                    user.getGoal().toString(),
                    user.getActivityLevel() != null ? user.getActivityLevel().getDescription() : "Moderate",
                    (user.getEquipment() == null || user.getEquipment().isEmpty()) ? "Bodyweight" : user.getEquipment().toString(),
                    (user.getPreferredWorkoutDays() == null || user.getPreferredWorkoutDays().isEmpty()) ? "Flexible" : user.getPreferredWorkoutDays().toString(),
                    user.getPreferredWorkoutDurationMinutes() > 0 ? user.getPreferredWorkoutDurationMinutes() : 45
            );

            final String jsonInputString = String.format(
                    "{\"contents\": [{\"parts\": [{\"text\": \"%s\"}]}]}", prompt
            );

            try (OutputStream outputStream = connection.getOutputStream()) {
                final byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                outputStream.write(input, 0, input.length);
            }

            final int responseCode = connection.getResponseCode();
            if (responseCode == HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    final StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line.trim());
                    }
                    return extractAiText(response.toString());
                }
            }
            return "Focus: " + user.getGoal().getWorkoutFocus();
        }
        catch (final Throwable ex) {
            // Catch all errors so the UI thread never freezes
            return "Suggested focus: " + (user.getGoal() != null ? user.getGoal().getWorkoutFocus() : "General fitness");
        }
    }

    private String extractAiText(final String json) {
        final int textIndex = json.indexOf("\"text\": \"");
        if (textIndex != -1) {
            final int startIndex = textIndex + 9;
            final int endIndex = json.indexOf("\"", startIndex);
            if (endIndex != -1) {
                return json.substring(startIndex, endIndex)
                        .replace("\\n", "\n")
                        .replace("\\\"", "\"");
            }
        }
        return json;
    }
}