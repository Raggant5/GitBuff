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
import entity.User;
import entity.WorkoutPlan;

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
        public List<WorkoutPlan> generateWorkoutPlans(final User user) {
            final List<WorkoutPlan> plans = new ArrayList<>();
            final List<Exercise> exercises = new ArrayList<>();
            exercises.add(new Exercise("Push-Ups", "3 sets of 10", "Lower chest to ground.", "http://example.com"));
            plans.add(new WorkoutPlan("Monday, Aug 3", "Upper Body", "Chest focus", exercises));
            return plans;
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
                assertEquals(160, outputData.getDailyProteinGrams());
                assertEquals(user.getBMI(), outputData.getBmi(), 0.0001);
                assertEquals(FitnessGoal.MUSCLE_AND_STRENGTH_GAIN.getWorkoutFocus(), outputData.getWorkoutFocus());
                assertEquals(1, outputData.getWorkoutPlans().size());
                assertEquals("Upper Body", outputData.getWorkoutPlans().get(0).getTitle());
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
                assertTrue(outputData.getWorkoutPlans().isEmpty());
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
