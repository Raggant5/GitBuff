package entity;

/**
 * Represents fitness equipment available to a user.
 */
public enum Equipment {

    BODYWEIGHT_ONLY("Bodyweight Only"),
    DUMBBELLS("Dumbbells"),
    BARBELL("Barbell"),
    KETTLEBELL("Kettlebell"),
    RESISTANCE_BANDS("Resistance Bands"),
    PULL_UP_BAR("Pull-Up Bar"),
    BENCH("Bench"),
    SQUAT_RACK("Squat Rack"),
    STATIONARY_BIKE("Stationary Bike"),
    TREADMILL("Treadmill"),
    ROWING_MACHINE("Rowing Machine"),
    CABLE_MACHINE("Cable Machine");

    private final String displayName;

    Equipment(final String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets the display name of the equipment.
     *
     * @return display name string
     */
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public String toString() {
        return this.displayName;
    }
}
