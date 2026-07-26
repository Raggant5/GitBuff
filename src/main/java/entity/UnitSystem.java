package entity;

public enum UnitSystem {
    METRIC("Metric (kg, cm)"),
    IMPERIAL("Imperial (lbs, in)");

    private final String displayName;

    UnitSystem(String displayName) {
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
