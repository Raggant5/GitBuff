package entity;

/**
 * Represents measurement systems supported by the application.
 */
public enum UnitSystem {

    METRIC("Metric (kg, cm)"),
    IMPERIAL("Imperial (lbs, in)");

    private final String displayName;

    UnitSystem(final String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets the display string for the unit system.
     *
     * @return display string
     */
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public String toString() {
        return this.displayName;
    }
}
