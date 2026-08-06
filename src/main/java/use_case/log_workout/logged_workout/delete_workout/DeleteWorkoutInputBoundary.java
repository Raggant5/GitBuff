package use_case.log_workout.logged_workout.delete_workout;

/**
 * Input Boundary for actions which are related to deleting a workout from the workout history.
 */
public interface DeleteWorkoutInputBoundary {

    /**
     * Executes the delete workout use case.
     * @param deleteWorkoutInputData the workout id for workout to be deleted
     */
    void execute(DeleteWorkoutInputData deleteWorkoutInputData);
}
