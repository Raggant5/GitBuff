package entity;

public enum FoodUnit {
    GRAM("g", 1),
    DEFAULT_SERVING("serving", 0),
    CUP("cup", 240),
    TABLESPOON("tbsp", 15),
    TEASPOON("tsp", 5);

    private final String displayName;
    private final double gramsPerUnit;

    FoodUnit(String displayName, double gramsPerUnit) {
        this.displayName = displayName;
        this.gramsPerUnit = gramsPerUnit;
    }

    /**
     * Fixed grams-per-unit conversion rate. Not meaningful for DEFAULT_SERVING,
     * whose gram equivalent instead comes from the selected search result's serving size.
     */
    public double getGramsPerUnit() {
        return gramsPerUnit;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
