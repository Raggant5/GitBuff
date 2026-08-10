package interface_adapter.log_workout.workout;

import java.util.ArrayList;
import java.util.List;

import interface_adapter.log_workout.exercise.ExercisePerformedDisplayData;
import interface_adapter.log_workout.exercise.StrengthDetailsDisplayData;
import use_case.log_workout.exercise_performed.ExercisePerformedData;
import use_case.log_workout.logged_workout.LoggedWorkoutData;
import use_case.log_workout.logged_workout.get_workouts.GetWorkoutsOutputBoundary;
import use_case.log_workout.logged_workout.get_workouts.GetWorkoutsOutputData;

/**
 * Presenter for the Get Workouts Use Case.
 */
public class GetWorkoutsPresenter implements GetWorkoutsOutputBoundary {

    private final ViewWorkoutsViewModel viewWorkoutsViewModel;

    public GetWorkoutsPresenter(final ViewWorkoutsViewModel viewWorkoutsViewModel) {
        this.viewWorkoutsViewModel = viewWorkoutsViewModel;
    }

    @Override
    public void prepareSuccessView(final GetWorkoutsOutputData outputData) {
        final ViewWorkoutsState state = this.viewWorkoutsViewModel.getState();
        state.setWorkouts(toDisplayData(outputData.getWorkouts()));
        state.setError("");
        this.viewWorkoutsViewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(final String errorMessage) {
        final ViewWorkoutsState state = this.viewWorkoutsViewModel.getState();
        state.setError(errorMessage);
        this.viewWorkoutsViewModel.firePropertyChanged();
    }

    private List<LoggedWorkoutDisplayData> toDisplayData(final List<LoggedWorkoutData> workouts) {
        final List<LoggedWorkoutDisplayData> result = new ArrayList<>();
        for (final LoggedWorkoutData workout : workouts) {
            result.add(toDisplayData(workout));
        }
        return result;
    }

    private LoggedWorkoutDisplayData toDisplayData(final LoggedWorkoutData workout) {
        final List<ExercisePerformedDisplayData> exercises = new ArrayList<>();
        for (final ExercisePerformedData exercise : workout.getExercises()) {
            exercises.add(toDisplayData(exercise));
        }
        return new LoggedWorkoutDisplayData(workout.getId(), workout.getDate(), exercises);
    }

    private ExercisePerformedDisplayData toDisplayData(final ExercisePerformedData exercise) {
        final StrengthDetailsDisplayData strengthDetailsDisplayData = new StrengthDetailsDisplayData(
                exercise.getSets(), exercise.getReps(), exercise.getWeight());
        return new ExercisePerformedDisplayData(exercise.getId(), exercise.getExerciseName(),
                strengthDetailsDisplayData, exercise.getDurationMins(), exercise.getDistanceKm(),
                exercise.getIsCardio());
    }
}
