package interface_adapter.log_workout.workout;

import entity.LoggedWorkout;
import use_case.log_workout.logged_workout.prepare_edit_workout.PrepareEditWorkoutInputBoundary;
import use_case.log_workout.logged_workout.prepare_edit_workout.PrepareEditWorkoutInputData;

public class PrepareEditWorkoutController {

    private final PrepareEditWorkoutInputBoundary prepareEditWorkoutInteractor;

    public PrepareEditWorkoutController(PrepareEditWorkoutInputBoundary prepareEditWorkoutInteractor) {
        this.prepareEditWorkoutInteractor = prepareEditWorkoutInteractor;
    }

    /**
     * Executes the Prepare Edit Workout Use Case.
     * @param workout the workout to be edited
     */
    public void execute(LoggedWorkout workout) {
        prepareEditWorkoutInteractor.execute(new PrepareEditWorkoutInputData(workout));
    }

}
