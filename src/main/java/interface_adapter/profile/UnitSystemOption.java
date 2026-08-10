package interface_adapter.profile;

/**
 * Display option mirroring {@code entity.UnitSystem}, for use by {@link ProfileState} and
 * {@code view.ProfileView}. See {@link ActivityLevelOption} for why the constant names and
 * order are kept identical to the entity enum.
 */
public enum UnitSystemOption {

    METRIC("Metric (kg, cm)"),
    IMPERIAL("Imperial (lbs, in)");

    private final String displayName;

    UnitSystemOption(final String displayName) {
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

