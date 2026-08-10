package use_case.recommendation;

/**
 * Output boundary for the Refresh Meal Recommendations Use Case.
 */
public interface RefreshMealRecommendationsOutputBoundary {

    /**
     * Prepares the success view for the Refresh Meal Recommendations Use Case.
     *
     * @param outputData output data containing the refreshed meal recommendations
     */
    void prepareSuccessView(RefreshMealRecommendationsOutputData outputData);

    /**
     * Prepares the failure view for the Refresh Meal Recommendations Use Case.
     *
     * @param errorMessage explanation of failure cause
     */
    void prepareFailView(String errorMessage);
}
