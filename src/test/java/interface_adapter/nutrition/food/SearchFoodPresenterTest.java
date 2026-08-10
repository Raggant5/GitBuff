package interface_adapter.nutrition.food;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import entity.FoodUnit;
import use_case.nutrition.food.FoodNutritionData;
import use_case.nutrition.food.search_food.FoodSearchResultData;
import use_case.nutrition.food.search_food.SearchFoodOutputData;

/**
 * Unit tests for the Search Food Presenter.
 */
class SearchFoodPresenterTest {

    @Test
    void prepareSuccessViewMapsFoodResultsAndClearsSubmitError() {
        final FoodEditorViewModel foodEditorViewModel = new FoodEditorViewModel();
        final SearchFoodPresenter presenter = new SearchFoodPresenter(foodEditorViewModel);
        foodEditorViewModel.getState().setSubmitError("previous failure");

        final FoodSearchResultData resultData = new FoodSearchResultData("Apple", "1 medium", 182,
                new FoodNutritionData(95, 0.5, 25, 0.3), FoodUnit.DEFAULT_SERVING, 1);

        presenter.prepareSuccessView(new SearchFoodOutputData(List.of(resultData)));

        final FoodEditorState state = foodEditorViewModel.getState();
        assertEquals(1, state.getSearchResults().size());
        final FoodSearchResultDisplayData mapped = state.getSearchResults().get(0);
        assertEquals("Apple", mapped.getFoodName());
        assertEquals("1 medium", mapped.getServingLabel());
        assertEquals(182, mapped.getServingGrams());
        assertEquals(95, mapped.getNutrition().getCalories());
        assertEquals(0.5, mapped.getNutrition().getProtein());
        assertEquals(25, mapped.getNutrition().getCarbs());
        assertEquals(0.3, mapped.getNutrition().getFat());
        assertEquals(FoodUnitOption.DEFAULT_SERVING, mapped.getUnit());
        assertEquals(1, mapped.getQuantity());
        assertEquals("", state.getSubmitError());
    }

    @Test
    void prepareSuccessViewWithEmptyResultsClearsSearchResults() {
        final FoodEditorViewModel foodEditorViewModel = new FoodEditorViewModel();
        final SearchFoodPresenter presenter = new SearchFoodPresenter(foodEditorViewModel);

        presenter.prepareSuccessView(new SearchFoodOutputData(List.of()));

        assertTrue(foodEditorViewModel.getState().getSearchResults().isEmpty());
    }

    @Test
    void prepareFailViewSetsSubmitErrorAndClearsSearchResults() {
        final FoodEditorViewModel foodEditorViewModel = new FoodEditorViewModel();
        final SearchFoodPresenter presenter = new SearchFoodPresenter(foodEditorViewModel);
        foodEditorViewModel.getState().setSearchResults(List.of(new FoodSearchResultDisplayData(
                "Apple", "1 medium", 182, new FoodMacroAmounts(95, 0.5, 25, 0.3),
                FoodUnitOption.DEFAULT_SERVING, 1)));

        presenter.prepareFailView("search failed");

        final FoodEditorState state = foodEditorViewModel.getState();
        assertEquals("search failed", state.getSubmitError());
        assertTrue(state.getSearchResults().isEmpty());
    }
}
