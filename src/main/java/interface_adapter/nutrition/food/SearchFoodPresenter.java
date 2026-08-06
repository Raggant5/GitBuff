package interface_adapter.nutrition.food;

import java.util.ArrayList;

import use_case.nutrition.food.search_food.SearchFoodOutputBoundary;
import use_case.nutrition.food.search_food.SearchFoodOutputData;

public class SearchFoodPresenter implements SearchFoodOutputBoundary {

    private final FoodEditorViewModel foodEditorViewModel;

    public SearchFoodPresenter(FoodEditorViewModel foodEditorViewModel) {
        this.foodEditorViewModel = foodEditorViewModel;
    }

    @Override
    public void prepareSuccessView(SearchFoodOutputData outputData) {

        final FoodEditorState state = foodEditorViewModel.getState();
        state.setSearchResults(outputData.getFoodResults());
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
