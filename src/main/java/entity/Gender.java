package entity;

/**
 * Represents gender identity choices for a user profile.
 */
public enum Gender {

    MALE("Male"),
    FEMALE("Female"),
    OTHER("Other"),
    PREFER_NOT_TO_SAY("Prefer Not to Say");

    private final String displayName;

    Gender(final String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets the display string for gender.
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
