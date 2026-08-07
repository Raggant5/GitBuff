package use_case.profile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.junit.jupiter.api.Test;

import entity.ActivityLevel;
import entity.CommonUser;
import entity.FitnessGoal;
import entity.Gender;
import entity.UnitSystem;
import entity.User;
import org.junit.jupiter.api.Test;
import use_case.recommendation.RecommendationInputBoundary;

public class EditProfileInteractorTest {

    @Test
    public void executeSavesProfile() {
        final FakeDataAccessObject dataAccessObject = new FakeDataAccessObject();
        final User user = new CommonUser("aahir", "password");
        dataAccessObject.save(user);
        dataAccessObject.currentUsername = "aahir";

        final EditProfileOutputBoundary presenter = new EditProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(EditProfileOutputData outputData) {
                assertEquals("aahir", outputData.getUsername());
                assertEquals(1.8f, outputData.getHeight(), 0.0001);
                assertEquals(80f, outputData.getWeight(), 0.0001);
                assertEquals(ActivityLevel.VERY_ACTIVE, outputData.getActivityLevel());
                assertEquals(FitnessGoal.MUSCLE_AND_STRENGTH_GAIN, outputData.getGoal());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                throw new AssertionError("Expected success view, got failure: " + errorMessage);
            }
        };

        final EditProfileInputData inputData = new EditProfileInputData.Builder()
                .height(1.8f)
                .weight(80f)
                .activityLevel(ActivityLevel.VERY_ACTIVE)
                .goal(FitnessGoal.MUSCLE_AND_STRENGTH_GAIN)
                .profilePicturePath("/tmp/pic.png")
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .gender(Gender.MALE)
                .bio("Hello world")
                .preferredUnitSystem(UnitSystem.METRIC)
                .equipment(new HashSet<>())
                .dietaryRestrictions(new HashSet<>())
                .preferredWorkoutDays(new HashSet<>())
                .preferredWorkoutDurationMinutes(45)
                .privacySettings(new HashSet<>())
                .build();

        new EditProfileInteractor(dataAccessObject, presenter).execute(inputData);

        assertEquals("/tmp/pic.png", dataAccessObject.get("aahir").getProfilePicturePath());
    }

    @Test
    public void executeWithInvalidHeightFails() {
        final FakeDataAccessObject dataAccessObject = new FakeDataAccessObject();
        final User user = new CommonUser("aahir", "password");
        dataAccessObject.save(user);
        dataAccessObject.currentUsername = "aahir";

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

        final EditProfileInputData inputData = new EditProfileInputData.Builder()
                .height(0f)
                .weight(80f)
                .activityLevel(ActivityLevel.VERY_ACTIVE)
                .goal(FitnessGoal.MUSCLE_AND_STRENGTH_GAIN)
                .profilePicturePath(null)
                .dateOfBirth(null)
                .gender(Gender.PREFER_NOT_TO_SAY)
                .bio("")
                .preferredUnitSystem(UnitSystem.METRIC)
                .equipment(new HashSet<>())
                .dietaryRestrictions(new HashSet<>())
                .preferredWorkoutDays(new HashSet<>())
                .preferredWorkoutDurationMinutes(45)
                .privacySettings(new HashSet<>())
                .build();

        new EditProfileInteractor(dataAccessObject, presenter).execute(inputData);
        assertTrue(failed[0]);
    }

    private static final class FakeDataAccessObject implements ProfileUserDataAccessInterface {
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
}
