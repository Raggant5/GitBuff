package interface_adapter.recommendation;

import java.util.ArrayList;
import java.util.List;

import interface_adapter.workouts.RecommendedExerciseDisplayData;
import interface_adapter.workouts.WorkoutPlanDisplayData;
import interface_adapter.workouts.WorkoutsState;
import interface_adapter.workouts.WorkoutsViewModel;
import use_case.recommendation.ExerciseData;
import use_case.recommendation.RecommendWorkoutPlanOutputBoundary;
import use_case.recommendation.RecommendWorkoutPlanOutputData;
import use_case.recommendation.WorkoutPlanData;

/**
 * Presenter for the Recommend Workout Plan Use Case. Updates only the Workouts view model.
 */
public class RecommendWorkoutPlanPresenter implements RecommendWorkoutPlanOutputBoundary {

    private final WorkoutsViewModel workoutsViewModel;

    public RecommendWorkoutPlanPresenter(final WorkoutsViewModel workoutsViewModel) {
        this.workoutsViewModel = workoutsViewModel;
    }

    @Override
    public void prepareSuccessView(final RecommendWorkoutPlanOutputData outputData) {
        final WorkoutsState workoutsState = this.workoutsViewModel.getState();
        workoutsState.setWorkoutFocus(outputData.getWorkoutFocus());
        workoutsState.setActivityLevelDescription(outputData.getActivityLevelDescription());
        final List<WorkoutPlanDisplayData> workoutPlans = new ArrayList<>();
        for (final WorkoutPlanData plan : outputData.getWorkoutPlans()) {
            workoutPlans.add(toDisplayData(plan));
        }
        workoutsState.setWorkoutPlans(workoutPlans);
        workoutsState.setMessage("");
        workoutsState.setLoading(false);
        this.workoutsViewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(final String errorMessage) {
        final WorkoutsState workoutsState = this.workoutsViewModel.getState();
        workoutsState.setMessage(errorMessage);
        workoutsState.setLoading(false);
        this.workoutsViewModel.firePropertyChanged();
    }

    private WorkoutPlanDisplayData toDisplayData(final WorkoutPlanData plan) {
        final List<RecommendedExerciseDisplayData> exercises = new ArrayList<>();
        for (final ExerciseData exercise : plan.getExercises()) {
            exercises.add(toDisplayData(exercise));
        }
        return new WorkoutPlanDisplayData(
                plan.getDate(), plan.getTitle(), plan.getDescription(),
                plan.getEstimatedDurationMinutes(), plan.getEstimatedCaloriesBurned(),
                plan.getEstimatedFatBurnedGrams(), plan.getEstimatedCarbsBurnedGrams(), exercises);
    }

    private RecommendedExerciseDisplayData toDisplayData(final ExerciseData exercise) {
        return new RecommendedExerciseDisplayData(
                exercise.getName(), exercise.getSets(), exercise.getReps(), exercise.getDurationMinutes(),
                exercise.getTargetMuscleGroup(), exercise.getEquipmentRequired(),
                exercise.getInstructions(), exercise.getVideoUrl());
    }
}
