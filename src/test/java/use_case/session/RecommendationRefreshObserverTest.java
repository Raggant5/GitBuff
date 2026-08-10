package use_case.session;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDate;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import entity.ActivityLevel;
import entity.FitnessGoal;
import entity.Gender;
import entity.UnitSystem;
import use_case.login.LoginOutputData;
import use_case.recommendation.RecommendWorkoutPlanInputBoundary;
import use_case.recommendation.RefreshMealRecommendationsInputBoundary;

class RecommendationRefreshObserverTest {

    private static final int AWAIT_SECONDS = 5;
    private static final float HEIGHT = 1.8f;
    private static final float WEIGHT = 80f;
    private static final int DURATION_MINUTES = 45;

    @Test
    void onUserLoggedInExecutesBothInteractorsInBackground() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(2);
        final boolean[] workoutExecuted = {false};
        final boolean[] mealsRefreshed = {false};

        final RecommendWorkoutPlanInputBoundary workoutInteractor = () -> {
            workoutExecuted[0] = true;
            latch.countDown();
        };
        final RefreshMealRecommendationsInputBoundary mealInteractor = () -> {
            mealsRefreshed[0] = true;
            latch.countDown();
        };

        final RecommendationRefreshObserver observer =
                new RecommendationRefreshObserver(workoutInteractor, mealInteractor);
        final LoginOutputData loginData = new LoginOutputData("aahir", HEIGHT, WEIGHT, ActivityLevel.VERY_ACTIVE,
                FitnessGoal.MUSCLE_AND_STRENGTH_GAIN, "/tmp/pic.png", LocalDate.of(2000, 1, 1), Gender.MALE,
                "bio", UnitSystem.METRIC, Set.of(), Set.of(), Set.of(), DURATION_MINUTES, Set.of(), false);

        observer.onUserLoggedIn(new UserLoggedInEvent(loginData));

        if (!latch.await(AWAIT_SECONDS, TimeUnit.SECONDS)) {
            fail("Expected both interactors to run within " + AWAIT_SECONDS + " seconds.");
        }
        assertTrue(workoutExecuted[0]);
        assertTrue(mealsRefreshed[0]);
    }
}
