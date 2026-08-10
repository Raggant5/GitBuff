package use_case.recommendation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import entity.ActivityLevel;
import entity.CommonUser;
import entity.Exercise;
import entity.FitnessGoal;
import entity.MealRecommendation;
import entity.User;
import entity.WorkoutPlan;

/**
 * Unit tests for the Recommendation Interactor.
 */
public class RecommendationInteractorTest {

    private static final float TEST_HEIGHT = 1.8f;
    private static final float TEST_WEIGHT = 80f;
    private static final int EXPECTED_CALORIES = 3028;
    private static final int EXPECTED_PROTEIN = 160;
    private static final int ESTIMATED_CALORIES_BURN = 350;
    private static final int ESTIMATED_FAT_BURN = 15;
    private static final int ESTIMATED_CARBS_BURN = 45;
    private static final int TEST_SETS = 3;
    private static final int TEST_REPS = 10;
    private static final int TEST_DURATION = 10;
    private static final int DEFAULT_DURATION_MINUTES = 45;
    private static final int MEAL_READY_MINUTES = 20;

    @Test
    public void executeWithCompleteProfileProducesRecommendation() {
        final FakeDataAccessObject dataAccessObject = new FakeDataAccessObject();
        final FakeAiWorkoutDataAccessObject aiDao = new FakeAiWorkoutDataAccessObject();
        final FakeFoodRecommendationDataAccessObject foodDao = new FakeFoodRecommendationDataAccessObject();

        final User user = new CommonUser("aahir", "password");
        user.setHeight(TEST_HEIGHT);
        user.setWeight(TEST_WEIGHT);
        user.setActivityLevel(ActivityLevel.MODERATELY_ACTIVE);
        user.setGoal(FitnessGoal.MUSCLE_AND_STRENGTH_GAIN);
        dataAccessObject.save(user);
        dataAccessObject.setCurrentUsername("aahir");

        final RecommendationOutputBoundary presenter = new RecommendationOutputBoundary() {
            @Override
            public void prepareSuccessView(final RecommendationOutputData outputData) {
                assertEquals(EXPECTED_CALORIES, outputData.getDailyCalorieTarget());
                assertEquals(EXPECTED_PROTEIN, outputData.getDailyProteinGrams());
                assertEquals(user.getBmi(), outputData.getBmi(), 0.0001);
                assertEquals(FitnessGoal.MUSCLE_AND_STRENGTH_GAIN.getWorkoutFocus(), outputData.getWorkoutFocus());
                assertEquals(1, outputData.getWorkoutPlans().size());
                assertEquals("Upper Body", outputData.getWorkoutPlans().get(0).getTitle());
                assertEquals(ESTIMATED_CALORIES_BURN, outputData.getWorkoutPlans().get(0).getEstimatedCaloriesBurned());
                assertEquals(1, outputData.getMealRecommendations().size());
                assertEquals("Chicken and Rice", outputData.getMealRecommendations().get(0).getTitle());
                assertEquals(EXPECTED_CALORIES, foodDao.lastRequestedCalories);
            }

            @Override
            public void prepareFailView(final String errorMessage) {
                throw new AssertionError("Expected success view, got failure: " + errorMessage);
            }
        };

        new RecommendationInteractor(dataAccessObject, presenter, aiDao, foodDao).execute();
    }

    @Test
    public void executeWithNoLoggedInUserFails() {
        final FakeDataAccessObject dataAccessObject = new FakeDataAccessObject();
        final FakeAiWorkoutDataAccessObject aiDao = new FakeAiWorkoutDataAccessObject();
        final FakeFoodRecommendationDataAccessObject foodDao = new FakeFoodRecommendationDataAccessObject();
        final boolean[] failed = {false};

        final RecommendationOutputBoundary presenter = new RecommendationOutputBoundary() {
            @Override
            public void prepareSuccessView(final RecommendationOutputData outputData) {
                throw new AssertionError("Expected failure view");
            }

            @Override
            public void prepareFailView(final String errorMessage) {
                failed[0] = true;
                assertFalse(errorMessage.isEmpty());
            }
        };

        new RecommendationInteractor(dataAccessObject, presenter, aiDao, foodDao).execute();
        assertTrue(failed[0]);
    }

    @Test
    public void executeWithIncompleteProfileShowsDefaultView() {
        final FakeDataAccessObject dataAccessObject = new FakeDataAccessObject();
        final FakeAiWorkoutDataAccessObject aiDao = new FakeAiWorkoutDataAccessObject();
        final FakeFoodRecommendationDataAccessObject foodDao = new FakeFoodRecommendationDataAccessObject();

        final User user = new CommonUser("aahir", "password");
        dataAccessObject.save(user);
        dataAccessObject.setCurrentUsername("aahir");
        final boolean[] succeeded = {false};

        final RecommendationOutputBoundary presenter = new RecommendationOutputBoundary() {
            @Override
            public void prepareSuccessView(final RecommendationOutputData outputData) {
                succeeded[0] = true;
                assertEquals(0.0, outputData.getBmi());
                assertEquals(0, outputData.getDailyCalorieTarget());
                assertTrue(outputData.getWorkoutPlans().isEmpty());
                assertTrue(outputData.getMealRecommendations().isEmpty());
            }

            @Override
            public void prepareFailView(final String errorMessage) {
                throw new AssertionError("Expected success view with defaults, got failure: " + errorMessage);
            }
        };

        new RecommendationInteractor(dataAccessObject, presenter, aiDao, foodDao).execute();
        assertTrue(succeeded[0]);
        assertNull(dataAccessObject.get("nobody"));
    }

