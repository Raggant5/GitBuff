package interface_adapter.dashboard;

/**
 * Display-only view of the user's macronutrient totals.
 */
public class MacroDisplayData {

    private final double protein;
    private final double carbs;
    private final double fat;

    /**
     * Constructs a MacroDisplayData instance.
     *
     * @param protein total protein in grams
     * @param carbs total carbohydrates in grams
     * @param fat total fat in grams
     */
    public MacroDisplayData(final double protein, final double carbs, final double fat) {
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
    }

    public double getProtein() {
        return this.protein;
    }

    public double getCarbs() {
        return this.carbs;
    }

    public double getFat() {
        return this.fat;
    }
}
