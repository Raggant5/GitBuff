package use_case.share.report;

import entity.User;

/**
 * Leaf {@link ReportSection} presenting the user's bio, fitness goal, height, and weight.
 *
 * <p>Included in the report only when the user has enabled
 * {@code PrivacySetting.SHARE_PROFILE}; see {@code use_case.share.ShareProgressInteractor}.
 */
public class ProfileReportSection implements ReportSection {

    private final User user;

    /**
     * Constructs a profile section for the given user.
     *
     * @param user the user whose profile details are rendered
     */
    public ProfileReportSection(final User user) {
        this.user = user;
    }

    @Override
    public boolean hasContent() {
        return true;
    }

    @Override
    public String render() {
        final String bio = this.user.getBio() != null && !this.user.getBio().isBlank()
                ? this.user.getBio() : "None";
        final String goal = this.user.getGoal() != null ? this.user.getGoal().toString() : "Not set";

        final StringBuilder builder = new StringBuilder();
        builder.append("--- PROFILE DETAILS ---\n");
        builder.append("Bio: ").append(bio).append("\n");
        builder.append("Goal: ").append(goal).append("\n");
        builder.append("Height: ").append(String.format("%.2f m", this.user.getHeight())).append("\n");
        builder.append("Weight: ").append(String.format("%.1f kg", this.user.getWeight())).append("\n\n");
        return builder.toString();
    }
}

