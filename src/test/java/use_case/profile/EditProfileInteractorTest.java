package use_case.profile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import entity.ActivityLevel;
import entity.CommonUser;
import entity.FitnessGoal;
import entity.User;
import org.junit.jupiter.api.Test;
import use_case.recommendation.RecommendationInputBoundary;

public class EditProfileInteractorTest {

    private static class FakeDataAccessObject implements ProfileUserDataAccessInterface {
        private final Map<String, User> users = new HashMap<>();
        private String currentUsername;

        @Override
        public User get(String username) {
            return users.get(username);
        }

        @Override
        public void save(User user) {
            users.put(user.getName(), user);
        }

        @Override
        public String getCurrentUsername() {
            return currentUsername;
        }
    }

    @Test
    public void executeSavesProfileAndRefreshesRecommendations() {
        final FakeDataAccessObject dataAccessObject = new FakeDataAccessObject();
        final User user = new CommonUser("aahir", "password");
        dataAccessObject.save(user);
        dataAccessObject.currentUsername = "aahir";

        final boolean[] recommendationRefreshed = {false};
        final RecommendationInputBoundary recommendationInteractor = () -> recommendationRefreshed[0] = true;

        final EditProfileOutputBoundary presenter = new EditProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(EditProfileOutputData outputData) {
                assertEquals("aahir", outputData.getUsername());
                assertEquals(1.8f, outputData.getHeight(), 0.0001);
                assertEquals(80f, outputData.getWeight(), 0.0001);
                assertEquals(ActivityLevel.VERY_ACTIVE, outputData.getActivityLevel());
                assertEquals(FitnessGoal.GAIN_MUSCLE, outputData.getGoal());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                throw new AssertionError("Expected success view, got failure: " + errorMessage);
            }
        };

        final EditProfileInputData inputData = new EditProfileInputData(
                1.8f, 80f, ActivityLevel.VERY_ACTIVE, FitnessGoal.GAIN_MUSCLE, "/tmp/pic.png");

        new EditProfileInteractor(dataAccessObject, presenter, recommendationInteractor).execute(inputData);

        assertTrue(recommendationRefreshed[0]);
        assertEquals("/tmp/pic.png", dataAccessObject.get("aahir").getProfilePicturePath());
    }

    @Test
    public void executeWithInvalidHeightFails() {
        final FakeDataAccessObject dataAccessObject = new FakeDataAccessObject();
        final User user = new CommonUser("aahir", "password");
        dataAccessObject.save(user);
        dataAccessObject.currentUsername = "aahir";

        final RecommendationInputBoundary recommendationInteractor = () -> {
            throw new AssertionError("Should not refresh recommendations on failure");
        };

        final boolean[] failed = {false};
        final EditProfileOutputBoundary presenter = new EditProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(EditProfileOutputData outputData) {
                throw new AssertionError("Expected failure view");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                failed[0] = true;
            }
        };

        final EditProfileInputData inputData = new EditProfileInputData(
                0f, 80f, ActivityLevel.VERY_ACTIVE, FitnessGoal.GAIN_MUSCLE, null);

        new EditProfileInteractor(dataAccessObject, presenter, recommendationInteractor).execute(inputData);
        assertTrue(failed[0]);
    }
}
