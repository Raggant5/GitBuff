package interface_adapter.nutrition.food;

import interface_adapter.nutrition.meal.MealEditorState;
import interface_adapter.nutrition.meal.MealEditorViewModel;
import use_case.nutrition.food.FoodValidationErrors;
import use_case.nutrition.food.edit_food.EditFoodOutputBoundary;
import use_case.nutrition.food.edit_food.EditFoodOutputData;

public class EditFoodPresenter implements EditFoodOutputBoundary {

    private final MealEditorViewModel mealEditorViewModel;
    private final FoodEditorViewModel foodEditorViewModel;

    public EditFoodPresenter(MealEditorViewModel mealEditorViewModel, FoodEditorViewModel foodEditorViewModel) {
        this.mealEditorViewModel = mealEditorViewModel;
        this.foodEditorViewModel = foodEditorViewModel;
    }

    @Override
    public void prepareSuccessView(EditFoodOutputData outputData) {
        final FoodNutritionDisplayData nutrition = new FoodNutritionDisplayData(outputData.getNutrition().getCalories(),
                outputData.getNutrition().getProtein(), outputData.getNutrition().getCarbs(),
                outputData.getNutrition().getFat());
        final FoodEntryDisplayData updated = new FoodEntryDisplayData(outputData.getId(), outputData.getFoodName(),
                nutrition, outputData.getQuantity(), FoodEnumMapper.toOption(outputData.getUnit()),
                outputData.getGrams());

        final MealEditorState state = mealEditorViewModel.getState();
        state.replaceFoodEntry(updated);
        state.setShowFoodEditor(false);
        mealEditorViewModel.firePropertyChanged();
        foodEditorViewModel.getState().reset();
        foodEditorViewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(FoodValidationErrors errors) {
        final FoodEditorState currentState = foodEditorViewModel.getState();
        currentState.setCaloriesError(errors.getCaloriesError());
        currentState.setProteinError(errors.getProteinError());
        currentState.setCarbsError(errors.getCarbsError());
        currentState.setFatError(errors.getFatError());
        currentState.setQuantityError(errors.getQuantityError());
        currentState.setGramsError(errors.getGramsError());
        currentState.setSubmitError(errors.getGeneralError());
        foodEditorViewModel.firePropertyChanged();
    }
}
