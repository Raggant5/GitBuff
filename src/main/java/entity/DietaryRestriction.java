package entity;

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

    DietaryRestriction(String displayName) {
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
