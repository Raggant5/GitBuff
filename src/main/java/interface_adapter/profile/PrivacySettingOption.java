package interface_adapter.profile;

/**
 * Display option mirroring {@code entity.PrivacySetting}, for use by {@link ProfileState} and
 * {@code view.ProfileView}. See {@link ActivityLevelOption} for why the constant names and
 * order are kept identical to the entity enum.
 */
public enum PrivacySettingOption {

    SHARE_PROFILE("Share Profile Bio & Picture"),
    SHARE_WORKOUT_LOGS("Share Completed Workouts"),
    SHARE_PERSONAL_RECORDS("Share Personal Records (PRs)"),
    SHARE_STREAK("Share Workout Streak"),
    SHARE_MEAL_LOGS("Share Meal Logs");

    private final String displayName;

    PrivacySettingOption(final String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public String toString() {
        return this.displayName;
    }
}

