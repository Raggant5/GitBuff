package interface_adapter.recommendation;

import use_case.recommendation.RefreshMealRecommendationsInputBoundary;

/**
 * The controller for the Refresh Meal Recommendations Use Case.
 */
public class RefreshMealRecommendationsController {

    private final RefreshMealRecommendationsInputBoundary refreshMealRecommendationsUseCaseInteractor;

    /**
     * Constructs a RefreshMealRecommendationsController instance.
     *
     * @param refreshMealRecommendationsUseCaseInteractor interactor boundary for the refresh logic
     */
    public RefreshMealRecommendationsController(
            final RefreshMealRecommendationsInputBoundary refreshMealRecommendationsUseCaseInteractor) {
        this.refreshMealRecommendationsUseCaseInteractor = refreshMealRecommendationsUseCaseInteractor;
    }

    /**
     * Executes the Refresh Meal Recommendations Use Case for the currently logged-in user.
     */
    public void execute() {
        this.refreshMealRecommendationsUseCaseInteractor.execute();
    }
}
