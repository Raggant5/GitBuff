package data_access;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import entity.CommonUser;
import entity.DietaryRestriction;
import entity.MealRecommendation;
import entity.User;

/**
 * Unit tests for the Spoonacular meal recommendation DAO's graceful-fallback behaviour
 * and dietary-restriction mapping.
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

    @Test
    public void mapToSpoonacularDietPicksVegetarianDiet() {
        final SpoonacularMealRecommendationDataAccessObject dao =
                new SpoonacularMealRecommendationDataAccessObject("some-key");
        final Set<DietaryRestriction> restrictions = EnumSet.of(DietaryRestriction.VEGETARIAN);

        assertEquals("vegetarian", dao.mapToSpoonacularDiet(restrictions));
    }

    @Test
    public void mapToSpoonacularDietReturnsEmptyForNoMappedRestriction() {
        final SpoonacularMealRecommendationDataAccessObject dao =
                new SpoonacularMealRecommendationDataAccessObject("some-key");
        final Set<DietaryRestriction> restrictions = EnumSet.of(DietaryRestriction.NUT_ALLERGY);

        assertTrue(dao.mapToSpoonacularDiet(restrictions).isEmpty());
    }

    @Test
    public void mapToExcludedIngredientsIncludesNutAllergyExclusions() {
        final SpoonacularMealRecommendationDataAccessObject dao =
                new SpoonacularMealRecommendationDataAccessObject("some-key");
        final Set<DietaryRestriction> restrictions = EnumSet.of(DietaryRestriction.NUT_ALLERGY);

        assertTrue(dao.mapToExcludedIngredients(restrictions).contains("peanut"));
    }

    @Test
    public void mapToExcludedIngredientsCombinesHalalAndNutAllergy() {
        final SpoonacularMealRecommendationDataAccessObject dao =
                new SpoonacularMealRecommendationDataAccessObject("some-key");
        final Set<DietaryRestriction> restrictions =
                EnumSet.of(DietaryRestriction.HALAL, DietaryRestriction.NUT_ALLERGY);

        final String exclude = dao.mapToExcludedIngredients(restrictions);
        assertTrue(exclude.contains("pork"));
        assertTrue(exclude.contains("peanut"));
    }

    @Test
    public void mapToExcludedIngredientsReturnsEmptyForDietOnlyRestrictions() {
        final SpoonacularMealRecommendationDataAccessObject dao =
                new SpoonacularMealRecommendationDataAccessObject("some-key");
        final Set<DietaryRestriction> restrictions = EnumSet.of(DietaryRestriction.VEGAN);

        assertTrue(dao.mapToExcludedIngredients(restrictions).isEmpty());
    }
}
