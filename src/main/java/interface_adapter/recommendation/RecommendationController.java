package interface_adapter.recommendation;

import use_case.recommendation.RecommendationInputBoundary;

/**
 * The controller for the Recommendation Use Case.
 */
public class RecommendationController {

    private final RecommendationInputBoundary recommendationUseCaseInteractor;

    public RecommendationController(RecommendationInputBoundary recommendationUseCaseInteractor) {
        this.recommendationUseCaseInteractor = recommendationUseCaseInteractor;
    }

    /**
     * Executes the Recommendation Use Case for the currently logged-in user.
     */
    public void execute() {
        recommendationUseCaseInteractor.execute();
    }
}
