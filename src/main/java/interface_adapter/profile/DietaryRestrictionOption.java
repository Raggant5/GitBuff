package interface_adapter.profile;

/**
 * Display option mirroring {@code entity.DietaryRestriction}, for use by {@link ProfileState}
 * and {@code view.ProfileView}. See {@link ActivityLevelOption} for why the constant names and
 * order are kept identical to the entity enum.
 */
public enum DietaryRestrictionOption {

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

    DietaryRestrictionOption(final String displayName) {
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

