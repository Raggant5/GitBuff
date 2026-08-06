package interface_adapter.nutrition.meal;

import java.util.ArrayList;
import java.util.List;

import entity.Meal;
import interface_adapter.MainViewManagerModel;
import use_case.dashboard.DashboardInputBoundary;
import use_case.nutrition.meal.add_meal.AddMealOutputBoundary;
import use_case.nutrition.meal.add_meal.AddMealOutputData;

/**
 * Presenter for the Add Meal Use Case.
 */
public class AddMealPresenter implements AddMealOutputBoundary {

    private final MealEditorViewModel mealEditorViewModel;
    private final ViewMealsViewModel viewMealsViewModel;
    private final MainViewManagerModel mainViewManagerModel;
    private final DashboardInputBoundary dashboardInteractor;

    /**
     * Constructs an AddMealPresenter.
     *
     * @param mealEditorViewModel meal editor view model
     * @param viewMealsViewModel saved meals view model
     * @param mainViewManagerModel main view manager model
     * @param dashboardInteractor dashboard refresh interactor
     */
    public AddMealPresenter(
            final MealEditorViewModel mealEditorViewModel,
            final ViewMealsViewModel viewMealsViewModel,
            final MainViewManagerModel mainViewManagerModel,
            final DashboardInputBoundary dashboardInteractor
    ) {
        this.mealEditorViewModel = mealEditorViewModel;
        this.viewMealsViewModel = viewMealsViewModel;
        this.mainViewManagerModel = mainViewManagerModel;
        this.dashboardInteractor = dashboardInteractor;
    }

    @Override
    public void prepareSuccessView(
            final AddMealOutputData outputData
    ) {
        final Meal savedMeal = outputData.getMeal();

        final MealEditorState currentState =
                this.mealEditorViewModel.getState();

        currentState.reset();
        this.mealEditorViewModel.firePropertyChanged();

        final ViewMealsState mealsState =
                this.viewMealsViewModel.getState();

        final List<Meal> meals =
                new ArrayList<>(mealsState.getMeals());

        meals.add(0, savedMeal);

        mealsState.setMeals(meals);
        this.viewMealsViewModel.setState(mealsState);
        this.viewMealsViewModel.firePropertyChanged();

        if (this.dashboardInteractor != null) {
            this.dashboardInteractor.execute(
                    savedMeal.getUserId()
            );
        }

        this.mainViewManagerModel.setState("nutrition");
        this.mainViewManagerModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(
            final String errorMessage
    ) {
        this.mealEditorViewModel
                .getState()
                .setErrorMessage(errorMessage);

        this.mealEditorViewModel.firePropertyChanged();
    }
}