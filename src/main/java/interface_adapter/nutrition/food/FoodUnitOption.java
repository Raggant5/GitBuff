package interface_adapter.nutrition.food;

/**
 * Display option mirroring entity.FoodUnit, for use by view.FoodEditorView and the food display
 * data/state classes in this layer.
 */
public enum FoodUnitOption {

    GRAM("g"),
    DEFAULT_SERVING("serving"),
    CUP("cup"),
    TABLESPOON("tbsp"),
    TEASPOON("tsp");

    private final String displayName;

    FoodUnitOption(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
