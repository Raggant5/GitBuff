package interface_adapter.log_workout.workout;

import use_case.log_workout.logged_workout.get_workouts.GetWorkoutsInputBoundary;
import use_case.log_workout.logged_workout.get_workouts.GetWorkoutsInputData;

/**
 * Controller for the Get Workouts Use Case. Called when the user navigates to the Workout
 * History tab, so workouts load on demand instead of being fetched eagerly for every user at
 * login.
 */
public class GetWorkoutsController {

    private final GetWorkoutsInputBoundary getWorkoutsInteractor;

    public GetWorkoutsController(final GetWorkoutsInputBoundary getWorkoutsInteractor) {
        this.getWorkoutsInteractor = getWorkoutsInteractor;
    }

    /**
     * Executes the Get Workouts Use Case for the currently logged-in user.
     *
     * @param userId the currently logged-in user's id
     */
    public void execute(final String userId) {
        if (userId != null && !userId.isBlank()) {
            this.getWorkoutsInteractor.execute(new GetWorkoutsInputData(userId));
        }
    }
}
