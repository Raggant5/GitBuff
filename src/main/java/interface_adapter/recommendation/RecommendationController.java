package interface_adapter.recommendation;

import use_case.recommendation.RecommendationInputBoundary;

/**
 * The controller for the Recommendation Use Case.
 */
public class RecommendationController {

    private final RecommendationInputBoundary recommendationUseCaseInteractor;

    /**
     * Constructs a RecommendationController instance.
     *
     * @param recommendationUseCaseInteractor interactor boundary for recommendation logic.
     */
    public RecommendationController(final RecommendationInputBoundary recommendationUseCaseInteractor) {
        this.recommendationUseCaseInteractor = recommendationUseCaseInteractor;
    }

    /**
     * Executes the Recommendation Use Case for the currently logged-in user.
     */
    public void execute() {
        this.recommendationUseCaseInteractor.execute();
    }
}
