package interface_adapter.profile;

/**
 * Display option mirroring entity.FitnessGoal, for use by ProfileState and view.ProfileView.
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

