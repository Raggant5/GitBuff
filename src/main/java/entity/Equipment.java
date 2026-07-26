package entity;

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

    Equipment(String displayName) {
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
