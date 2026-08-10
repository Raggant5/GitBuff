package use_case.recommendation;

/**
 * Output boundary for the Recommend Workout Plan Use Case.
 */
public interface RecommendWorkoutPlanOutputBoundary {

    /**
     * Prepares the success view for the Recommend Workout Plan Use Case.
     *
     * @param outputData the generated workout plan output
     */
    void prepareSuccessView(RecommendWorkoutPlanOutputData outputData);

    /**
     * Prepares the failure view for the Recommend Workout Plan Use Case.
     *
     * @param errorMessage explanation of failure cause
     */
    void prepareFailView(String errorMessage);
}
