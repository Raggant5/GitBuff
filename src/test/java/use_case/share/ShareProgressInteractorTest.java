package use_case.share;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;

import entity.CommonUser;
import entity.LoggedWorkout;
import entity.LoggedWorkoutFactory;
import entity.PrivacySetting;
import entity.User;
import org.junit.jupiter.api.Test;

class ShareProgressInteractorTest {

    @Test
    void prepareSharePreviewWithNoUserFails() {
        final FakeDataAccessObject dataAccessObject = new FakeDataAccessObject(null);
        final CapturingPresenter presenter = new CapturingPresenter();

        new ShareProgressInteractor(dataAccessObject, dataAccessObject, presenter).prepareSharePreview();

        assertEquals("No user is currently logged in.", presenter.failMessage);
    }

    @Test
    void prepareSharePreviewWithNullPrivacySettingsFails() {
        final User user = new CommonUser("amir", "password");
        user.setPrivacySettings(null);
        final FakeDataAccessObject dataAccessObject = new FakeDataAccessObject(user);
        final CapturingPresenter presenter = new CapturingPresenter();

        new ShareProgressInteractor(dataAccessObject, dataAccessObject, presenter).prepareSharePreview();

        assertEquals("No shareable privacy settings enabled in your profile!", presenter.failMessage);
    }

    @Test
    void prepareSharePreviewWithNoSettingsEnabledFails() {
        final User user = new CommonUser("amir", "password");
        user.setPrivacySettings(Set.of());
        final FakeDataAccessObject dataAccessObject = new FakeDataAccessObject(user);
        final CapturingPresenter presenter = new CapturingPresenter();

        new ShareProgressInteractor(dataAccessObject, dataAccessObject, presenter).prepareSharePreview();

        assertEquals("No shareable privacy settings enabled in your profile!", presenter.failMessage);
    }

    @Test
    void prepareSharePreviewWithAllSettingsEnabledSucceeds() {
        final User user = new CommonUser("amir", "password");
        user.setProfilePicturePath("/tmp/pic.png");
        user.setPrivacySettings(Set.of(
                PrivacySetting.SHARE_PROFILE, PrivacySetting.SHARE_WORKOUT_LOGS,
                PrivacySetting.SHARE_PERSONAL_RECORDS));
        final LoggedWorkout workout = new LoggedWorkoutFactory().create("amir", java.time.LocalDate.now());
        final FakeDataAccessObject dataAccessObject = new FakeDataAccessObject(user);
        dataAccessObject.workouts = List.of(workout);
        dataAccessObject.totalMinutes = 120.0;
        final CapturingPresenter presenter = new CapturingPresenter();

        new ShareProgressInteractor(dataAccessObject, dataAccessObject, presenter).prepareSharePreview();

        assertTrue(presenter.previewData.getFormattedShareText().contains("amir"));
        assertEquals("/tmp/pic.png", presenter.previewData.getProfilePicturePath());
    }

    @Test
    void sendShareEmailWithInvalidEmailFails() {
        final FakeDataAccessObject dataAccessObject = new FakeDataAccessObject(null);
        final CapturingPresenter presenter = new CapturingPresenter();

        new ShareProgressInteractor(dataAccessObject, dataAccessObject, presenter).sendShareEmail("not-an-email");

        assertEquals("Please enter a valid recipient email address.", presenter.failMessage);
    }

    @Test
    void sendShareEmailWithNoUserFails() {
        final FakeDataAccessObject dataAccessObject = new FakeDataAccessObject(null);
        final CapturingPresenter presenter = new CapturingPresenter();

        new ShareProgressInteractor(dataAccessObject, dataAccessObject, presenter)
                .sendShareEmail("friend@example.com");

        assertEquals("No user session found.", presenter.failMessage);
    }

    @Test
    void sendShareEmailWithNoSharableContentFails() {
        final User user = new CommonUser("amir", "password");
        user.setPrivacySettings(Set.of());
        final FakeDataAccessObject dataAccessObject = new FakeDataAccessObject(user);
        final CapturingPresenter presenter = new CapturingPresenter();

        new ShareProgressInteractor(dataAccessObject, dataAccessObject, presenter)
                .sendShareEmail("friend@example.com");

        assertEquals("No shareable privacy settings enabled in your profile!", presenter.failMessage);
    }

    @Test
    void sendShareEmailSucceeds() {
        final User user = new CommonUser("amir", "password");
        user.setPrivacySettings(Set.of(PrivacySetting.SHARE_PROFILE));
        final FakeDataAccessObject dataAccessObject = new FakeDataAccessObject(user);
        dataAccessObject.emailSendResult = true;
        final CapturingPresenter presenter = new CapturingPresenter();

        new ShareProgressInteractor(dataAccessObject, dataAccessObject, presenter)
                .sendShareEmail("friend@example.com");

        assertEquals("Progress shared successfully with friend@example.com!", presenter.sendSuccessMessage);
    }

    @Test
    void sendShareEmailWhenEmailServiceFailsPresentsFailure() {
        final User user = new CommonUser("amir", "password");
        user.setPrivacySettings(Set.of(PrivacySetting.SHARE_PROFILE));
        final FakeDataAccessObject dataAccessObject = new FakeDataAccessObject(user);
        dataAccessObject.emailSendResult = false;
        final CapturingPresenter presenter = new CapturingPresenter();

        new ShareProgressInteractor(dataAccessObject, dataAccessObject, presenter)
                .sendShareEmail("friend@example.com");

        assertEquals("Failed to send email. Opening default mail application...", presenter.failMessage);
    }

    private static final class FakeDataAccessObject
            implements ShareProgressUserDataAccessInterface, ShareEmailDataAccessInterface {
        private final User currentUser;
        private List<LoggedWorkout> workouts = List.of();
        private double totalMinutes;
        private boolean emailSendResult;

        private FakeDataAccessObject(final User currentUser) {
            this.currentUser = currentUser;
        }

        @Override
        public User getCurrentUser() {
            return currentUser;
        }

        @Override
        public int getTotalCompletedWorkouts(final String username) {
            return workouts.size();
        }

        @Override
        public double getTotalMinutesWorkedOut(final String username) {
            return totalMinutes;
        }

        @Override
        public List<LoggedWorkout> getWorkoutsForUser(final String username) {
            return workouts;
        }

        @Override
        public boolean sendEmail(final String recipientEmail, final String subject, final String bodyText,
                                 final String imagePath) {
            return emailSendResult;
        }
    }

    private static final class CapturingPresenter implements ShareProgressOutputBoundary {
        private ShareProgressOutputData previewData;
        private String sendSuccessMessage;
        private String failMessage;

        @Override
        public void preparePreviewView(final ShareProgressOutputData outputData) {
            this.previewData = outputData;
        }

        @Override
        public void prepareSendSuccessView(final String message) {
            this.sendSuccessMessage = message;
        }

        @Override
        public void prepareFailView(final String errorMessage) {
            this.failMessage = errorMessage;
        }
    }
}
