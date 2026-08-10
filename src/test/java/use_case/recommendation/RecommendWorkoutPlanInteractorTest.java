package use_case.recommendation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
import entity.User;
import entity.WorkoutPlan;
import use_case.EventPublisher;

/**
 * Unit tests for the Recommend Workout Plan Interactor. Never touches meal recommendations -
 * see RefreshMealRecommendationsInteractorTest for that pipeline.
 */
public class RecommendWorkoutPlanInteractorTest {

    private static final float TEST_HEIGHT = 1.8f;
    private static final float TEST_WEIGHT = 80f;
    private static final int ESTIMATED_CALORIES_BURN = 350;
    private static final int ESTIMATED_FAT_BURN = 15;
    private static final int ESTIMATED_CARBS_BURN = 45;
    private static final int TEST_SETS = 3;
    private static final int TEST_REPS = 10;
    private static final int TEST_DURATION = 10;
    private static final int DEFAULT_DURATION_MINUTES = 45;

    @Test
    public void executeWithCompleteProfileProducesRecommendationAndPublishesEvent() {
        final FakeDataAccessObject dataAccessObject = new FakeDataAccessObject();
        final FakeAiWorkoutDataAccessObject aiDao = new FakeAiWorkoutDataAccessObject();
        final FakeWorkoutPlanEventPublisher eventPublisher = new FakeWorkoutPlanEventPublisher();

        final User user = new CommonUser("aahir", "password");
        user.setHeight(TEST_HEIGHT);
        user.setWeight(TEST_WEIGHT);
        user.setActivityLevel(ActivityLevel.MODERATELY_ACTIVE);
        user.setGoal(FitnessGoal.MUSCLE_AND_STRENGTH_GAIN);
        dataAccessObject.save(user);
        dataAccessObject.setCurrentUsername("aahir");

        final RecommendWorkoutPlanOutputBoundary presenter = new RecommendWorkoutPlanOutputBoundary() {
            @Override
            public void prepareSuccessView(final RecommendWorkoutPlanOutputData outputData) {
                assertEquals(FitnessGoal.MUSCLE_AND_STRENGTH_GAIN.getWorkoutFocus(), outputData.getWorkoutFocus());
                assertEquals(1, outputData.getWorkoutPlans().size());
                assertEquals("Upper Body", outputData.getWorkoutPlans().get(0).getTitle());
                assertEquals(ESTIMATED_CALORIES_BURN, outputData.getWorkoutPlans().get(0)
                        .getEstimatedCaloriesBurned());
            }

            @Override
            public void prepareFailView(final String errorMessage) {
                throw new AssertionError("Expected success view, got failure: " + errorMessage);
            }
        };

        new RecommendWorkoutPlanInteractor(dataAccessObject, presenter, aiDao, eventPublisher).execute();

        assertEquals(1, eventPublisher.publishedEvents.size());
        assertEquals("aahir", eventPublisher.publishedEvents.get(0).getUserId());
        assertEquals(1, eventPublisher.publishedEvents.get(0).getWorkoutPlans().size());
    }

    @Test
    public void executeWithNoLoggedInUserFails() {
        final FakeDataAccessObject dataAccessObject = new FakeDataAccessObject();
        final FakeAiWorkoutDataAccessObject aiDao = new FakeAiWorkoutDataAccessObject();
        final FakeWorkoutPlanEventPublisher eventPublisher = new FakeWorkoutPlanEventPublisher();
        final boolean[] failed = {false};

        final RecommendWorkoutPlanOutputBoundary presenter = new RecommendWorkoutPlanOutputBoundary() {
            @Override
            public void prepareSuccessView(final RecommendWorkoutPlanOutputData outputData) {
                throw new AssertionError("Expected failure view");
            }

            @Override
            public void prepareFailView(final String errorMessage) {
                failed[0] = true;
                assertFalse(errorMessage.isEmpty());
            }
        };

        new RecommendWorkoutPlanInteractor(dataAccessObject, presenter, aiDao, eventPublisher).execute();
        assertTrue(failed[0]);
        assertTrue(eventPublisher.publishedEvents.isEmpty());
    }

    @Test
    public void executeWithIncompleteProfileShowsDefaultView() {
        final FakeDataAccessObject dataAccessObject = new FakeDataAccessObject();
        final FakeAiWorkoutDataAccessObject aiDao = new FakeAiWorkoutDataAccessObject();
        final FakeWorkoutPlanEventPublisher eventPublisher = new FakeWorkoutPlanEventPublisher();

        final User user = new CommonUser("aahir", "password");
        dataAccessObject.save(user);
        dataAccessObject.setCurrentUsername("aahir");
        final boolean[] succeeded = {false};

        final RecommendWorkoutPlanOutputBoundary presenter = new RecommendWorkoutPlanOutputBoundary() {
            @Override
            public void prepareSuccessView(final RecommendWorkoutPlanOutputData outputData) {
                succeeded[0] = true;
                assertTrue(outputData.getWorkoutPlans().isEmpty());
            }

            @Override
            public void prepareFailView(final String errorMessage) {
                throw new AssertionError("Expected success view with defaults, got failure: " + errorMessage);
            }
        };

        new RecommendWorkoutPlanInteractor(dataAccessObject, presenter, aiDao, eventPublisher).execute();
        assertTrue(succeeded[0]);
    }

    @Test
    public void executeFillsInDefaultsForIncompleteButValidProfile() {
        final FakeDataAccessObject dataAccessObject = new FakeDataAccessObject();
        final FakeAiWorkoutDataAccessObject aiDao = new FakeAiWorkoutDataAccessObject();
        final FakeWorkoutPlanEventPublisher eventPublisher = new FakeWorkoutPlanEventPublisher();

        final User user = new CommonUser("aahir", "password");
        user.setHeight(TEST_HEIGHT);
        user.setWeight(TEST_WEIGHT);
        user.setGoal(null);
        user.setActivityLevel(null);
        user.setPreferredWorkoutDurationMinutes(0);
        dataAccessObject.save(user);
        dataAccessObject.setCurrentUsername("aahir");
        final boolean[] succeeded = {false};

        final RecommendWorkoutPlanOutputBoundary presenter = new RecommendWorkoutPlanOutputBoundary() {
            @Override
            public void prepareSuccessView(final RecommendWorkoutPlanOutputData outputData) {
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

        new RecommendWorkoutPlanInteractor(dataAccessObject, presenter, aiDao, eventPublisher).execute();

        assertTrue(succeeded[0]);
        assertEquals(FitnessGoal.MAINTAIN_GENERAL_FITNESS, user.getGoal());
        assertEquals(ActivityLevel.MODERATELY_ACTIVE, user.getActivityLevel());
        assertEquals(DEFAULT_DURATION_MINUTES, user.getPreferredWorkoutDurationMinutes());
    }

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

    private static final class FakeWorkoutPlanEventPublisher implements EventPublisher<WorkoutPlanGeneratedEvent> {
        private final List<WorkoutPlanGeneratedEvent> publishedEvents = new ArrayList<>();

        @Override
        public void publish(final WorkoutPlanGeneratedEvent event) {
            publishedEvents.add(event);
        }
    }
}
