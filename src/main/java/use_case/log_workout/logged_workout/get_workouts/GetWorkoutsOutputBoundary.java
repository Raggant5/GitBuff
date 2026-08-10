package use_case.log_workout.logged_workout.get_workouts;

/**
 * Output boundary for the Get Workouts Use Case.
 */
public interface GetWorkoutsOutputBoundary {

    /**
     * Prepares the success view for the Get Workouts Use Case.
     *
     * @param outputData the user's logged workouts
     */
    void prepareSuccessView(GetWorkoutsOutputData outputData);

    /**
     * Prepares the failure view for the Get Workouts Use Case.
     *
     * @param errorMessage explanation of failure cause
     */
    void prepareFailView(String errorMessage);
}
