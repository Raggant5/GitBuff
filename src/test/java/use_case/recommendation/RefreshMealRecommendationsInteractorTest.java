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
import entity.FitnessGoal;
import entity.MealRecommendation;
import entity.User;

/**
 * Unit tests for the Refresh Meal Recommendations Interactor.
 */
public class RefreshMealRecommendationsInteractorTest {

    private static final float TEST_HEIGHT = 1.8f;
    private static final float TEST_WEIGHT = 80f;
    private static final int EXPECTED_CALORIES = 3028;
    private static final int EXPECTED_PROTEIN = 160;
    private static final int MEAL_READY_MINUTES = 20;

    @Test
    public void executeRefreshesMealsOnly() {
        final FakeDataAccessObject dataAccessObject = new FakeDataAccessObject();
        final FakeFoodRecommendationDataAccessObject foodDao = new FakeFoodRecommendationDataAccessObject();

        final User user = new CommonUser("aahir", "password");
        user.setHeight(TEST_HEIGHT);
        user.setWeight(TEST_WEIGHT);
        user.setActivityLevel(ActivityLevel.MODERATELY_ACTIVE);
        user.setGoal(FitnessGoal.MUSCLE_AND_STRENGTH_GAIN);
        dataAccessObject.save(user);
        dataAccessObject.setCurrentUsername("aahir");
        final boolean[] succeeded = {false};

        final RefreshMealRecommendationsOutputBoundary presenter = new RefreshMealRecommendationsOutputBoundary() {
            @Override
            public void prepareSuccessView(final RefreshMealRecommendationsOutputData outputData) {
                succeeded[0] = true;
                assertEquals(EXPECTED_CALORIES, outputData.getDailyCalorieTarget());
                assertEquals(EXPECTED_PROTEIN, outputData.getDailyProteinGrams());
                assertEquals(user.getBmi(), outputData.getBmi(), 0.0001);
                assertEquals(1, outputData.getMealRecommendations().size());
                assertEquals("Chicken and Rice", outputData.getMealRecommendations().get(0).getTitle());
                assertEquals(EXPECTED_CALORIES, foodDao.lastRequestedCalories);
            }

            @Override
            public void prepareFailView(final String errorMessage) {
                throw new AssertionError("Expected success view, got failure: " + errorMessage);
            }
        };

        new RefreshMealRecommendationsInteractor(dataAccessObject, presenter, foodDao).execute();

        assertTrue(succeeded[0]);
    }

    @Test
    public void executeWithNoLoggedInUserFails() {
        final FakeDataAccessObject dataAccessObject = new FakeDataAccessObject();
        final FakeFoodRecommendationDataAccessObject foodDao = new FakeFoodRecommendationDataAccessObject();
        final boolean[] failed = {false};

        final RefreshMealRecommendationsOutputBoundary presenter = new RefreshMealRecommendationsOutputBoundary() {
            @Override
            public void prepareSuccessView(final RefreshMealRecommendationsOutputData outputData) {
                throw new AssertionError("Expected failure view");
            }

            @Override
            public void prepareFailView(final String errorMessage) {
                failed[0] = true;
                assertFalse(errorMessage.isEmpty());
            }
        };

        new RefreshMealRecommendationsInteractor(dataAccessObject, presenter, foodDao).execute();

        assertTrue(failed[0]);
        assertNull(dataAccessObject.get("nobody"));
    }

    @Test
    public void executeWithMissingUserRecordFails() {
        final FakeDataAccessObject dataAccessObject = new FakeDataAccessObject();
        final FakeFoodRecommendationDataAccessObject foodDao = new FakeFoodRecommendationDataAccessObject();
        dataAccessObject.setCurrentUsername("ghost");
        final boolean[] failed = {false};

        final RefreshMealRecommendationsOutputBoundary presenter = new RefreshMealRecommendationsOutputBoundary() {
            @Override
            public void prepareSuccessView(final RefreshMealRecommendationsOutputData outputData) {
                throw new AssertionError("Expected failure view");
            }

            @Override
            public void prepareFailView(final String errorMessage) {
                failed[0] = true;
                assertFalse(errorMessage.isEmpty());
            }
        };

        new RefreshMealRecommendationsInteractor(dataAccessObject, presenter, foodDao).execute();

        assertTrue(failed[0]);
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
