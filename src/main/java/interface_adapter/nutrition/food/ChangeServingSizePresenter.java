package interface_adapter.nutrition.food;

import use_case.nutrition.food.change_serving_size.ChangeServingSizeOutputBoundary;
import use_case.nutrition.food.change_serving_size.ChangeServingSizeOutputData;

public class ChangeServingSizePresenter implements ChangeServingSizeOutputBoundary {

    private final FoodEditorViewModel foodEditorViewModel;

    public ChangeServingSizePresenter(FoodEditorViewModel foodEditorViewModel) {
        this.foodEditorViewModel = foodEditorViewModel;
    }

    @Override
    public void prepareSuccessView(ChangeServingSizeOutputData outputData) {
        final FoodServingDetails servingDetails = foodEditorViewModel.getState().getServingDetails();
        servingDetails.setUnit(outputData.getUnit());
        servingDetails.setServingGrams(outputData.getServingGrams());
        servingDetails.setServingCalories(outputData.getServingCalories());
        servingDetails.setServingProtein(outputData.getServingProtein());
        servingDetails.setServingCarbs(outputData.getServingCarbs());
        servingDetails.setServingFat(outputData.getServingFat());
        servingDetails.recalculateTotals();
        foodEditorViewModel.firePropertyChanged();
    }
}
