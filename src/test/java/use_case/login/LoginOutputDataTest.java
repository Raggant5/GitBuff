package use_case.login;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.Test;

import entity.ActivityLevel;
import entity.FitnessGoal;
import entity.Gender;
import entity.UnitSystem;

class LoginOutputDataTest {

    private static final float HEIGHT = 1.8f;
    private static final float WEIGHT = 80f;
    private static final int DURATION_MINUTES = 45;

    @Test
    void isUseCaseFailedReflectsConstructedFlag() {
        final LoginOutputData failed = new LoginOutputData("aahir", HEIGHT, WEIGHT, ActivityLevel.VERY_ACTIVE,
                FitnessGoal.MUSCLE_AND_STRENGTH_GAIN, "/tmp/pic.png", LocalDate.of(2000, 1, 1), Gender.MALE,
                "bio", UnitSystem.METRIC, Set.of(), Set.of(), Set.of(), DURATION_MINUTES, Set.of(), true);
        final LoginOutputData succeeded = new LoginOutputData("aahir", HEIGHT, WEIGHT, ActivityLevel.VERY_ACTIVE,
                FitnessGoal.MUSCLE_AND_STRENGTH_GAIN, "/tmp/pic.png", LocalDate.of(2000, 1, 1), Gender.MALE,
                "bio", UnitSystem.METRIC, Set.of(), Set.of(), Set.of(), DURATION_MINUTES, Set.of(), false);

        assertTrue(failed.isUseCaseFailed());
        assertFalse(succeeded.isUseCaseFailed());
    }
}
