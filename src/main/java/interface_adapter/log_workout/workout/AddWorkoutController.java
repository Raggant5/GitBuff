package interface_adapter.log_workout.workout;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import interface_adapter.log_workout.exercise.ExercisePerformedDisplayData;
import interface_adapter.login.LoginViewModel;
import use_case.log_workout.StrengthDetailsInput;
import use_case.log_workout.exercise_performed.ExercisePerformedInputData;
import use_case.log_workout.logged_workout.add_workout.AddWorkoutInputBoundary;
import use_case.log_workout.logged_workout.add_workout.AddWorkoutInputData;

public class AddWorkoutController {

    private final AddWorkoutInputBoundary addWorkoutInteractor;
    private final LoginViewModel loginViewModel;

    public AddWorkoutController(AddWorkoutInputBoundary addWorkoutInteractor, LoginViewModel loginViewModel) {
        this.addWorkoutInteractor = addWorkoutInteractor;
        this.loginViewModel = loginViewModel;
    }

    /**
     * Executes the Add Workout Use Case.
     * @param exercisesForWorkout every exercise associated with the workout
     */
    public void execute(List<ExercisePerformedDisplayData> exercisesForWorkout) {
        final List<ExercisePerformedInputData> exercises = new ArrayList<>();
        for (ExercisePerformedDisplayData exercise : exercisesForWorkout) {
            final StrengthDetailsInput strengthDetailsInput = new StrengthDetailsInput(exercise.getSets(),
                    exercise.getReps(), exercise.getWeight());
            exercises.add(new ExercisePerformedInputData(exercise.getId(), exercise.getExerciseName(),
                    strengthDetailsInput, exercise.getDurationMins(), exercise.getDistanceKm(),
                    exercise.getIsCardio()));
        }
        addWorkoutInteractor.execute(new AddWorkoutInputData(loginViewModel.getState().getUsername(),
                LocalDate.now(), exercises));
    }

}
