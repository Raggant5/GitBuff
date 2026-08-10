package interface_adapter.nutrition.food;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Food Editor State.
 */
class FoodEditorStateTest {

    @Test
    void gettersAndSettersRoundTripValues() {
        final FoodEditorState state = new FoodEditorState();

        state.setEditingFoodEntryId(11);
        state.setSearchQuery("chicken");
        state.setFoodName("Chicken Breast");
        state.setCaloriesError("calories error");
        state.setProteinError("protein error");
        state.setCarbsError("carbs error");
        state.setFatError("fat error");
        state.setQuantityError("quantity error");
        state.setGramsError("grams error");
        state.setSubmitError("submit error");

        assertEquals(11, state.getEditingFoodEntryId());
        assertEquals("chicken", state.getSearchQuery());
        assertEquals("Chicken Breast", state.getFoodName());
        assertEquals("calories error", state.getCaloriesError());
        assertEquals("protein error", state.getProteinError());
        assertEquals("carbs error", state.getCarbsError());
        assertEquals("fat error", state.getFatError());
        assertEquals("quantity error", state.getQuantityError());
        assertEquals("grams error", state.getGramsError());
        assertEquals("submit error", state.getSubmitError());
        assertTrue(state.getSearchResults().isEmpty());
    }

    @Test
    void setSearchResultsReplacesList() {
        final FoodEditorState state = new FoodEditorState();
        final List<FoodSearchResultDisplayData> results = new ArrayList<>();
        results.add(new FoodSearchResultDisplayData("Apple", "1 medium", 182,
                new FoodMacroAmounts(95, 0.5, 25, 0.3), FoodUnitOption.DEFAULT_SERVING, 1));

        state.setSearchResults(results);

        assertEquals(1, state.getSearchResults().size());

        state.clearSearchResults();

        assertTrue(state.getSearchResults().isEmpty());
    }

    @Test
    void selectSearchResultPopulatesFoodNameAndServingDetailsAndClearsResults() {
        final FoodEditorState state = new FoodEditorState();
        state.setSearchResults(new ArrayList<>(List.of(new FoodSearchResultDisplayData("Apple", "1 medium", 182,
                new FoodMacroAmounts(95, 0.5, 25, 0.3), FoodUnitOption.DEFAULT_SERVING, 1))));

        final FoodMacroAmounts nutrition = new FoodMacroAmounts(95, 0.5, 25, 0.3);
        state.selectSearchResult("Apple", "1 medium", 182, nutrition, FoodUnitOption.DEFAULT_SERVING, 2);

        assertEquals("Apple", state.getFoodName());
        final FoodServingDetails servingDetails = state.getServingDetails();
        assertEquals("1 medium", servingDetails.getServingLabel());
        assertEquals(182, servingDetails.getServingGrams());
        assertEquals(182, servingDetails.getOriginalServingGrams());
        assertEquals(95, servingDetails.getServingCalories());
        assertEquals("2.0", servingDetails.getQuantity());
        assertEquals(FoodUnitOption.DEFAULT_SERVING, servingDetails.getUnit());
        assertTrue(state.getSearchResults().isEmpty());
    }

    @Test
    void resetClearsAllFieldsBackToDefaults() {
        final FoodEditorState state = new FoodEditorState();
        state.setEditingFoodEntryId(3);
        state.setSearchQuery("query");
        state.setSearchResults(new ArrayList<>(List.of(new FoodSearchResultDisplayData("Apple", "1 medium", 182,
                new FoodMacroAmounts(95, 0.5, 25, 0.3), FoodUnitOption.DEFAULT_SERVING, 1))));
        state.setFoodName("Apple");
        state.setCaloriesError("error");
        state.setProteinError("error");
        state.setCarbsError("error");
        state.setFatError("error");
        state.setQuantityError("error");
        state.setGramsError("error");
        state.setSubmitError("error");

        state.reset();

        assertNull(state.getEditingFoodEntryId());
        assertEquals("", state.getSearchQuery());
        assertTrue(state.getSearchResults().isEmpty());
        assertEquals("", state.getFoodName());
        assertEquals("", state.getCaloriesError());
        assertEquals("", state.getProteinError());
        assertEquals("", state.getCarbsError());
        assertEquals("", state.getFatError());
        assertEquals("", state.getQuantityError());
        assertEquals("", state.getGramsError());
        assertEquals("", state.getSubmitError());
    }
}
