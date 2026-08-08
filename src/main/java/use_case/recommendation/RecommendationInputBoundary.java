package use_case.recommendation;

/**
 * Input boundary for recommendation use cases.
 */
public interface RecommendationInputBoundary {

    /**
     * Executes full workout and meal recommendations.
     */
    void execute();

    /**
     * Executes meal recommendations only without regenerating workout plans.
     */
    void executeMealRecommendationsOnly();
}
