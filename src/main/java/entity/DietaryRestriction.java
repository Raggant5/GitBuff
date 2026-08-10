package entity;

/**
 * Represents dietary restrictions or preferences for a user.
 */
public enum DietaryRestriction {

    NONE("None"),
    VEGETARIAN("Vegetarian"),
    VEGAN("Vegan"),
    PESCATARIAN("Pescatarian"),
    HALAL("Halal"),
    KETO("Keto"),
    GLUTEN_FREE("Gluten-Free"),
    DAIRY_FREE("Dairy-Free"),
    NUT_ALLERGY("Nut Allergy");

    private final String displayName;

    DietaryRestriction(final String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets the display name of the dietary restriction.
     *
     * @return display name string.
     */
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public String toString() {
        return this.displayName;
    }
}
