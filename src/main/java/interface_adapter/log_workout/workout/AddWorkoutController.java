package interface_adapter.log_workout.workout;

import java.time.LocalDate;
import java.util.List;

import entity.ExercisePerformed;
import interface_adapter.login.LoginViewModel;
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
     * @param exercisesToRemove the exercises to be deleted from the list of exercises for the workout
     */
    public void execute(List<ExercisePerformed> exercisesForWorkout, List<ExercisePerformed> exercisesToRemove) {
        exercisesForWorkout.removeAll(exercisesToRemove);
        addWorkoutInteractor.execute(new AddWorkoutInputData(loginViewModel.getState().getUsername(),
                LocalDate.now(), exercisesForWorkout));
    }

}
