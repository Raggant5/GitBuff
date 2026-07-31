package use_case.recommendation;

/**
 * Input Boundary for generating a personalized workout and nutrition recommendation.
 */
public interface RecommendationInputBoundary {

    /**
     * Executes the recommendation use case for the currently logged-in user.
     */
    void execute();
}
