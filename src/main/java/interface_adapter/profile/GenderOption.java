package interface_adapter.profile;

/**
 * Display option mirroring {@code entity.Gender}, for use by {@link ProfileState} and
 * {@code view.ProfileView}. See {@link ActivityLevelOption} for why the constant names and
 * order are kept identical to the entity enum.
 */
public enum GenderOption {

    MALE("Male"),
    FEMALE("Female"),
    OTHER("Other"),
    PREFER_NOT_TO_SAY("Prefer Not to Say");

    private final String displayName;

    GenderOption(final String displayName) {
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

