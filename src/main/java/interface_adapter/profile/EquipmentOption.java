package interface_adapter.profile;

/**
 * Display option mirroring {@code entity.Equipment}, for use by {@link ProfileState} and
 * {@code view.ProfileView}. See {@link ActivityLevelOption} for why the constant names and
 * order are kept identical to the entity enum.
 */
public enum EquipmentOption {

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

    EquipmentOption(final String displayName) {
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

