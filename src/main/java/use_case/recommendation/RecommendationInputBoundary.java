package use_case.recommendation;

/**
 * Input Boundary for generating a personalized workout and nutrition recommendation
 * for the currently logged-in user.
 */
public interface RecommendationInputBoundary {

    /**
     * Executes the recommendation use case for the currently logged-in user.
     */
    void execute();
}
