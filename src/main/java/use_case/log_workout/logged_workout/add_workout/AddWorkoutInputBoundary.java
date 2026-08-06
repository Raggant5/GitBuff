package use_case.log_workout.logged_workout.add_workout;

/**
 * Input Boundary for actions which are related to adding a workout logged.
 */
public interface AddWorkoutInputBoundary {

    /**
     * Executes the add workout use case.
     * @param addWorkoutInputData the input data
     */
    void execute(AddWorkoutInputData addWorkoutInputData);
}
