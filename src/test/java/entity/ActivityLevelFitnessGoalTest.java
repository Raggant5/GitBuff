package entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ActivityLevelFitnessGoalTest {

    @Test
    public void moreActiveLevelsHaveHigherCalorieMultipliers() {
        assertTrue(ActivityLevel.NOT_ACTIVE.getCalorieMultiplier()
                < ActivityLevel.LIGHTLY_ACTIVE.getCalorieMultiplier());
        assertTrue(ActivityLevel.LIGHTLY_ACTIVE.getCalorieMultiplier()
                < ActivityLevel.MODERATELY_ACTIVE.getCalorieMultiplier());
        assertTrue(ActivityLevel.MODERATELY_ACTIVE.getCalorieMultiplier()
                < ActivityLevel.VERY_ACTIVE.getCalorieMultiplier());
        assertTrue(ActivityLevel.VERY_ACTIVE.getCalorieMultiplier()
                < ActivityLevel.EXTRA_ACTIVE.getCalorieMultiplier());
    }

    @Test
    public void loseWeightGoalHasNegativeCalorieAdjustment() {
        assertTrue(FitnessGoal.LOSE_WEIGHT.getDailyCalorieAdjustment() < 0);
    }

    @Test
    public void gainMuscleGoalHasPositiveCalorieAdjustment() {
        assertTrue(FitnessGoal.MUSCLE_AND_STRENGTH_GAIN.getDailyCalorieAdjustment() > 0);
    }

    @Test
    public void maintainGoalHasNoCalorieAdjustment() {
        assertEquals(0, FitnessGoal.MAINTAIN_GENERAL_FITNESS.getDailyCalorieAdjustment());
    }
}
