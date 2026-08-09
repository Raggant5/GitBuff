package interface_adapter.nutrition.food;

import java.util.ArrayList;
import java.util.List;

import use_case.nutrition.food.search_food.FoodSearchResultData;
import use_case.nutrition.food.search_food.SearchFoodOutputBoundary;
import use_case.nutrition.food.search_food.SearchFoodOutputData;

public class SearchFoodPresenter implements SearchFoodOutputBoundary {

    private final FoodEditorViewModel foodEditorViewModel;

    public SearchFoodPresenter(FoodEditorViewModel foodEditorViewModel) {
        this.foodEditorViewModel = foodEditorViewModel;
    }

    @Override
    public void prepareSuccessView(SearchFoodOutputData outputData) {

        final List<FoodSearchResultDisplayData> results = new ArrayList<>();
        for (FoodSearchResultData food : outputData.getFoodResults()) {
            final FoodMacroAmounts nutrition = new FoodMacroAmounts(food.getNutrition().getCalories(),
                    food.getNutrition().getProtein(), food.getNutrition().getCarbs(), food.getNutrition().getFat());
            results.add(new FoodSearchResultDisplayData(food.getFoodName(), food.getServingLabel(),
                    food.getServingGrams(), nutrition, food.getUnit(), food.getQuantity()));
        }

        final FoodEditorState state = foodEditorViewModel.getState();
        state.setSearchResults(results);
        state.setSubmitError("");
        foodEditorViewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String error) {

        final FoodEditorState state = foodEditorViewModel.getState();
        state.setSubmitError(error);
        state.setSearchResults(new ArrayList<>());
        foodEditorViewModel.firePropertyChanged();
    }
}
