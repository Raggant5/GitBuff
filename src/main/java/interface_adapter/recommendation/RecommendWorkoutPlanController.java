package interface_adapter.recommendation;

import use_case.recommendation.RecommendWorkoutPlanInputBoundary;

/**
 * The controller for the Recommend Workout Plan Use Case. Used only by view-triggered actions
 * (e.g. view.WorkoutsView's refresh button) - login-triggered refresh goes straight through
 * RecommendWorkoutPlanInputBoundary from an observer, not through this controller.
 */
public class RecommendWorkoutPlanController {

    private final RecommendWorkoutPlanInputBoundary recommendWorkoutPlanInteractor;

    public RecommendWorkoutPlanController(final RecommendWorkoutPlanInputBoundary recommendWorkoutPlanInteractor) {
        this.recommendWorkoutPlanInteractor = recommendWorkoutPlanInteractor;
    }

    /**
     * Executes the Recommend Workout Plan Use Case for the currently logged-in user.
     */
    public void execute() {
        this.recommendWorkoutPlanInteractor.execute();
    }
}
