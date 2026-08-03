package interface_adapter.nutrition.food;

import use_case.nutrition.food.search_food.SearchFoodOutputBoundary;
import use_case.nutrition.food.search_food.SearchFoodOutputData;

import java.util.ArrayList;

public class SearchFoodPresenter implements SearchFoodOutputBoundary {

    private final FoodEditorViewModel foodEditorViewModel;

    public SearchFoodPresenter(FoodEditorViewModel foodEditorViewModel) {
        this.foodEditorViewModel = foodEditorViewModel;
    }

    @Override
    public void prepareSuccessView(SearchFoodOutputData outputData) {

        final FoodEditorState state = foodEditorViewModel.getState();
        state.setSearchResults(outputData.getFoodResults());
        state.setError("");
        foodEditorViewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String error) {

        final FoodEditorState state = foodEditorViewModel.getState();
        state.setError(error);
        state.setSearchResults(new ArrayList<>());
        foodEditorViewModel.firePropertyChanged();
    }
}
