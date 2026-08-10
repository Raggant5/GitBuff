package interface_adapter.nutrition.food;

import java.util.ArrayList;
import java.util.List;

public class FoodEditorState {

    private Integer editingFoodEntryId;

    private String searchQuery = "";
    private List<FoodSearchResultDisplayData> searchResults = new ArrayList<>();

    private String foodName = "";

    private final FoodServingDetails servingDetails = new FoodServingDetails();

    private String caloriesError = "";
    private String proteinError = "";
    private String carbsError = "";
    private String fatError = "";
    private String quantityError = "";
    private String gramsError = "";
    private String submitError = "";

    /**
     * Resets the values when the food editor is no longer needed for reuse later.
     */
    public void reset() {
        editingFoodEntryId = null;

        searchQuery = "";
        searchResults.clear();

        foodName = "";
        servingDetails.reset();

        caloriesError = "";
        proteinError = "";
        carbsError = "";
        fatError = "";
        quantityError = "";
        gramsError = "";
        submitError = "";
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public List<FoodSearchResultDisplayData> getSearchResults() {
        return searchResults;
    }

    public void setSearchResults(List<FoodSearchResultDisplayData> searchResults) {
        this.searchResults = searchResults;
    }

    /**
     * Removes all search results stored.
     */
    public void clearSearchResults() {
        this.searchResults.clear();
    }

    /**
     * Populates the food name and serving details from a selected search result.
     * @param newFoodName the selected food's name
     * @param servingLabel the selected food's serving label
     * @param servingGrams the selected food's serving size in grams
     * @param nutrition the selected food's per-serving calories/protein/carbs/fat
     * @param unit the unit to display the serving in
     * @param quantity the quantity of the unit
     */
    public void selectSearchResult(
            final String newFoodName,
            final String servingLabel,
            final double servingGrams,
            final FoodMacroAmounts nutrition,
            final FoodUnitOption unit,
            final double quantity
    ) {
        this.foodName = newFoodName;
        servingDetails.setServingData(servingLabel, servingGrams, servingGrams, nutrition.getCalories(),
                nutrition.getProtein(), nutrition.getCarbs(), nutrition.getFat());
        servingDetails.setQuantity(String.valueOf(quantity));
        servingDetails.setUnit(unit);
        servingDetails.recalculateTotals();
        clearSearchResults();
    }

    public Integer getEditingFoodEntryId() {
        return editingFoodEntryId;
    }

    public void setEditingFoodEntryId(Integer editingFoodEntryId) {
        this.editingFoodEntryId = editingFoodEntryId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public FoodServingDetails getServingDetails() {
        return servingDetails;
    }

    public String getCaloriesError() {
        return caloriesError;
    }

    public void setCaloriesError(String caloriesError) {
        this.caloriesError = caloriesError;
    }

    public String getProteinError() {
        return proteinError;
    }

    public void setProteinError(String proteinError) {
        this.proteinError = proteinError;
    }

    public String getCarbsError() {
        return carbsError;
    }

    public void setCarbsError(String carbsError) {
        this.carbsError = carbsError;
    }

    public String getFatError() {
        return fatError;
    }

    public void setFatError(String fatError) {
        this.fatError = fatError;
    }

    public String getQuantityError() {
        return quantityError;
    }

    public void setQuantityError(String quantityError) {
        this.quantityError = quantityError;
    }

    public String getGramsError() {
        return gramsError;
    }

    public void setGramsError(String gramsError) {
        this.gramsError = gramsError;
    }

    public String getSubmitError() {
        return submitError;
    }

    public void setSubmitError(String submitError) {
        this.submitError = submitError;
    }
}
