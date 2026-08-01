package use_case.recommendation;

/**
 * Output Boundary for the Recommendation Use Case.
 */
public interface RecommendationOutputBoundary {

    /**
     * Prepares the success view for the Recommendation Use Case.
     *
     * @param outputData output data containing calculation results
     */
    void prepareSuccessView(RecommendationOutputData outputData);

    /**
     * Prepares the failure view for the Recommendation Use Case.
     *
     * @param errorMessage explanation of failure cause
     */
    void prepareFailView(String errorMessage);
}
