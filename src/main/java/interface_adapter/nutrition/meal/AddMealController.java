package interface_adapter.nutrition.meal;

import java.time.LocalDate;
import java.util.List;

import entity.FoodEntry;
import interface_adapter.login.LoginViewModel;
import use_case.nutrition.meal.add_meal.AddMealInputBoundary;
import use_case.nutrition.meal.add_meal.AddMealInputData;

public class AddMealController {

    private final AddMealInputBoundary addMealInteractor;
    private final LoginViewModel loginViewModel;

    public AddMealController(AddMealInputBoundary addMealInteractor, LoginViewModel loginViewModel) {
        this.addMealInteractor = addMealInteractor;
        this.loginViewModel = loginViewModel;
    }

    /**
     * Executes the Add Meal Use Case.
     * @param name the name of the meal to be added
     * @param foodEntriesForMeal every food associated with the meal
     * @param foodEntriesToRemove the food to be deleted from the list of foods for the meal
     */
    public void execute(String name, List<FoodEntry> foodEntriesForMeal, List<FoodEntry> foodEntriesToRemove) {
        foodEntriesForMeal.removeAll(foodEntriesToRemove);
        addMealInteractor.execute(new AddMealInputData(name, loginViewModel.getState().getUsername(),
                LocalDate.now(), foodEntriesForMeal));
    }

}
