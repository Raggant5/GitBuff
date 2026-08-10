package interface_adapter.session;

import java.util.ArrayList;
import java.util.List;

import entity.ExercisePerformed;
import entity.LoggedWorkout;
import interface_adapter.log_workout.exercise.ExercisePerformedDisplayData;
import interface_adapter.log_workout.exercise.StrengthDetailsDisplayData;
import interface_adapter.log_workout.workout.LoggedWorkoutDisplayData;
import interface_adapter.log_workout.workout.ViewWorkoutsViewModel;

/**
 * Reloads the logged-in user's completed workout history into the workout-history view.
 *
 * <p>Extracted from {@code interface_adapter.login.LoginPresenter}, which previously did this
 * inline as one of several unrelated responsibilities crammed into one method.
 */
public class WorkoutHistorySessionObserver implements UserSessionObserver {

    private final ViewWorkoutsViewModel viewWorkoutsViewModel;

    /**
     * Constructs a WorkoutHistorySessionObserver instance.
     *
     * @param viewWorkoutsViewModel view model for the user's workout history
     */
    public WorkoutHistorySessionObserver(final ViewWorkoutsViewModel viewWorkoutsViewModel) {
        this.viewWorkoutsViewModel = viewWorkoutsViewModel;
    }

    @Override
    public void onUserLoggedIn(final UserLoggedInEvent event) {
        final List<LoggedWorkoutDisplayData> workouts = new ArrayList<>();
        for (final LoggedWorkout workout : event.data().getWorkouts()) {
            final List<ExercisePerformedDisplayData> exercises = new ArrayList<>();
            for (final ExercisePerformed exercise : workout.getExercises()) {
                final StrengthDetailsDisplayData strengthDetailsDisplayData = new StrengthDetailsDisplayData(
                        exercise.getSets(), exercise.getReps(), exercise.getWeight());
                exercises.add(new ExercisePerformedDisplayData(exercise.getId(), exercise.getExerciseName(),
                        strengthDetailsDisplayData, exercise.getDurationMins(), exercise.getDistanceKm(),
                        exercise.getIsCardio()));
            }
            workouts.add(new LoggedWorkoutDisplayData(workout.getId(), workout.getDate(), exercises));
        }

        this.viewWorkoutsViewModel.getState().setWorkouts(workouts);
        this.viewWorkoutsViewModel.firePropertyChanged();
    }
}

