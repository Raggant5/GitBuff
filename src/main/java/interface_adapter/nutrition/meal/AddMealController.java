package interface_adapter.nutrition.meal;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import entity.FoodEntry;
import interface_adapter.login.LoginViewModel;
import use_case.dashboard.DashboardInputBoundary;
import use_case.nutrition.meal.add_meal.AddMealInputBoundary;
import use_case.nutrition.meal.add_meal.AddMealInputData;

/**
 * Controller for adding a meal.
 */
public class AddMealController {

    private final AddMealInputBoundary addMealInteractor;
    private final LoginViewModel loginViewModel;
    private final DashboardInputBoundary dashboardInteractor;

    /**
     * Constructs an AddMealController.
     *
     * @param addMealInteractor interactor for adding meals
     * @param loginViewModel view model containing the logged-in user
     * @param dashboardInteractor interactor for refreshing dashboard data
     */
    public AddMealController(
            final AddMealInputBoundary addMealInteractor,
            final LoginViewModel loginViewModel,
            final DashboardInputBoundary dashboardInteractor
    ) {
        this.addMealInteractor = addMealInteractor;
        this.loginViewModel = loginViewModel;
        this.dashboardInteractor = dashboardInteractor;
    }

    /**
     * Executes the Add Meal Use Case.
     *
     * @param name the name of the meal to be added
     * @param foodEntriesForMeal every food associated with the meal
     * @param foodEntriesToRemove foods marked for removal
     */
    public void execute(
            final String name,
            final List<FoodEntry> foodEntriesForMeal,
            final List<FoodEntry> foodEntriesToRemove
    ) {
        final List<FoodEntry> foodsToSave =
                new ArrayList<>(foodEntriesForMeal);

        foodsToSave.removeAll(foodEntriesToRemove);

        final String username =
                this.loginViewModel.getState().getUsername();

        final AddMealInputData inputData =
                new AddMealInputData(
                        name,
                        username,
                        LocalDate.now(),
                        foodsToSave
                );

        this.addMealInteractor.execute(inputData);

        if (this.dashboardInteractor != null) {
            this.dashboardInteractor.execute(username);
        }
    }
}