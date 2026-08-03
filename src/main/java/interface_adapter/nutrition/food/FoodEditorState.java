package interface_adapter.nutrition.food;

import java.util.ArrayList;
import java.util.List;

import entity.FoodEntry;
import entity.FoodSearchResult;
import entity.FoodUnit;

public class FoodEditorState {

    private FoodEntry editingFood;

    private String searchQuery = "";
    private List<FoodSearchResult> searchResults = new ArrayList<>();

    private String foodName = "";
    private String servingLabel = "";
    private double originalServingGrams;

    private double servingGrams;
    private double servingCalories;
    private double servingProtein;
    private double servingCarbs;
    private double servingFat;

    private String totalCaloriesDisplay = "0.0";
    private String totalGramsDisplay = "0.0";
    private String totalProteinDisplay = "0.0";
    private String totalCarbsDisplay = "0.0";
    private String totalFatDisplay = "0.0";

    private String quantity = "1";
    private FoodUnit unit = FoodUnit.GRAM;

    private String error = "";
    private boolean searchSelected;

    /**
     * Resets the values when the food editor is no longer needed for reuse later.
     */
    public void reset() {
        editingFood = null;

        searchQuery = "";
        searchResults.clear();

        foodName = "";
        servingLabel = "";
        originalServingGrams = 0.0;

        servingGrams = 0.0;
        servingCalories = 0.0;
        servingProtein = 0.0;
        servingCarbs = 0.0;
        servingFat = 0.0;

        totalCaloriesDisplay = "0.0";
        totalGramsDisplay = "0.0";
        totalProteinDisplay = "0.0";
        totalCarbsDisplay = "0.0";
        totalFatDisplay = "0.0";

        quantity = "1";
        unit = FoodUnit.GRAM;

        error = "";
        searchSelected = false;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public boolean isSearchSelected() {
        return searchSelected;
    }

    public void setSearchSelected(boolean searchSelected) {
        this.searchSelected = searchSelected;
    }

    public List<FoodSearchResult> getSearchResults() {
        return searchResults;
    }

    public void setSearchResults(List<FoodSearchResult> searchResults) {
        this.searchResults = searchResults;
    }

    public FoodEntry getEditingFood() {
        return editingFood;
    }

    public void setEditingFood(FoodEntry editingFood) {
        this.editingFood = editingFood;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getServingLabel() {
        return servingLabel;
    }

    public void setServingLabel(String servingLabel) {
        this.servingLabel = servingLabel;
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

    public void recalculateTotals() {
        final double quantityValue = getQuantityValue();

        totalGramsDisplay = String.valueOf(servingGrams * quantityValue);
        totalCaloriesDisplay = String.valueOf(servingCalories * quantityValue);
        totalProteinDisplay = String.valueOf(servingProtein * quantityValue);
        totalCarbsDisplay = String.valueOf(servingCarbs * quantityValue);
        totalFatDisplay = String.valueOf(servingFat * quantityValue);
    }

    private void updateServingSize() {
        final double newServingGrams;
        if (unit == FoodUnit.DEFAULT_SERVING) {
            if (originalServingGrams == 0) {
                return;
            }
            newServingGrams = originalServingGrams;
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

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    private double getQuantityValue() {
        // should be handled elsewhere first
        try {
            return parseDouble(quantity);
        }
        catch (NumberFormatException exception) {
            return 0;
        }
    }

    private double parseDouble(String value) {
        try {
            return Double.parseDouble(value);
        }
        catch (NumberFormatException exception) {
            return 0;
        }
    }
}
