package use_case.session;

import static org.junit.jupiter.api.Assertions.assertSame;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.Test;

import entity.ActivityLevel;
import entity.FitnessGoal;
import entity.Gender;
import entity.UnitSystem;
import use_case.login.LoginOutputData;

class UserSessionObserverTest {

    private static final float HEIGHT = 1.8f;
    private static final float WEIGHT = 80f;
    private static final int DURATION_MINUTES = 45;

    @Test
    void acceptDelegatesToOnUserLoggedIn() {
        final UserLoggedInEvent[] received = {null};
        final UserSessionObserver observer = event -> received[0] = event;
        final LoginOutputData loginData = new LoginOutputData("aahir", HEIGHT, WEIGHT, ActivityLevel.VERY_ACTIVE,
                FitnessGoal.MUSCLE_AND_STRENGTH_GAIN, "/tmp/pic.png", LocalDate.of(2000, 1, 1), Gender.MALE,
                "bio", UnitSystem.METRIC, Set.of(), Set.of(), Set.of(), DURATION_MINUTES, Set.of(), false);
        final UserLoggedInEvent event = new UserLoggedInEvent(loginData);

        observer.accept(event);

        assertSame(event, received[0]);
    }
}
