package interface_adapter.recommendation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import interface_adapter.workouts.WorkoutsState;
import interface_adapter.workouts.WorkoutsViewModel;
import use_case.recommendation.ExerciseData;
import use_case.recommendation.RecommendWorkoutPlanOutputData;
import use_case.recommendation.WorkoutPlanData;

/**
 * Unit tests for the Recommend Workout Plan Presenter. Only updates the Workouts view model -
 * meal recommendations have their own presenter and their own test.
 */
public class RecommendationPresenterTest {

    private static final int WORKOUT_DURATION_MINUTES = 45;
    private static final int ESTIMATED_CALORIES_BURN = 350;
    private static final int ESTIMATED_FAT_BURN = 15;
    private static final int ESTIMATED_CARBS_BURN = 45;

    @Test
    public void prepareSuccessViewUpdatesWorkoutsState() {
        final WorkoutsViewModel workoutsViewModel = new WorkoutsViewModel();
        workoutsViewModel.getState().setLoading(true);
        final RecommendWorkoutPlanPresenter presenter = new RecommendWorkoutPlanPresenter(workoutsViewModel);

        final List<WorkoutPlanData> workoutPlans = new ArrayList<>();
        workoutPlans.add(new WorkoutPlanData("Monday, Aug 10", "Upper Body", "Chest focus",
                WORKOUT_DURATION_MINUTES, ESTIMATED_CALORIES_BURN, ESTIMATED_FAT_BURN,
                ESTIMATED_CARBS_BURN, new ArrayList<ExerciseData>()));

        final RecommendWorkoutPlanOutputData outputData = new RecommendWorkoutPlanOutputData(
                "Muscle Gain", "Moderately Active", workoutPlans);

        presenter.prepareSuccessView(outputData);

        final WorkoutsState workoutsState = workoutsViewModel.getState();
        assertEquals("Muscle Gain", workoutsState.getWorkoutFocus());
        assertEquals("Moderately Active", workoutsState.getActivityLevelDescription());
        assertEquals(1, workoutsState.getWorkoutPlans().size());
        assertEquals("", workoutsState.getMessage());
        assertFalse(workoutsState.isLoading());
    }

    @Test
    public void prepareFailViewSetsMessageAndStopsLoading() {
        final WorkoutsViewModel workoutsViewModel = new WorkoutsViewModel();
        workoutsViewModel.getState().setLoading(true);
        final RecommendWorkoutPlanPresenter presenter = new RecommendWorkoutPlanPresenter(workoutsViewModel);

        presenter.prepareFailView("No user is currently logged in.");

        assertEquals("No user is currently logged in.", workoutsViewModel.getState().getMessage());
        assertFalse(workoutsViewModel.getState().isLoading());
    }
}
