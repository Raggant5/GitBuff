package interface_adapter.profile;

/**
 * Display option mirroring {@code entity.FitnessGoal}, for use by {@link ProfileState} and
 * {@code view.ProfileView}.
 *
 * <p>The entity version carries business rules (calorie adjustment, protein ratio, workout
 * focus text, all used by {@code use_case.recommendation}); this interface_adapter layer only
 * needs the display name, so that's all that's mirrored here. See {@link ActivityLevelOption}
 * for why the constant names and order are kept identical to the entity enum.
 */
public enum FitnessGoalOption {

    LOSE_WEIGHT("Lose Weight"),
    MAINTAIN_GENERAL_FITNESS("Maintain Weight & General Fitness"),
    MUSCLE_AND_STRENGTH_GAIN("Muscle & Strength Gain"),
    INCREASE_ENDURANCE("Increase Endurance"),
    FLEXIBILITY_AND_MOBILITY("Flexibility & Mobility");

    private final String displayName;

    FitnessGoalOption(final String displayName) {
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

