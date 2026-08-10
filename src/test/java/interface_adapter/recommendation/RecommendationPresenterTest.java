package interface_adapter.recommendation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import entity.MealRecommendation;
import entity.WorkoutPlan;
import interface_adapter.nutrition.NutritionState;
import interface_adapter.nutrition.NutritionViewModel;
import interface_adapter.workouts.WorkoutsState;
import interface_adapter.workouts.WorkoutsViewModel;
import use_case.recommendation.RecommendationOutputData;

/**
 * Unit tests for the Recommendation Presenter. Uses a null CalendarController, matching the
 * presenter's own null-guard, so these tests don't need to wire up the calendar feature's
 * unrelated dependency graph.
 */
public class RecommendationPresenterTest {

    private static final double TEST_BMI = 24.7;
    private static final int TEST_CALORIES = 2800;
    private static final int TEST_PROTEIN = 150;
    private static final int MEAL_READY_MINUTES = 20;
    private static final int WORKOUT_DURATION_MINUTES = 45;
    private static final int ESTIMATED_CALORIES_BURN = 350;
    private static final int ESTIMATED_FAT_BURN = 15;
    private static final int ESTIMATED_CARBS_BURN = 45;

    @Test
    public void prepareSuccessViewUpdatesNutritionAndWorkoutsState() {
        final NutritionViewModel nutritionViewModel = new NutritionViewModel();
        final WorkoutsViewModel workoutsViewModel = new WorkoutsViewModel();
        workoutsViewModel.getState().setLoading(true);
        final RecommendationPresenter presenter =
                new RecommendationPresenter(nutritionViewModel, workoutsViewModel, null);

        final List<WorkoutPlan> workoutPlans = new ArrayList<>();
        workoutPlans.add(new WorkoutPlan("Monday, Aug 10", "Upper Body", "Chest focus",
                "STRENGTH", "UPPER_BODY", "MEDIUM", "CHEST", "BODYWEIGHT",
                WORKOUT_DURATION_MINUTES, ESTIMATED_CALORIES_BURN, ESTIMATED_FAT_BURN,
                ESTIMATED_CARBS_BURN, new ArrayList<>()));

        final List<MealRecommendation> meals = new ArrayList<>();
        meals.add(new MealRecommendation("Chicken and Rice", MEAL_READY_MINUTES, "http://example.com/recipe"));

        final RecommendationOutputData outputData = new RecommendationOutputData(
                TEST_BMI, TEST_CALORIES, TEST_PROTEIN, "Muscle Gain",
                "Moderately Active", workoutPlans, meals);

        presenter.prepareSuccessView(outputData);

        final NutritionState nutritionState = nutritionViewModel.getState();
        assertEquals(TEST_BMI, nutritionState.getBmi(), 0.0001);
        assertEquals(TEST_CALORIES, nutritionState.getDailyCalorieTarget());
        assertEquals(TEST_PROTEIN, nutritionState.getDailyProteinGrams());
        assertEquals(1, nutritionState.getMealRecommendations().size());
        assertEquals("", nutritionState.getMessage());

        final WorkoutsState workoutsState = workoutsViewModel.getState();
        assertEquals("Muscle Gain", workoutsState.getWorkoutFocus());
        assertEquals("Moderately Active", workoutsState.getActivityLevelDescription());
        assertEquals(1, workoutsState.getWorkoutPlans().size());
        assertEquals("", workoutsState.getMessage());
        assertFalse(workoutsState.isLoading());
    }

    @Test
    public void prepareFailViewSetsErrorMessageOnBothStatesAndStopsLoading() {
        final NutritionViewModel nutritionViewModel = new NutritionViewModel();
        final WorkoutsViewModel workoutsViewModel = new WorkoutsViewModel();
        workoutsViewModel.getState().setLoading(true);
        final RecommendationPresenter presenter =
                new RecommendationPresenter(nutritionViewModel, workoutsViewModel, null);

        presenter.prepareFailView("No user is currently logged in.");

        assertEquals("No user is currently logged in.", nutritionViewModel.getState().getMessage());
        assertEquals("No user is currently logged in.", workoutsViewModel.getState().getMessage());
        assertFalse(workoutsViewModel.getState().isLoading());
    }
}
