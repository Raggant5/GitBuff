package interface_adapter.nutrition.food;

import java.util.ArrayList;
import java.util.List;

import entity.FoodSearchResult;

public class FoodEditorState {

    private Integer editingFoodEntryId;

    private String searchQuery = "";
    private List<FoodSearchResult> searchResults = new ArrayList<>();

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

    public List<FoodSearchResult> getSearchResults() {
        return searchResults;
    }

    public void setSearchResults(List<FoodSearchResult> searchResults) {
        this.searchResults = searchResults;
    }

    /**
     * Removes all search results stored.
     */
    public void clearSearchResults() {
        this.searchResults.clear();
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
