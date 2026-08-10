package interface_adapter.recommendation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import interface_adapter.nutrition.NutritionState;
import interface_adapter.nutrition.NutritionViewModel;
import use_case.recommendation.MealRecommendationData;
import use_case.recommendation.RefreshMealRecommendationsOutputData;

/**
 * Unit tests for the Refresh Meal Recommendations Presenter. The key thing this presenter must
 * get right: it only ever touches NutritionState. Unlike the workout-plan presenter, it has no
 * WorkoutsViewModel dependency at all, so a meals-only refresh can never wipe out the user's
 * current workout plans.
 */
public class RefreshMealRecommendationsPresenterTest {

    private static final double TEST_BMI = 24.7;
    private static final int TEST_CALORIES = 2800;
    private static final int TEST_PROTEIN = 150;
    private static final int MEAL_READY_MINUTES = 20;

    @Test
    public void prepareSuccessViewUpdatesNutritionStateOnly() {
        final NutritionViewModel nutritionViewModel = new NutritionViewModel();
        final RefreshMealRecommendationsPresenter presenter =
                new RefreshMealRecommendationsPresenter(nutritionViewModel);

        final List<MealRecommendationData> meals = new ArrayList<>();
        meals.add(new MealRecommendationData("Chicken and Rice", MEAL_READY_MINUTES, "http://example.com/recipe"));

        final RefreshMealRecommendationsOutputData outputData =
                new RefreshMealRecommendationsOutputData(TEST_BMI, TEST_CALORIES, TEST_PROTEIN, meals);

        presenter.prepareSuccessView(outputData);

        final NutritionState nutritionState = nutritionViewModel.getState();
        assertEquals(TEST_BMI, nutritionState.getBmi(), 0.0001);
        assertEquals(TEST_CALORIES, nutritionState.getDailyCalorieTarget());
        assertEquals(TEST_PROTEIN, nutritionState.getDailyProteinGrams());
        assertEquals(1, nutritionState.getMealRecommendations().size());
        assertEquals("", nutritionState.getMessage());
    }

    @Test
    public void prepareFailViewSetsMessageOnNutritionStateOnly() {
        final NutritionViewModel nutritionViewModel = new NutritionViewModel();
        final RefreshMealRecommendationsPresenter presenter =
                new RefreshMealRecommendationsPresenter(nutritionViewModel);

        presenter.prepareFailView("No user is currently logged in.");

        assertEquals("No user is currently logged in.", nutritionViewModel.getState().getMessage());
    }
}
