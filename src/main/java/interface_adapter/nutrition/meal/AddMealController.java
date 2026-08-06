package interface_adapter.nutrition.meal;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import entity.FoodEntry;
import interface_adapter.login.LoginViewModel;
import use_case.nutrition.meal.add_meal.AddMealInputBoundary;
import use_case.nutrition.meal.add_meal.AddMealInputData;

public class AddMealController {

    private final AddMealInputBoundary addMealInteractor;
    private final LoginViewModel loginViewModel;

    public AddMealController(
            final AddMealInputBoundary addMealInteractor,
            final LoginViewModel loginViewModel
    ) {
        this.addMealInteractor = addMealInteractor;
        this.loginViewModel = loginViewModel;
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

        System.out.println(
                "Saving meal \"" + name
                        + "\" with "
                        + foodsToSave.size()
                        + " foods."
        );

        final AddMealInputData inputData = new AddMealInputData(
                name,
                this.loginViewModel.getState().getUsername(),
                LocalDate.now(),
                foodsToSave
        );

        this.addMealInteractor.execute(inputData);
    }
}