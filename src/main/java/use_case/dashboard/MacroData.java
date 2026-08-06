package use_case.dashboard;

/**
 * Stores the user's macronutrient totals.
 */
public class MacroData {

    private final double protein;
    private final double carbs;
    private final double fat;

    /**
     * Constructs macronutrient data.
     *
     * @param protein total protein in grams
     * @param carbs total carbohydrates in grams
     * @param fat total fat in grams
     */
    public MacroData(
            final double protein,
            final double carbs,
            final double fat
    ) {
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