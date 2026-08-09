package interface_adapter.nutrition.food;

import entity.FoodUnit;

/**
 * Owns the serving-size/quantity/unit math for the food editor: given per-serving macros,
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
    private FoodUnit unit = FoodUnit.GRAM;

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
        unit = FoodUnit.GRAM;
    }

    public String getServingLabel() {
        return servingLabel;
    }

    public void setOriginalServingGrams(double originalServingGrams) {
        this.originalServingGrams = originalServingGrams;
    }

    public void setServingGrams(double servingGrams) {
        this.servingGrams = servingGrams;
    }

    public void setServingCalories(double servingCalories) {
        this.servingCalories = servingCalories;
    }

    public void setServingProtein(double servingProtein) {
        this.servingProtein = servingProtein;
    }

    public void setServingCarbs(double servingCarbs) {
        this.servingCarbs = servingCarbs;
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
        if (quantityValue == null) {
            return;
        }

        totalGramsDisplay = formatTotal(servingGrams * quantityValue);
        totalCaloriesDisplay = formatTotal(servingCalories * quantityValue);
        totalProteinDisplay = formatTotal(servingProtein * quantityValue);
        totalCarbsDisplay = formatTotal(servingCarbs * quantityValue);
        totalFatDisplay = formatTotal(servingFat * quantityValue);
    }

    private void updateServingSize() {
        final double newServingGrams;
        if (unit == FoodUnit.DEFAULT_SERVING) {
            if (originalServingGrams != 0) {
                newServingGrams = originalServingGrams;
            }
            else {
                newServingGrams = servingGrams;
            }
        }
        else {
            newServingGrams = unit.getGramsPerUnit();
        }
        if (servingGrams != 0) {
            final double ratio = newServingGrams / servingGrams;
            servingCalories *= ratio;
            servingProtein *= ratio;
            servingCarbs *= ratio;
            servingFat *= ratio;
        }
        servingGrams = newServingGrams;
    }

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

    public void setQuantity(String quantity) {
        this.quantity = quantity;
        recalculateTotals();
    }

    public FoodUnit getUnit() {
        return unit;
    }

    public void setUnit(FoodUnit unit) {
        this.unit = unit;
        updateServingSize();
        recalculateTotals();
    }

    private double getQuantityValue() {
        return parseDouble(quantity);
    }

    private double parseDouble(String value) {
        final Double result = parseDoubleOrNull(value);
        final double fallback = 0;
        return result == null ? fallback : result;
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
