package use_case.share;

import java.util.List;

import entity.ExercisePerformed;
import entity.LoggedWorkout;
import entity.PrivacySetting;
import entity.User;

/**
 * Interactor for aggregating and sharing user progress according to active privacy settings.
 */
public class ShareProgressInteractor implements ShareProgressInputBoundary {

    private final ShareProgressUserDataAccessInterface userDataAccessObject;
    private final ShareEmailDataAccessInterface emailDataAccessObject;
    private final ShareProgressOutputBoundary presenter;

    /**
     * Constructs ShareProgressInteractor instance.
     *
     * @param userDataAccessObject user and workout data access object
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

        final String formattedReport = buildShareReport(user);
        if (formattedReport == null) {
            this.presenter.prepareFailView("No shareable privacy settings enabled in your profile!");
            return;
        }

        this.presenter.preparePreviewView(new ShareProgressOutputData(formattedReport, user.getProfilePicturePath()));
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

        final String formattedReport = buildShareReport(user);
        if (formattedReport == null) {
            this.presenter.prepareFailView("No shareable privacy settings enabled in your profile!");
            return;
        }

        final boolean success = this.emailDataAccessObject.sendEmail(
                recipientEmail,
                "GitBuff Progress Update from " + user.getName(),
                formattedReport,
                user.getProfilePicturePath()
        );

        if (success) {
            this.presenter.prepareSendSuccessView("Progress shared successfully with " + recipientEmail + "!");
        }
        else {
            this.presenter.prepareFailView("Failed to send email. Opening default mail application...");
        }
    }

    private String buildShareReport(final User user) {
        final StringBuilder sb = new StringBuilder();
        sb.append("Fitness Progress Report for ").append(user.getName()).append("\n\n");

        boolean contentAdded = false;

        if (user.getPrivacySettings() != null) {
            if (user.getPrivacySettings().contains(PrivacySetting.SHARE_PROFILE)) {
                contentAdded = true;
                sb.append("--- PROFILE DETAILS ---\n");
                sb.append("Bio: ").append(user.getBio() != null && !user.getBio().isBlank() ? user.getBio() : "None").append("\n");
                sb.append("Goal: ").append(user.getGoal() != null ? user.getGoal().toString() : "Not set").append("\n");
                sb.append("Height: ").append(String.format("%.2f m", user.getHeight())).append("\n");
                sb.append("Weight: ").append(String.format("%.1f kg", user.getWeight())).append("\n\n");
            }

            if (user.getPrivacySettings().contains(PrivacySetting.SHARE_WORKOUT_LOGS)) {
                contentAdded = true;
                final List<LoggedWorkout> workouts = this.userDataAccessObject.getWorkoutsForUser(user.getName());
                final int totalWorkouts = workouts != null ? workouts.size() : 0;

                sb.append("--- COMPLETED WORKOUTS ---\n");
                sb.append("Total Workouts Completed: ").append(totalWorkouts).append("\n");

                if (workouts != null && !workouts.isEmpty()) {
                    sb.append("Recent Activities:\n");
                    for (final LoggedWorkout workout : workouts) {
                        sb.append(" • Date: ").append(workout.getDate()).append("\n");
                        if (workout.getExercises() != null) {
                            for (final ExercisePerformed exercise : workout.getExercises()) {
                                sb.append("   - ").append(exercise.getExerciseName())
                                        .append(" (").append((int) exercise.getDurationMins()).append(" mins)\n");
                            }
                        }
                    }
                }
                sb.append("\n");
            }

            if (user.getPrivacySettings().contains(PrivacySetting.SHARE_PERSONAL_RECORDS)) {
                contentAdded = true;
                final double totalMinutes = this.userDataAccessObject.getTotalMinutesWorkedOut(user.getName());
                sb.append("--- PERSONAL RECORDS (PRs) ---\n");
                sb.append("Total Time Worked Out (All-Time): ").append((int) Math.round(totalMinutes)).append(" minutes\n\n");
            }
        }

        return contentAdded ? sb.toString() : null;
    }
}
