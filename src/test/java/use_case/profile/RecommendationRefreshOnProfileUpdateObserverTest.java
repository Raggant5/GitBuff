package use_case.profile;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import use_case.recommendation.RecommendWorkoutPlanInputBoundary;
import use_case.recommendation.RefreshMealRecommendationsInputBoundary;

class RecommendationRefreshOnProfileUpdateObserverTest {

    @Test
    void onProfileUpdatedExecutesBothInteractors() {
        final boolean[] workoutExecuted = {false};
        final boolean[] mealsRefreshed = {false};
        final RecommendWorkoutPlanInputBoundary workoutInteractor = () -> workoutExecuted[0] = true;
        final RefreshMealRecommendationsInputBoundary mealInteractor = () -> mealsRefreshed[0] = true;

        final RecommendationRefreshOnProfileUpdateObserver observer =
                new RecommendationRefreshOnProfileUpdateObserver(workoutInteractor, mealInteractor);

        observer.onProfileUpdated(new ProfileUpdatedEvent("aahir"));

        assertTrue(workoutExecuted[0]);
        assertTrue(mealsRefreshed[0]);
    }
}
