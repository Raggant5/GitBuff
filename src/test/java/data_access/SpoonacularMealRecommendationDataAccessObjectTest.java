package data_access;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;

import entity.CommonUser;
import entity.MealRecommendation;
import entity.User;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Spoonacular meal recommendation DAO's graceful-fallback behaviour.
 */
public class SpoonacularMealRecommendationDataAccessObjectTest {

    private static final int TARGET_CALORIES = 2000;

    @Test
    public void generateMealRecommendationsWithoutApiKeyReturnsFallback() {
        final SpoonacularMealRecommendationDataAccessObject dao =
                new SpoonacularMealRecommendationDataAccessObject("");
        final User user = new CommonUser("aahir", "password");

        final List<MealRecommendation> meals = dao.generateMealRecommendations(user, TARGET_CALORIES);

        assertFalse(meals.isEmpty());
    }

    @Test
    public void generateMealRecommendationsWithZeroTargetReturnsFallback() {
        final SpoonacularMealRecommendationDataAccessObject dao =
                new SpoonacularMealRecommendationDataAccessObject("some-key");
        final User user = new CommonUser("aahir", "password");

        final List<MealRecommendation> meals = dao.generateMealRecommendations(user, 0);

        assertFalse(meals.isEmpty());
    }
}
