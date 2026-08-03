package data_access;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import entity.FoodSearchResult;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import use_case.nutrition.food.search_food.SearchFoodDataAccessInterface;

/**
 * Real implementation that calls the nutrition search API.
 */
public class SearchFoodDataAccessObject implements SearchFoodDataAccessInterface {

    private static final String API_URL = "https://calorieapiadmin.com";
    private static final int LIMIT = 5;
    private static final int SKIP = 0;
    private final OkHttpClient client = new OkHttpClient();

    @Override
    public List<FoodSearchResult> searchFood(String searchQuery) throws IOException {

        final String token = loadKeyFromDotEnv("NUTRITION_API_KEY");

        final Request request = new Request.Builder()
                .url(String.format(
                        "%s/api/v1/public/search/foods?q=%s&limit=%d&skip=%d&match_mode=any&verified_only=true",
                        API_URL,
                        searchQuery,
                        LIMIT,
                        SKIP))
                .addHeader("Authorization", token)
                .build();

        try (Response response = client.newCall(request).execute()) {

            if (!response.isSuccessful()) {
                throw new IOException(
                        "Search food result failed: " + response);
            }

            final JSONObject responseBody =
                    new JSONObject(response.body().string());

            final JSONArray foods = responseBody.getJSONArray("data");

            final List<FoodSearchResult> results = new ArrayList<>();

            for (int i = 0; i < foods.length(); i++) {
                final JSONObject food = foods.getJSONObject(i);

                results.add(new FoodSearchResult(
                        food.getString("name"),
                        food.getString("serving"),
                        food.getDouble("serving_size"),
                        food.getJSONObject("meal").getDouble("calories"),
                        food.getJSONObject("meal")
                                .getJSONObject("macros")
                                .getDouble("protein"),
                        food.getJSONObject("meal")
                                .getJSONObject("macros")
                                .getDouble("carbs"),
                        food.getJSONObject("meal")
                                .getJSONObject("macros")
                                .getDouble("fat")
                ));
            }

            return results;
        }
    }

    private String loadKeyFromDotEnv(String apiKeyName) {
        final File envFile = new File(".env");
        if (!envFile.exists()) {
            return null;
        }
        try (Scanner scanner = new Scanner(envFile, StandardCharsets.UTF_8)) {
            while (scanner.hasNextLine()) {
                final String line = scanner.nextLine().trim();
                if (line.startsWith(apiKeyName + "=")) {
                    return line.substring(
                            (apiKeyName + "=").length()).trim();
                }
            }
        }
        catch (Exception ignored) {
            // Return null if .env cannot be read
        }
        return null;
    }
}
