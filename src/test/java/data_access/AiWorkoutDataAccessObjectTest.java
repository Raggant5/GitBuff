package data_access;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import entity.ActivityLevel;
import entity.CommonUser;
import entity.Exercise;
import entity.FitnessGoal;
import entity.User;
import entity.WorkoutPlan;

/**
 * Unit tests for the AI Workout Data Access Object's fallback plan generation.
 *
 * <p>These tests do not exercise the live Gemini API call, since no API key is supplied.
 * generateWorkoutPlans falls back to deterministic local generation whenever the key is
 * missing, so these tests never touch the network.
 */
public class AiWorkoutDataAccessObjectTest {

    private static final float TEST_HEIGHT = 1.8f;
    private static final float TEST_WEIGHT = 80f;
    private static final int CUSTOM_DURATION_MINUTES = 60;
    private static final int WEEK_DAYS = 7;
    private static final int DEFAULT_PREFERRED_DAY_COUNT = 3;

    private static User buildUser() {
        final User user = new CommonUser("aahir", "password");
        user.setHeight(TEST_HEIGHT);
        user.setWeight(TEST_WEIGHT);
        user.setActivityLevel(ActivityLevel.MODERATELY_ACTIVE);
        user.setGoal(FitnessGoal.MUSCLE_AND_STRENGTH_GAIN);
        return user;
    }

    private static int countActiveDays(final List<WorkoutPlan> plans) {
        int activeDayCount = 0;
        for (final WorkoutPlan plan : plans) {
            if (!"Rest & Recovery".equals(plan.getTitle())) {
                activeDayCount++;
            }
        }
        return activeDayCount;
    }

    @Test
    public void generateWorkoutPlansWithoutApiKeyUsesFallback() {
        final AiWorkoutDataAccessObject dataAccessObject = new AiWorkoutDataAccessObject("");
        final User user = buildUser();

        final List<WorkoutPlan> plans = dataAccessObject.generateWorkoutPlans(user, WEEK_DAYS);

        assertEquals(WEEK_DAYS, plans.size());
        for (final WorkoutPlan plan : plans) {
            assertNotNull(plan.getTitle());
            assertFalse(plan.getTitle().isEmpty());
        }
    }

    @Test
    public void generateWorkoutPlansHonoursPreferredWorkoutDays() {
        final AiWorkoutDataAccessObject dataAccessObject = new AiWorkoutDataAccessObject("");
        final User user = buildUser();
        final Set<DayOfWeek> preferredDays = new HashSet<>();
        preferredDays.add(DayOfWeek.MONDAY);
        user.setPreferredWorkoutDays(preferredDays);

        final List<WorkoutPlan> plans = dataAccessObject.generateWorkoutPlans(user, WEEK_DAYS);

        assertEquals(1, countActiveDays(plans));
        assertEquals(WEEK_DAYS - 1, plans.size() - countActiveDays(plans));
    }

    @Test
    public void generateWorkoutPlansUsesPreferredDurationWhenSet() {
        final AiWorkoutDataAccessObject dataAccessObject = new AiWorkoutDataAccessObject("");
        final User user = buildUser();
        final Set<DayOfWeek> preferredDays = new HashSet<>();
        preferredDays.add(DayOfWeek.MONDAY);
        user.setPreferredWorkoutDays(preferredDays);
        user.setPreferredWorkoutDurationMinutes(CUSTOM_DURATION_MINUTES);

        final List<WorkoutPlan> plans = dataAccessObject.generateWorkoutPlans(user, WEEK_DAYS);

        final WorkoutPlan activePlan = plans.stream()
                .filter(plan -> !"Rest & Recovery".equals(plan.getTitle()))
                .findFirst()
                .orElseThrow();
        assertEquals(CUSTOM_DURATION_MINUTES, activePlan.getEstimatedDurationMinutes());
    }

    @Test
    public void generateWorkoutPlansWithNullUserFallsBackToDefaultPreferredDays() {
        final AiWorkoutDataAccessObject dataAccessObject = new AiWorkoutDataAccessObject("");

        final List<WorkoutPlan> plans = dataAccessObject.generateWorkoutPlans(null, WEEK_DAYS);

        assertEquals(WEEK_DAYS, plans.size());
        assertEquals(DEFAULT_PREFERRED_DAY_COUNT, countActiveDays(plans));
    }

    @Test
    public void generateWorkoutPlansSingleArgOverloadDefaultsToOneWeek() {
        final AiWorkoutDataAccessObject dataAccessObject = new AiWorkoutDataAccessObject("");
        final User user = buildUser();

        final List<WorkoutPlan> plans = dataAccessObject.generateWorkoutPlans(user);

        assertEquals(WEEK_DAYS, plans.size());
    }

    @Test
    public void generateWorkoutPlansRestrictsExercisesToOwnedEquipment() {
        final AiWorkoutDataAccessObject dataAccessObject = new AiWorkoutDataAccessObject("");
        final User user = buildUser();
        final Set<DayOfWeek> preferredDays = new HashSet<>();
        preferredDays.add(DayOfWeek.MONDAY);
        user.setPreferredWorkoutDays(preferredDays);
        user.setEquipment(new HashSet<>());

        final List<WorkoutPlan> plans = dataAccessObject.generateWorkoutPlans(user, WEEK_DAYS);

        final WorkoutPlan activePlan = plans.stream()
                .filter(plan -> !"Rest & Recovery".equals(plan.getTitle()))
                .findFirst()
                .orElseThrow();
        for (final Exercise exercise : activePlan.getExercises()) {
            assertTrue("BODYWEIGHT".equalsIgnoreCase(exercise.getEquipmentType()));
            assertTrue("BODYWEIGHT".equalsIgnoreCase(exercise.getEquipmentRequired()));
        }
    }

    @Test
    public void generateWorkoutPlansWithNullApiKeyConstructorStillProducesFallback() {
        final AiWorkoutDataAccessObject dataAccessObject = new AiWorkoutDataAccessObject(null);
        final User user = new CommonUser("defaultGoalUser", "password");
        user.setHeight(TEST_HEIGHT);
        user.setWeight(TEST_WEIGHT);

        final List<WorkoutPlan> plans = dataAccessObject.generateWorkoutPlans(user, WEEK_DAYS);

        assertEquals(WEEK_DAYS, plans.size());
    }
}
