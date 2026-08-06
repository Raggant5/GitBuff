package data_access;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import entity.MealRecommendation;
import entity.User;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import use_case.recommendation.FoodRecommendationDataAccessInterface;

/**
 * DAO requesting a one-day meal plan matching a target calorie count, via the Spoonacular Food API
 * (https://spoonacular.com/food-api).
 */
public class SpoonacularMealRecommendationDataAccessObject implements FoodRecommendationDataAccessInterface {

    private static final String BASE_URL = "https://api.spoonacular.com";
    private static final String API_KEY_NAME = "SPOONACULAR_API_KEY";

    private final OkHttpClient client = new OkHttpClient();
    private final String apiKey;

    /**
     * Constructs a SpoonacularMealRecommendationDataAccessObject reading key from local .env file
     * or system environment.
     */
    public SpoonacularMealRecommendationDataAccessObject() {
        String key = loadKeyFromDotEnv();
        if (key == null || key.trim().isEmpty()) {
            key = System.getenv(API_KEY_NAME);
        }
        this.apiKey = key;
    }

    /**
     * Constructs a SpoonacularMealRecommendationDataAccessObject with an explicit API key.
     *
     * @param apiKey the Spoonacular API key
     */
    public SpoonacularMealRecommendationDataAccessObject(final String apiKey) {
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
                if (line.startsWith(API_KEY_NAME + "=")) {
                    return line.substring((API_KEY_NAME + "=").length()).trim();
                }
            }
        }
        catch (final Exception ex) {
            // Fallback on error reading local .env file
        }
        return null;
    }

    @Override
    public List<MealRecommendation> generateMealRecommendations(final User user, final int targetCalories) {
        if (this.apiKey == null || this.apiKey.trim().isEmpty() || "YOUR_API_KEY_HERE".equals(this.apiKey)
                || targetCalories <= 0) {
            return getFallbackMeals();
        }

        try {
            final String url = BASE_URL + "/mealplanner/generate?timeFrame=day&targetCalories=" + targetCalories
                    + "&apiKey=" + URLEncoder.encode(this.apiKey, StandardCharsets.UTF_8);
            final Request request = new Request.Builder().url(url).get().build();

            try (Response response = this.client.newCall(request).execute()) {
                if (!response.isSuccessful() || response.body() == null) {
                    return getFallbackMeals();
                }
                final JSONObject result = new JSONObject(response.body().string());
                final JSONArray mealsJson = result.optJSONArray("meals");
                if (mealsJson == null || mealsJson.isEmpty()) {
                    return getFallbackMeals();
                }

                final List<MealRecommendation> meals = new ArrayList<>();
                for (int i = 0; i < mealsJson.length(); i++) {
                    final JSONObject meal = mealsJson.getJSONObject(i);
                    meals.add(new MealRecommendation(
                            meal.optString("title", "Suggested meal"),
                            meal.optInt("readyInMinutes", 0),
                            meal.optString("sourceUrl", "")));
                }
                return meals;
            }
        }
        catch (final IOException | RuntimeException ex) {
            return getFallbackMeals();
        }
    }

    private List<MealRecommendation> getFallbackMeals() {
        final List<MealRecommendation> meals = new ArrayList<>();
        meals.add(new MealRecommendation("Grilled chicken, rice, and steamed vegetables", 25, ""));
        meals.add(new MealRecommendation("Greek yogurt with berries and nuts", 5, ""));
        meals.add(new MealRecommendation("Salmon, sweet potato, and salad", 30, ""));
        return meals;
    }
}
