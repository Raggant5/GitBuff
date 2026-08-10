package use_case.profile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import entity.ActivityLevel;
import entity.CommonUser;
import entity.FitnessGoal;
import entity.Gender;
import entity.UnitSystem;
import entity.User;
import use_case.EventPublisher;

public class EditProfileInteractorTest {

    private static final class FakeProfileEventPublisher implements EventPublisher<ProfileUpdatedEvent> {
        private final List<ProfileUpdatedEvent> publishedEvents = new ArrayList<>();

        @Override
        public void publish(ProfileUpdatedEvent event) {
            publishedEvents.add(event);
        }
    }

    @Test
    public void executeWithNoLoggedInUserFails() {
        final FakeDataAccessObject dataAccessObject = new FakeDataAccessObject();
        final boolean[] failed = {false};

        final EditProfileOutputBoundary presenter = new EditProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(EditProfileOutputData outputData) {
                throw new AssertionError("Expected failure view");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                failed[0] = true;
                assertEquals("No user is currently logged in.", errorMessage);
            }
        };

        final EditProfileInputData inputData = new EditProfileInputData.Builder()
                .height(1.8f)
                .weight(80f)
                .activityLevel(ActivityLevel.VERY_ACTIVE)
                .goal(FitnessGoal.MUSCLE_AND_STRENGTH_GAIN)
                .equipment(new HashSet<>())
                .dietaryRestrictions(new HashSet<>())
                .preferredWorkoutDays(new HashSet<>())
                .preferredWorkoutDurationMinutes(45)
                .privacySettings(new HashSet<>())
                .build();

        new EditProfileInteractor(dataAccessObject, presenter, new FakeProfileEventPublisher()).execute(inputData);
        assertTrue(failed[0]);
    }

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

        new EditProfileInteractor(dataAccessObject, presenter, new FakeProfileEventPublisher()).execute(inputData);

        assertEquals("/tmp/pic.png", dataAccessObject.get("aahir").getProfilePicturePath());
    }

    @Test
    public void executeOnSuccessPublishesProfileUpdatedEvent() {
        final FakeDataAccessObject dataAccessObject = new FakeDataAccessObject();
        final User user = new CommonUser("aahir", "password");
        dataAccessObject.save(user);
        dataAccessObject.currentUsername = "aahir";
        final FakeProfileEventPublisher eventPublisher = new FakeProfileEventPublisher();

        final EditProfileOutputBoundary presenter = new EditProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(EditProfileOutputData outputData) {
                // this test only checks that the event was published
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
                .equipment(new HashSet<>())
                .dietaryRestrictions(new HashSet<>())
                .preferredWorkoutDays(new HashSet<>())
                .preferredWorkoutDurationMinutes(45)
                .privacySettings(new HashSet<>())
                .build();

        new EditProfileInteractor(dataAccessObject, presenter, eventPublisher).execute(inputData);

        assertEquals(1, eventPublisher.publishedEvents.size());
        assertEquals("aahir", eventPublisher.publishedEvents.get(0).getUsername());
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

        new EditProfileInteractor(dataAccessObject, presenter, new FakeProfileEventPublisher()).execute(inputData);
        assertTrue(failed[0]);
    }

    @Test
    public void executeWhenDataAccessThrowsFails() {
        final FakeDataAccessObject dataAccessObject = new FakeDataAccessObject();
        dataAccessObject.currentUsername = "aahir";
        dataAccessObject.throwOnGet = true;

        final boolean[] failed = {false};
        final EditProfileOutputBoundary presenter = new EditProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(EditProfileOutputData outputData) {
                throw new AssertionError("Expected failure view");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                failed[0] = true;
                assertEquals("Unable to save your profile right now. Please try again.", errorMessage);
            }
        };

        final EditProfileInputData inputData = new EditProfileInputData.Builder()
                .height(1.8f)
                .weight(80f)
                .activityLevel(ActivityLevel.VERY_ACTIVE)
                .goal(FitnessGoal.MUSCLE_AND_STRENGTH_GAIN)
                .equipment(new HashSet<>())
                .dietaryRestrictions(new HashSet<>())
                .preferredWorkoutDays(new HashSet<>())
                .preferredWorkoutDurationMinutes(45)
                .privacySettings(new HashSet<>())
                .build();

        new EditProfileInteractor(dataAccessObject, presenter, new FakeProfileEventPublisher()).execute(inputData);
        assertTrue(failed[0]);
    }

    private static final class FakeDataAccessObject implements ProfileUserDataAccessInterface {
        private final Map<String, User> users = new HashMap<>();
        private String currentUsername;
        private boolean throwOnGet;

        @Override
        public User get(String username) {
            if (throwOnGet) {
                throw new use_case.DataAccessException("Database unavailable");
            }
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
