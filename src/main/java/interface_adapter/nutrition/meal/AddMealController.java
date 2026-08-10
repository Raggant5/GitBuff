package interface_adapter.nutrition.meal;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import interface_adapter.login.LoginViewModel;
import interface_adapter.nutrition.food.FoodEnumMapper;
import interface_adapter.nutrition.food.FoodEntryDisplayData;
import use_case.nutrition.food.FoodEntryInputData;
import use_case.nutrition.food.FoodNutritionInput;
import use_case.nutrition.meal.add_meal.AddMealInputBoundary;
import use_case.nutrition.meal.add_meal.AddMealInputData;

/**
 * Controller for adding a meal.
 */
public class AddMealController {

    private final AddMealInputBoundary addMealInteractor;
    private final LoginViewModel loginViewModel;

    /**
     * Constructs an AddMealController.
     *
     * @param addMealInteractor interactor for adding meals
     * @param loginViewModel view model containing the logged-in user
     */
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
     */
    public void execute(
            final String name,
            final List<FoodEntryDisplayData> foodEntriesForMeal
    ) {
        final List<FoodEntryInputData> foods = new ArrayList<>();
        for (FoodEntryDisplayData food : foodEntriesForMeal) {
            final FoodNutritionInput nutrition = new FoodNutritionInput(food.getNutrition().getCalories(),
                    food.getNutrition().getProtein(), food.getNutrition().getCarbs(), food.getNutrition().getFat());
            foods.add(new FoodEntryInputData(food.getId(), food.getFoodName(), nutrition, food.getQuantity(),
                    FoodEnumMapper.toEntity(food.getUnit()), food.getGrams()));
        }

        final String username =
                this.loginViewModel.getState().getUsername();

        final AddMealInputData inputData =
                new AddMealInputData(
                        name,
                        username,
                        LocalDate.now(),
                        foods
                );

        this.addMealInteractor.execute(inputData);
    }
}
