package interface_adapter.nutrition.food;

import interface_adapter.nutrition.meal.MealEditorState;
import interface_adapter.nutrition.meal.MealEditorViewModel;
import use_case.nutrition.food.FoodValidationErrors;
import use_case.nutrition.food.create_food.AddFoodEntryOutputBoundary;
import use_case.nutrition.food.create_food.AddFoodEntryOutputData;

public class AddFoodPresenter implements AddFoodEntryOutputBoundary {
    private final MealEditorViewModel mealEditorViewModel;
    private final FoodEditorViewModel foodEditorViewModel;

    public AddFoodPresenter(MealEditorViewModel mealEditorViewModel, FoodEditorViewModel foodEditorViewModel) {
        this.mealEditorViewModel = mealEditorViewModel;
        this.foodEditorViewModel = foodEditorViewModel;
    }

    @Override
    public void prepareSuccessView(AddFoodEntryOutputData outputData) {
        final MealEditorState currentState = mealEditorViewModel.getState();

        final Integer id;
        if (outputData.getId() == null) {
            id = currentState.getNextTempId();
            currentState.setNextTempId(id - 1);
        }
        else {
            id = outputData.getId();
        }

        final FoodNutritionDisplayData nutrition = new FoodNutritionDisplayData(outputData.getNutrition().getCalories(),
                outputData.getNutrition().getProtein(), outputData.getNutrition().getCarbs(),
                outputData.getNutrition().getFat());
        final FoodEntryDisplayData food = new FoodEntryDisplayData(id, outputData.getFoodName(),
                nutrition, outputData.getQuantity(), FoodEnumMapper.toOption(outputData.getUnit()),
                outputData.getGrams());

        currentState.addFoodEntry(food);
        currentState.setShowFoodEditor(false);
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
