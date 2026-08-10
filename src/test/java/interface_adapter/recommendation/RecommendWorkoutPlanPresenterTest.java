package interface_adapter.recommendation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import interface_adapter.workouts.WorkoutPlanDisplayData;
import interface_adapter.workouts.WorkoutsState;
import interface_adapter.workouts.WorkoutsViewModel;
import use_case.recommendation.ExerciseData;
import use_case.recommendation.RecommendWorkoutPlanOutputData;
import use_case.recommendation.WorkoutPlanData;

class RecommendWorkoutPlanPresenterTest {

    private static final int SETS = 3;
    private static final int REPS = 10;
    private static final int DURATION_MINUTES = 45;
    private static final int CALORIES = 300;
    private static final int FAT_GRAMS = 10;
    private static final int CARBS_GRAMS = 40;

    @Test
    void prepareSuccessViewConvertsPlansAndExercisesAndFiresChange() {
        final WorkoutsViewModel viewModel = new WorkoutsViewModel();
        final RecommendWorkoutPlanPresenter presenter = new RecommendWorkoutPlanPresenter(viewModel);

        final ExerciseData exercise = new ExerciseData("Push-Ups", SETS, REPS, DURATION_MINUTES,
                "Chest", "Bodyweight", "Lower chest to ground.", "http://example.com/video");
        final List<ExerciseData> exercises = new ArrayList<>();
        exercises.add(exercise);
        final WorkoutPlanData plan = new WorkoutPlanData("Monday, Aug 3", "Upper Body", "Chest focus",
                DURATION_MINUTES, CALORIES, FAT_GRAMS, CARBS_GRAMS, exercises);
        final List<WorkoutPlanData> plans = new ArrayList<>();
        plans.add(plan);

        presenter.prepareSuccessView(new RecommendWorkoutPlanOutputData("Muscle Gain", "Very Active", plans));

        final WorkoutsState state = viewModel.getState();
        assertEquals("Muscle Gain", state.getWorkoutFocus());
        assertEquals("Very Active", state.getActivityLevelDescription());
        assertEquals("", state.getMessage());
        assertFalse(state.isLoading());
        assertEquals(1, state.getWorkoutPlans().size());

        final WorkoutPlanDisplayData displayPlan = state.getWorkoutPlans().get(0);
        assertEquals("Upper Body", displayPlan.getTitle());
        assertEquals(1, displayPlan.getExercises().size());
        assertEquals("Push-Ups", displayPlan.getExercises().get(0).getName());
        assertEquals(SETS, displayPlan.getExercises().get(0).getSets());
        assertEquals("http://example.com/video", displayPlan.getExercises().get(0).getVideoUrl());
    }

    @Test
    void prepareFailViewSetsMessageAndStopsLoading() {
        final WorkoutsViewModel viewModel = new WorkoutsViewModel();
        viewModel.getState().setLoading(true);
        final RecommendWorkoutPlanPresenter presenter = new RecommendWorkoutPlanPresenter(viewModel);

        presenter.prepareFailView("Something went wrong");

        assertEquals("Something went wrong", viewModel.getState().getMessage());
        assertFalse(viewModel.getState().isLoading());
    }
}
