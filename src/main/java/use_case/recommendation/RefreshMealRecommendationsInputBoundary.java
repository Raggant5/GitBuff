package use_case.recommendation;

/**
 * Input boundary for refreshing a user's meal recommendations without touching their
 * workout plans.
 */
public interface RefreshMealRecommendationsInputBoundary {

    /**
     * Executes the refresh-meal-recommendations use case for the currently logged-in user.
     */
    void execute();
}
