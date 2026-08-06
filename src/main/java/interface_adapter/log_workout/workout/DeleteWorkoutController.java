package interface_adapter.log_workout.workout;

import use_case.log_workout.logged_workout.delete_workout.DeleteWorkoutInputBoundary;
import use_case.log_workout.logged_workout.delete_workout.DeleteWorkoutInputData;

public class DeleteWorkoutController {
    private final DeleteWorkoutInputBoundary deleteWorkoutInputInteractor;

    public DeleteWorkoutController(DeleteWorkoutInputBoundary deleteWorkoutInputInteractor) {
        this.deleteWorkoutInputInteractor = deleteWorkoutInputInteractor;
    }

    /**
     * Executes the Delete Workout Use Case.
     * @param workoutId workout to delete
     */
    public void execute(int workoutId) {
        deleteWorkoutInputInteractor.execute(new DeleteWorkoutInputData(workoutId));
    }

}
