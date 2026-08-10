package use_case.share;

import java.util.List;
import java.util.Set;

import entity.LoggedWorkout;
import entity.PrivacySetting;
import entity.User;
import use_case.share.report.CompositeReportSection;
import use_case.share.report.PersonalRecordsReportSection;
import use_case.share.report.ProfileReportSection;
import use_case.share.report.WorkoutLogReportSection;

/**
 * Interactor for aggregating and sharing user progress according to active privacy settings.
 * The report is assembled with the Composite design pattern: each enabled PrivacySetting adds
 * one ReportSection leaf to a CompositeReportSection, which is then rendered as a whole.
 */
public class ShareProgressInteractor implements ShareProgressInputBoundary {

    private static final String NO_SHAREABLE_CONTENT_MESSAGE =
            "No shareable privacy settings enabled in your profile!";

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
            this.presenter.prepareFailView(NO_SHAREABLE_CONTENT_MESSAGE);
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
            this.presenter.prepareFailView(NO_SHAREABLE_CONTENT_MESSAGE);
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

    /**
     * Builds the shareable report by composing one {@code ReportSection} leaf per privacy
     * setting the user has enabled.
     *
     * @param user the currently logged-in user
     * @return the rendered report text, or {@code null} if no privacy setting is enabled
     */
    private String buildShareReport(final User user) {
        final CompositeReportSection report =
                new CompositeReportSection("Fitness Progress Report for " + user.getName());

        final Set<PrivacySetting> privacySettings = user.getPrivacySettings();
        if (privacySettings != null) {
            if (privacySettings.contains(PrivacySetting.SHARE_PROFILE)) {
                report.add(new ProfileReportSection(user));
            }
            if (privacySettings.contains(PrivacySetting.SHARE_WORKOUT_LOGS)) {
                final List<LoggedWorkout> workouts = this.userDataAccessObject.getWorkoutsForUser(user.getName());
                report.add(new WorkoutLogReportSection(workouts));
            }
            if (privacySettings.contains(PrivacySetting.SHARE_PERSONAL_RECORDS)) {
                final double totalMinutes = this.userDataAccessObject.getTotalMinutesWorkedOut(user.getName());
                report.add(new PersonalRecordsReportSection(totalMinutes));
            }
        }

        return report.hasContent() ? report.render() : null;
    }
}



