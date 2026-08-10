package interface_adapter.nutrition.food;

/**
 * Contains serving-size/quantity/unit math for the food editor: given per-serving macros,
 * a quantity, and a unit, derives the displayed totals and keeps them in sync as any of
 * those inputs change.
 */
public class FoodServingDetails {

    private static final String NUTRITIONAL_DEFAULT_VALUE = "0.0";
    private static final double DISPLAY_ROUNDING_FACTOR = 100.0;

    private String servingLabel = "";
    private double originalServingGrams;

    private double servingGrams;
    private double servingCalories;
    private double servingProtein;
    private double servingCarbs;
    private double servingFat;

    private String totalCaloriesDisplay = NUTRITIONAL_DEFAULT_VALUE;
    private String totalGramsDisplay = NUTRITIONAL_DEFAULT_VALUE;
    private String totalProteinDisplay = NUTRITIONAL_DEFAULT_VALUE;
    private String totalCarbsDisplay = NUTRITIONAL_DEFAULT_VALUE;
    private String totalFatDisplay = NUTRITIONAL_DEFAULT_VALUE;

    private String quantity = "1";
    private FoodUnitOption unit = FoodUnitOption.GRAM;

    /**
     * Resets the values when the food editor is no longer needed for reuse later.
     */
    public void reset() {
        servingLabel = "";
        originalServingGrams = 0.0;

        servingGrams = 0.0;
        servingCalories = 0.0;
        servingProtein = 0.0;
        servingCarbs = 0.0;
        servingFat = 0.0;

        totalCaloriesDisplay = NUTRITIONAL_DEFAULT_VALUE;
        totalGramsDisplay = NUTRITIONAL_DEFAULT_VALUE;
        totalProteinDisplay = NUTRITIONAL_DEFAULT_VALUE;
        totalCarbsDisplay = NUTRITIONAL_DEFAULT_VALUE;
        totalFatDisplay = NUTRITIONAL_DEFAULT_VALUE;

        quantity = "1";
        unit = FoodUnitOption.GRAM;
    }

    public String getServingLabel() {
        return servingLabel;
    }

    public double getOriginalServingGrams() {
        return originalServingGrams;
    }

    public void setOriginalServingGrams(double originalServingGrams) {
        this.originalServingGrams = originalServingGrams;
    }

    public double getServingGrams() {
        return servingGrams;
    }

    public void setServingGrams(double servingGrams) {
        this.servingGrams = servingGrams;
    }

    public double getServingCalories() {
        return servingCalories;
    }

    public void setServingCalories(double servingCalories) {
        this.servingCalories = servingCalories;
    }

    public double getServingProtein() {
        return servingProtein;
    }

    public void setServingProtein(double servingProtein) {
        this.servingProtein = servingProtein;
    }

    public double getServingCarbs() {
        return servingCarbs;
    }

    public void setServingCarbs(double servingCarbs) {
        this.servingCarbs = servingCarbs;
    }

    public double getServingFat() {
        return servingFat;
    }

    public void setServingFat(double servingFat) {
        this.servingFat = servingFat;
    }

    public String getTotalCaloriesDisplay() {
        return totalCaloriesDisplay;
    }

    /**
     * Recomputes displayed totals from the per-serving macros and current quantity.
     * Leaves the displayed totals untouched while the quantity is blank/unparseable
     * (e.g. mid-edit), rather than flashing them to zero.
     */
    public void recalculateTotals() {
        final Double quantityValue = parseDoubleOrNull(quantity);
        if (quantityValue != null) {
            totalGramsDisplay = formatTotal(servingGrams * quantityValue);
            totalCaloriesDisplay = formatTotal(servingCalories * quantityValue);
            totalProteinDisplay = formatTotal(servingProtein * quantityValue);
            totalCarbsDisplay = formatTotal(servingCarbs * quantityValue);
            totalFatDisplay = formatTotal(servingFat * quantityValue);
        }
    }

    /**
     * Sets the total calories to be displayed and adjusts the serving calories based on quantity and total.
     * @param value the new value to be set for total calories
     */
    public void setTotalCaloriesDisplay(String value) {
        totalCaloriesDisplay = value;
        final double quantityValue = getQuantityValue();
        if (quantityValue != 0) {
            servingCalories = parseDouble(value) / quantityValue;
        }
    }

    public String getTotalGramsDisplay() {
        return totalGramsDisplay;
    }

