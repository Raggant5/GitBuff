package interface_adapter.log_workout.workout;

import use_case.log_workout.logged_workout.prepare_edit_workout.PrepareEditWorkoutInputBoundary;
import use_case.log_workout.logged_workout.prepare_edit_workout.PrepareEditWorkoutInputData;

public class PrepareEditWorkoutController {
    private final PrepareEditWorkoutInputBoundary prepareEditWorkoutInteractor;

    public PrepareEditWorkoutController(PrepareEditWorkoutInputBoundary prepareEditWorkoutInteractor) {
        this.prepareEditWorkoutInteractor = prepareEditWorkoutInteractor;
    }

    /**
     * Executes the Prepare Edit Workout Use Case.
     * @param workoutId the id of the workout to edit
     */
    public void execute(int workoutId) {
        prepareEditWorkoutInteractor.execute(new PrepareEditWorkoutInputData(workoutId));
    }
}
