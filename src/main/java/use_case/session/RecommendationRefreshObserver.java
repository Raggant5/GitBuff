package use_case.session;

import use_case.recommendation.RecommendWorkoutPlanInputBoundary;
import use_case.recommendation.RefreshMealRecommendationsInputBoundary;

/**
 * Triggers a background refresh of the AI workout plan and meal recommendations after login.
 */
public class RecommendationRefreshObserver implements UserSessionObserver {

    private final RecommendWorkoutPlanInputBoundary recommendWorkoutPlanInteractor;
    private final RefreshMealRecommendationsInputBoundary refreshMealRecommendationsInteractor;

    public RecommendationRefreshObserver(
            final RecommendWorkoutPlanInputBoundary recommendWorkoutPlanInteractor,
            final RefreshMealRecommendationsInputBoundary refreshMealRecommendationsInteractor) {
        this.recommendWorkoutPlanInteractor = recommendWorkoutPlanInteractor;
        this.refreshMealRecommendationsInteractor = refreshMealRecommendationsInteractor;
    }

    @Override
    public void onUserLoggedIn(final UserLoggedInEvent event) {
        final Thread worker = new Thread(() -> {
            this.recommendWorkoutPlanInteractor.execute();
            this.refreshMealRecommendationsInteractor.execute();
        });
        worker.setDaemon(true);
        worker.start();
    }
}
