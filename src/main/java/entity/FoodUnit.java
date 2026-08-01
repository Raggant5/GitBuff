package entity;

public enum FoodUnit {
    GRAM("g"),
    KILOGRAM("kg"),
    MILLILITER("ml"),
    LITER("L"),
    CUP("cup"),
    TABLESPOON("tbsp"),
    TEASPOON("tsp"),
    OUNCE("oz"),
    POUND("lb"),
    PIECE("piece");

    private final String displayName;

    FoodUnit(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
