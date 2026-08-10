package use_case.recommendation;

/**
 * Input boundary for generating a workout plan only - never touches meal recommendations.
 */
public interface RecommendWorkoutPlanInputBoundary {

    /**
     * Executes the recommend-workout-plan use case for the currently logged-in user.
     */
    void execute();
}
