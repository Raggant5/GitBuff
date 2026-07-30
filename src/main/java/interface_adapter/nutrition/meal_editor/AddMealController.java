package interface_adapter.nutrition.meal_editor;

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

    public void execute(String name, List<FoodEntry> foodEntriesForMeal) {
        addMealInteractor.execute(new AddMealInputData(name, loginViewModel.getState().getUsername(),
                LocalDate.now(), foodEntriesForMeal));
    }

}
