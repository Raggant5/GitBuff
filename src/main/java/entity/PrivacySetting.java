package entity;

public enum PrivacySetting {
    SHARE_PROFILE("Share Profile Bio & Picture"),
    SHARE_WORKOUT_LOGS("Share Workout Activity"),
    SHARE_PERSONAL_RECORDS("Share Personal Records (PRs)"),
    SHARE_STREAK("Share Workout Streak"),
    SHARE_MEAL_LOGS("Share Meal Logs");

    private final String displayName;

    PrivacySetting(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
