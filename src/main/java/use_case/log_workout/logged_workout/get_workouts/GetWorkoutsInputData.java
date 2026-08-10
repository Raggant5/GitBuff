package use_case.log_workout.logged_workout.get_workouts;

/**
 * Input Data for the Get Workouts Use Case.
 */
public class GetWorkoutsInputData {

    private final String userId;

    public GetWorkoutsInputData(final String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return this.userId;
    }
}
