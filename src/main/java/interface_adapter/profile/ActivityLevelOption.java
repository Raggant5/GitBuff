package interface_adapter.profile;

/**
 * Display option mirroring {@code entity.ActivityLevel}, for use by {@link ProfileState} and
 * {@code view.ProfileView}.
 *
 * <p>The entity version carries a business rule (its calorie multiplier, used by
 * {@code use_case.recommendation.StandardCalorieCalculationStrategy}); this interface_adapter
 * layer has no business reason to know that value, so it isn't duplicated here. Only the
 * enum constant names and the display text are mirrored - kept in the same declaration order
 * and with the same constant names as {@code entity.ActivityLevel} so
 * {@link ProfileEnumMapper} is a trivial 1:1 lookup and existing name-based comparisons
 * elsewhere in the view layer continue to behave identically.
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