    /**
     * Sets the total grams to be displayed and adjusts the serving amounts based on the ratio of new vs old.
     * @param value the new value to be set for total grams
     */
    public void setTotalGramsDisplay(String value) {
        final double oldServingGrams = servingGrams;
        totalGramsDisplay = value;
        final double quantityValue = getQuantityValue();
        if (quantityValue != 0) {
            servingGrams = parseDouble(value) / quantityValue;
        }
        if (oldServingGrams != 0) {
            final double ratio = servingGrams / oldServingGrams;
            servingCalories *= ratio;
            servingProtein *= ratio;
            servingCarbs *= ratio;
            servingFat *= ratio;
        }
        recalculateTotals();
    }

    public String getTotalProteinDisplay() {
        return totalProteinDisplay;
    }

    /**
     * Sets the total protein to be displayed and adjusts the serving protein based on quantity and total.
     * @param value the new value to be set for total protein
     */
    public void setTotalProteinDisplay(String value) {
        totalProteinDisplay = value;
        final double quantityValue = getQuantityValue();
        if (quantityValue != 0) {
            servingProtein = parseDouble(value) / quantityValue;
        }
    }

    public String getTotalCarbsDisplay() {
        return totalCarbsDisplay;
    }

    /**
     * Sets the total carbs to be displayed and adjusts the serving carbs based on quantity and total.
     * @param value the new value to be set for total carbs
     */
    public void setTotalCarbsDisplay(String value) {
        totalCarbsDisplay = value;
        final double quantityValue = getQuantityValue();
        if (quantityValue != 0) {
            servingCarbs = parseDouble(value) / quantityValue;
        }
    }

    public String getTotalFatDisplay() {
        return totalFatDisplay;
    }

    /**
     * Sets the total fat to be displayed and adjusts the serving fat based on quantity and total.
     * @param value the new value to be set for total fat
     */
    public void setTotalFatDisplay(String value) {
        totalFatDisplay = value;
        final double quantityValue = getQuantityValue();
        if (quantityValue != 0) {
            servingFat = parseDouble(value) / quantityValue;
        }
    }

    public String getQuantity() {
        return quantity;
    }

    /**
     * Sets the total quantity to be displayed and adjusts the total amounts per nutritional category.
     * @param quantity the new value for the amount
     */
    public void setQuantity(String quantity) {
        this.quantity = quantity;
        recalculateTotals();
    }

    public FoodUnitOption getUnit() {
        return unit;
    }

    /**
     * Sets the unit to be displayed.
     * @param unit the new unit for the food to be measured in
     */
    public void setUnit(FoodUnitOption unit) {
        this.unit = unit;
    }

    private double getQuantityValue() {
        return parseDouble(quantity);
    }

    private double parseDouble(String value) {
        double result = 0;
        final Double parseDoubleOrNull = parseDoubleOrNull(value);
        if (parseDoubleOrNull == null) {
            result = 0;
        }
        else {
            result = parseDoubleOrNull;
        }
        return result;
    }

    private Double parseDoubleOrNull(String value) {
        Double result;
        if (value == null) {
            result = null;
        }
        else {
            try {
                result = Double.parseDouble(value);
            }
            catch (NumberFormatException exception) {
                result = null;
            }
        }
        return result;
    }

    private String formatTotal(double value) {
        return String.valueOf(Math.round(value * DISPLAY_ROUNDING_FACTOR) / DISPLAY_ROUNDING_FACTOR);
    }

    /**
     *  Sets the data relating to serving all at once.
     * @param newServingLabel the label / name for each serving
     * @param newOriginalServingGrams the initial serving grams, only if selected via search
     * @param newServingGrams the new serving grams to set
     * @param newServingCalories the new serving calories to be set
     * @param newServingProtein the new serving protein to be set
     * @param newServingCarbs the new serving carbs to be set
     * @param newServingFat the new serving fat to be set
     */
    public void setServingData(
            final String newServingLabel,
            final double newOriginalServingGrams,
            final double newServingGrams,
            final double newServingCalories,
            final double newServingProtein,
            final double newServingCarbs,
            final double newServingFat
    ) {
        this.servingLabel = newServingLabel;
        this.originalServingGrams = newOriginalServingGrams;
        this.servingGrams = newServingGrams;
        this.servingCalories = newServingCalories;
        this.servingProtein = newServingProtein;
        this.servingCarbs = newServingCarbs;
        this.servingFat = newServingFat;
    }
}
