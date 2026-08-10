package interface_adapter.nutrition.meal;

import use_case.nutrition.meal.delete_meal.DeleteMealOutputBoundary;
import use_case.nutrition.meal.delete_meal.DeleteMealOutputData;

public class DeleteMealPresenter implements DeleteMealOutputBoundary {

    private final ViewMealsViewModel viewMealsViewModel;

    public DeleteMealPresenter(ViewMealsViewModel viewMealsViewModel) {
        this.viewMealsViewModel = viewMealsViewModel;
    }

    @Override
    public void prepareSuccessView(DeleteMealOutputData deleteMealOutputData) {
        viewMealsViewModel.getState().setError("");
        viewMealsViewModel.getState().removeMeal(deleteMealOutputData.getMealId());
        viewMealsViewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        viewMealsViewModel.getState().setError(errorMessage);
        viewMealsViewModel.firePropertyChanged();
    }
}
