package use_case.recommendation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import entity.ActivityLevel;
import entity.CommonUser;
import entity.FitnessGoal;
import entity.User;
import org.junit.jupiter.api.Test;

public class RecommendationInteractorTest {

    private static class FakeDataAccessObject implements RecommendationUserDataAccessInterface {
        private final Map<String, User> users = new HashMap<>();
        private String currentUsername;

        void save(User user) {
            users.put(user.getName(), user);
        }

        void setCurrentUsername(String username) {
            this.currentUsername = username;
        }

        @Override
        public User get(String username) {
            return users.get(username);
        }

        @Override
        public String getCurrentUsername() {
            return currentUsername;
        }
    }

    @Test
    public void executeWithCompleteProfileProducesRecommendation() {
        final FakeDataAccessObject dataAccessObject = new FakeDataAccessObject();
        final User user = new CommonUser("aahir", "password");
        user.setHeight(1.8f);
        user.setWeight(80f);
        user.setActivityLevel(ActivityLevel.MODERATELY_ACTIVE);
        user.setGoal(FitnessGoal.GAIN_MUSCLE);
        dataAccessObject.save(user);
        dataAccessObject.setCurrentUsername("aahir");

        final RecommendationOutputBoundary presenter = new RecommendationOutputBoundary() {
            @Override
            public void prepareSuccessView(RecommendationOutputData outputData) {
                // resting calories = 22 * 80 = 1760; TDEE = 1760 * 1.55 = 2728; +300 for muscle gain = 3028
                assertEquals(3028, outputData.getDailyCalorieTarget());
                // protein = 80 * 2.0 g/kg = 160
                assertEquals(160, outputData.getDailyProteinGrams());
                assertEquals(user.getBMI(), outputData.getBmi(), 0.0001);
                assertEquals(FitnessGoal.GAIN_MUSCLE.getWorkoutFocus(), outputData.getWorkoutFocus());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                throw new AssertionError("Expected success view, got failure: " + errorMessage);
            }
        };

        new RecommendationInteractor(dataAccessObject, presenter).execute();
    }

    @Test
    public void executeWithNoLoggedInUserFails() {
        final FakeDataAccessObject dataAccessObject = new FakeDataAccessObject();
        final boolean[] failed = {false};

        final RecommendationOutputBoundary presenter = new RecommendationOutputBoundary() {
            @Override
            public void prepareSuccessView(RecommendationOutputData outputData) {
                throw new AssertionError("Expected failure view");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                failed[0] = true;
                assertFalse(errorMessage.isEmpty());
            }
        };

        new RecommendationInteractor(dataAccessObject, presenter).execute();
        assertTrue(failed[0]);
    }

    @Test
    public void executeWithIncompleteProfileFails() {
        final FakeDataAccessObject dataAccessObject = new FakeDataAccessObject();
        final User user = new CommonUser("aahir", "password");
        dataAccessObject.save(user);
        dataAccessObject.setCurrentUsername("aahir");
        final boolean[] failed = {false};

        final RecommendationOutputBoundary presenter = new RecommendationOutputBoundary() {
            @Override
            public void prepareSuccessView(RecommendationOutputData outputData) {
                throw new AssertionError("Expected failure view");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                failed[0] = true;
            }
        };

        new RecommendationInteractor(dataAccessObject, presenter).execute();
        assertTrue(failed[0]);
        assertNull(dataAccessObject.get("nobody"));
    }
}
