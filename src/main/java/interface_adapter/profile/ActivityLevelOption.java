package interface_adapter.profile;

/**
 * Display option mirroring entity.ActivityLevel, for use by ProfileState and view.ProfileView.
 */
public enum ActivityLevelOption {

    NOT_ACTIVE("Not active (little or no exercise)"),
    LIGHTLY_ACTIVE("Lightly active (light exercise 1-3 days/week)"),
    MODERATELY_ACTIVE("Moderately active (moderate exercise 3-5 days/week)"),
    VERY_ACTIVE("Very active (hard exercise 6-7 days/week)"),
    EXTRA_ACTIVE("Extra active (very hard exercise & physical job)");

    private final String description;

    ActivityLevelOption(final String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    @Override
    public String toString() {
        return this.description;
    }
}

