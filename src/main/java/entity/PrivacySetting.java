package entity;

/**
 * Options for configuring user privacy and data sharing settings.
 */
public enum PrivacySetting {

    SHARE_PROFILE("Share Profile Bio & Picture"),
    SHARE_WORKOUT_LOGS("Share Completed Workouts"),
    SHARE_PERSONAL_RECORDS("Share Personal Records (PRs)"),
    SHARE_STREAK("Share Workout Streak"),
    SHARE_MEAL_LOGS("Share Meal Logs");

    private final String displayName;

    PrivacySetting(final String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets the display string for privacy setting.
     *
     * @return display string.
     */
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public String toString() {
        return this.displayName;
    }
}
