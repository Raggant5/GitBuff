package use_case.profile;

import use_case.recommendation.RecommendWorkoutPlanInputBoundary;
import use_case.recommendation.RefreshMealRecommendationsInputBoundary;

/**
 * Regenerates the workout plan and meal recommendations whenever a profile is saved - a
 * weight/goal/activity-level change affects both.
 */
public class RecommendationRefreshOnProfileUpdateObserver implements ProfileUpdatedObserver {

    private final RecommendWorkoutPlanInputBoundary recommendWorkoutPlanInteractor;
    private final RefreshMealRecommendationsInputBoundary refreshMealRecommendationsInteractor;

    public RecommendationRefreshOnProfileUpdateObserver(
            final RecommendWorkoutPlanInputBoundary recommendWorkoutPlanInteractor,
            final RefreshMealRecommendationsInputBoundary refreshMealRecommendationsInteractor) {
        this.recommendWorkoutPlanInteractor = recommendWorkoutPlanInteractor;
        this.refreshMealRecommendationsInteractor = refreshMealRecommendationsInteractor;
    }

    @Override
    public void onProfileUpdated(final ProfileUpdatedEvent event) {
        this.recommendWorkoutPlanInteractor.execute();
        this.refreshMealRecommendationsInteractor.execute();
    }
}
