package use_case.session;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.Test;

import entity.ActivityLevel;
import entity.FitnessGoal;
import entity.Gender;
import entity.UnitSystem;
import use_case.dashboard.DashboardInputBoundary;
import use_case.login.LoginOutputData;

class DashboardRefreshObserverTest {

    private static final float HEIGHT = 1.8f;
    private static final float WEIGHT = 80f;
    private static final int DURATION_MINUTES = 45;

    @Test
    void onUserLoggedInRefreshesDashboardForUsername() {
        final String[] receivedUserId = {null};
        final DashboardInputBoundary dashboardInteractor = userId -> receivedUserId[0] = userId;
        final DashboardRefreshObserver observer = new DashboardRefreshObserver(dashboardInteractor);
        final LoginOutputData loginData = new LoginOutputData("aahir", HEIGHT, WEIGHT, ActivityLevel.VERY_ACTIVE,
                FitnessGoal.MUSCLE_AND_STRENGTH_GAIN, "/tmp/pic.png", LocalDate.of(2000, 1, 1), Gender.MALE,
                "bio", UnitSystem.METRIC, Set.of(), Set.of(), Set.of(), DURATION_MINUTES, Set.of(), false);

        observer.onUserLoggedIn(new UserLoggedInEvent(loginData));

        assertEquals("aahir", receivedUserId[0]);
    }
}