    @Test
    public void executeFillsInDefaultsForIncompleteButValidProfile() {
        final FakeDataAccessObject dataAccessObject = new FakeDataAccessObject();
        final FakeAiWorkoutDataAccessObject aiDao = new FakeAiWorkoutDataAccessObject();
        final FakeFoodRecommendationDataAccessObject foodDao = new FakeFoodRecommendationDataAccessObject();

        final User user = new CommonUser("aahir", "password");
        user.setHeight(TEST_HEIGHT);
        user.setWeight(TEST_WEIGHT);
        user.setGoal(null);
        user.setActivityLevel(null);
        user.setPreferredWorkoutDurationMinutes(0);
        dataAccessObject.save(user);
        dataAccessObject.setCurrentUsername("aahir");
        final boolean[] succeeded = {false};

        final RecommendationOutputBoundary presenter = new RecommendationOutputBoundary() {
            @Override
            public void prepareSuccessView(final RecommendationOutputData outputData) {
                succeeded[0] = true;
                assertEquals(FitnessGoal.MAINTAIN_GENERAL_FITNESS.getWorkoutFocus(), outputData.getWorkoutFocus());
                assertEquals(ActivityLevel.MODERATELY_ACTIVE.getDescription(),
                        outputData.getActivityLevelDescription());
            }

            @Override
            public void prepareFailView(final String errorMessage) {
                throw new AssertionError("Expected success view, got failure: " + errorMessage);
            }
        };

        new RecommendationInteractor(dataAccessObject, presenter, aiDao, foodDao).execute();

        assertTrue(succeeded[0]);
        assertEquals(FitnessGoal.MAINTAIN_GENERAL_FITNESS, user.getGoal());
        assertEquals(ActivityLevel.MODERATELY_ACTIVE, user.getActivityLevel());
        assertEquals(DEFAULT_DURATION_MINUTES, user.getPreferredWorkoutDurationMinutes());
    }

    /**
     * Fake data access object implementing RecommendationUserDataAccessInterface for unit testing.
     */
    private static final class FakeDataAccessObject implements RecommendationUserDataAccessInterface {
        private final Map<String, User> users = new HashMap<>();
        private String currentUsername;

        void save(final User user) {
            this.users.put(user.getName(), user);
        }

        void setCurrentUsername(final String username) {
            this.currentUsername = username;
        }

        @Override
        public User get(final String username) {
            return this.users.get(username);
        }

        @Override
        public String getCurrentUsername() {
            return this.currentUsername;
        }
    }

    /**
     * Fake AI Workout data access object implementing AiWorkoutDataAccessInterface for unit testing.
     */
    private static final class FakeAiWorkoutDataAccessObject implements AiWorkoutDataAccessInterface {
        @Override
        public List<WorkoutPlan> generateWorkoutPlans(final User user) {
            final List<WorkoutPlan> plans = new ArrayList<>();
            final List<Exercise> exercises = new ArrayList<>();
            exercises.add(new Exercise("Push-Ups", TEST_SETS, TEST_REPS, TEST_DURATION,
                    "Chest", "Bodyweight", "Lower chest to ground.", "http://example.com",
                    "STRENGTH", "UPPER_BODY", "MEDIUM", "BODYWEIGHT"));
            plans.add(new WorkoutPlan("Monday, Aug 3", "Upper Body", "Chest focus",
                    "STRENGTH", "UPPER_BODY", "MEDIUM", "CHEST", "BODYWEIGHT",
                    DEFAULT_DURATION_MINUTES, ESTIMATED_CALORIES_BURN,
                    ESTIMATED_FAT_BURN, ESTIMATED_CARBS_BURN, exercises));
            return plans;
        }

        @Override
        public List<WorkoutPlan> generateWorkoutPlans(final User user, final int numberOfDays) {
            return generateWorkoutPlans(user);
        }
    }

    /**
     * Fake food recommendation data access object implementing FoodRecommendationDataAccessInterface
     * for unit testing.
     */
    private static final class FakeFoodRecommendationDataAccessObject
            implements FoodRecommendationDataAccessInterface {
        private int lastRequestedCalories;

        @Override
        public List<MealRecommendation> generateMealRecommendations(final User user, final int targetCalories) {
            this.lastRequestedCalories = targetCalories;
            final List<MealRecommendation> meals = new ArrayList<>();
            meals.add(new MealRecommendation("Chicken and Rice", MEAL_READY_MINUTES, "http://example.com/recipe"));
            return meals;
        }
    }
}
