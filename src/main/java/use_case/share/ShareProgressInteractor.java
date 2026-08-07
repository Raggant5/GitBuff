package use_case.share;

import entity.PrivacySetting;
import entity.User;

/**
 * Interactor for aggregating and sharing progress according to user privacy settings.
 */
public class ShareProgressInteractor implements ShareProgressInputBoundary {

    private final ShareProgressUserDataAccessInterface userDataAccessObject;
    private final ShareEmailDataAccessInterface emailDataAccessObject;
    private final ShareProgressOutputBoundary presenter;

    /**
     * Constructs ShareProgressInteractor instance.
     *
     * @param userDataAccessObject user data access object
     * @param emailDataAccessObject email data access object
     * @param presenter output boundary presenter
     */
    public ShareProgressInteractor(final ShareProgressUserDataAccessInterface userDataAccessObject,
                                   final ShareEmailDataAccessInterface emailDataAccessObject,
                                   final ShareProgressOutputBoundary presenter) {
        this.userDataAccessObject = userDataAccessObject;
        this.emailDataAccessObject = emailDataAccessObject;
        this.presenter = presenter;
    }

    @Override
    public void prepareSharePreview() {
        final User user = this.userDataAccessObject.getCurrentUser();
        if (user == null) {
            this.presenter.prepareFailView("No user is currently logged in.");
            return;
        }

        final StringBuilder sb = new StringBuilder();
        sb.append("Fitness Progress Report for ").append(user.getName()).append("\n\n");

        String picturePath = null;
        boolean contentAdded = false;

        if (user.getPrivacySettings() != null) {
            if (user.getPrivacySettings().contains(PrivacySetting.SHARE_PROFILE)) {
                contentAdded = true;
                sb.append("--- PROFILE DETAILS ---\n");
                sb.append("Bio: ").append(user.getBio() != null ? user.getBio() : "None").append("\n");
                sb.append("Goal: ").append(user.getGoal() != null ? user.getGoal().toString() : "Not set").append("\n");
                sb.append("Height: ").append(String.format("%.2f m", user.getHeight())).append("\n");
                sb.append("Weight: ").append(String.format("%.1f kg", user.getWeight())).append("\n\n");
                picturePath = user.getProfilePicturePath();
            }

            if (user.getPrivacySettings().contains(PrivacySetting.SHARE_WORKOUT_LOGS)) {
                contentAdded = true;
                final int completedCount = this.userDataAccessObject.getTotalCompletedWorkouts(user.getName());
                sb.append("--- COMPLETED WORKOUTS ---\n");
                sb.append("Total Completed Workouts: ").append(completedCount).append("\n\n");
            }

            if (user.getPrivacySettings().contains(PrivacySetting.SHARE_PERSONAL_RECORDS)) {
                contentAdded = true;
                final int totalMinutes = this.userDataAccessObject.getTotalMinutesWorkedOut(user.getName());
                sb.append("--- PERSONAL RECORDS (PRs) ---\n");
                sb.append("Total Time Worked Out (All-Time): ").append(totalMinutes).append(" minutes\n\n");
            }
        }

        if (!contentAdded) {
            this.presenter.prepareFailView("No shareable privacy settings enabled in your profile!");
            return;
        }

        this.presenter.preparePreviewView(new ShareProgressOutputData(sb.toString(), picturePath));
    }

    @Override
    public void sendShareEmail(final String recipientEmail) {
        if (recipientEmail == null || !recipientEmail.contains("@") || !recipientEmail.contains(".")) {
            this.presenter.prepareFailView("Please enter a valid recipient email address.");
            return;
        }

        final User user = this.userDataAccessObject.getCurrentUser();
        if (user == null) {
            this.presenter.prepareFailView("No user session found.");
            return;
        }

        final StringBuilder sb = new StringBuilder();
        sb.append("Fitness Progress Report for ").append(user.getName()).append("\n\n");
        String picturePath = null;

        if (user.getPrivacySettings() != null) {
            if (user.getPrivacySettings().contains(PrivacySetting.SHARE_PROFILE)) {
                sb.append("--- PROFILE DETAILS ---\n");
                sb.append("Bio: ").append(user.getBio() != null ? user.getBio() : "None").append("\n");
                sb.append("Goal: ").append(user.getGoal() != null ? user.getGoal().toString() : "Not set").append("\n");
                sb.append("Height: ").append(String.format("%.2f m", user.getHeight())).append("\n");
                sb.append("Weight: ").append(String.format("%.1f kg", user.getWeight())).append("\n\n");
                picturePath = user.getProfilePicturePath();
            }

            if (user.getPrivacySettings().contains(PrivacySetting.SHARE_WORKOUT_LOGS)) {
                final int completedCount = this.userDataAccessObject.getTotalCompletedWorkouts(user.getName());
                sb.append("--- COMPLETED WORKOUTS ---\n");
                sb.append("Total Completed Workouts: ").append(completedCount).append("\n\n");
            }

            if (user.getPrivacySettings().contains(PrivacySetting.SHARE_PERSONAL_RECORDS)) {
                final int totalMinutes = this.userDataAccessObject.getTotalMinutesWorkedOut(user.getName());
                sb.append("--- PERSONAL RECORDS (PRs) ---\n");
                sb.append("Total Time Worked Out (All-Time): ").append(totalMinutes).append(" minutes\n\n");
            }
        }

        final boolean success = this.emailDataAccessObject.sendEmail(
                recipientEmail, "FitBuff Progress Update from " + user.getName(), sb.toString(), picturePath);

        if (success) {
            this.presenter.prepareSendSuccessView("Progress shared successfully with " + recipientEmail + "!");
        }
        else {
            this.presenter.prepareFailView("Failed to send email. Opening mail application...");
        }
    }
}
