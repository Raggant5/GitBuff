package use_case.recommendation;

/**
 * Output Boundary for the Recommendation Use Case.
 */
public interface RecommendationOutputBoundary {

    /**
     * Prepares the success view for the Recommendation Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(RecommendationOutputData outputData);

    /**
     * Prepares the failure view for the Recommendation Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}
