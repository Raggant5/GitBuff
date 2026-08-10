package use_case.log_workout.logged_workout.get_workouts;

/**
 * Input boundary for fetching a user's logged workout history on demand (e.g. when the Workout
 * History tab is opened), rather than Login eagerly fetching it for every user at login.
 */
public interface GetWorkoutsInputBoundary {

    /**
     * Executes the get-workouts use case.
     *
     * @param inputData the user to fetch workouts for
     */
    void execute(GetWorkoutsInputData inputData);
}
