package use_case.recommendation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import entity.ActivityLevel;
import entity.CommonUser;
import entity.FitnessGoal;
import entity.User;

/**
 * Unit tests for the Recommendation Interactor.
 */
public class RecommendationInteractorTest {

    private static class FakeDataAccessObject implements RecommendationUserDataAccessInterface {
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

    private static class FakeAiWorkoutDataAccessObject implements AiWorkoutDataAccessInterface {
        @Override
        public String generateWorkoutPlan(final User user) {
            return "Mock AI Workout Plan for " + user.getName();
        }
    }

    @Test
    public void executeWithCompleteProfileProducesRecommendation() {
        final FakeDataAccessObject dataAccessObject = new FakeDataAccessObject();
        final FakeAiWorkoutDataAccessObject aiDao = new FakeAiWorkoutDataAccessObject();

        final User user = new CommonUser("aahir", "password");
        user.setHeight(1.8f);
        user.setWeight(80f);
        user.setActivityLevel(ActivityLevel.MODERATELY_ACTIVE);
        user.setGoal(FitnessGoal.MUSCLE_AND_STRENGTH_GAIN);
        dataAccessObject.save(user);
        dataAccessObject.setCurrentUsername("aahir");

        final RecommendationOutputBoundary presenter = new RecommendationOutputBoundary() {
            @Override
            public void prepareSuccessView(final RecommendationOutputData outputData) {
                // resting calories = 22 * 80 = 1760; TDEE = 1760 * 1.55 = 2728; +300 for muscle gain = 3028
                assertEquals(3028, outputData.getDailyCalorieTarget());
                // protein = 80 * 2.0 g/kg = 160
                assertEquals(160, outputData.getDailyProteinGrams());
                assertEquals(user.getBMI(), outputData.getBmi(), 0.0001);
                assertEquals(FitnessGoal.MUSCLE_AND_STRENGTH_GAIN.getWorkoutFocus(), outputData.getWorkoutFocus());
                assertEquals("Mock AI Workout Plan for aahir", outputData.getAiWorkoutPlan());
            }

            @Override
            public void prepareFailView(final String errorMessage) {
                throw new AssertionError("Expected success view, got failure: " + errorMessage);
            }
        };

        new RecommendationInteractor(dataAccessObject, presenter, aiDao).execute();
    }

    @Test
    public void executeWithNoLoggedInUserFails() {
        final FakeDataAccessObject dataAccessObject = new FakeDataAccessObject();
        final FakeAiWorkoutDataAccessObject aiDao = new FakeAiWorkoutDataAccessObject();
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

        new RecommendationInteractor(dataAccessObject, presenter, aiDao).execute();
        assertTrue(failed[0]);
    }

    @Test
    public void executeWithIncompleteProfileShowsDefaultView() {
        final FakeDataAccessObject dataAccessObject = new FakeDataAccessObject();
        final FakeAiWorkoutDataAccessObject aiDao = new FakeAiWorkoutDataAccessObject();

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
                assertTrue(outputData.getAiWorkoutPlan().contains("Complete your profile"));
            }

            @Override
            public void prepareFailView(final String errorMessage) {
                throw new AssertionError("Expected success view with defaults, got failure: " + errorMessage);
            }
        };

        new RecommendationInteractor(dataAccessObject, presenter, aiDao).execute();
        assertTrue(succeeded[0]);
        assertNull(dataAccessObject.get("nobody"));
    }
}
