package use_case.nutrition.meal.prepare_edit_meal;

import java.util.ArrayList;
import java.util.List;

import entity.FoodEntry;
import entity.Meal;
import use_case.DataAccessException;
import use_case.nutrition.food.FoodEntryData;
import use_case.nutrition.food.FoodNutritionData;
import use_case.nutrition.meal.get_meals.ViewMealDataAccessInterface;

public class PrepareEditMealInteractor implements PrepareEditMealInputBoundary {

    private final PrepareEditMealOutputBoundary presenter;
    private final ViewMealDataAccessInterface mealDataAccessObject;

    public PrepareEditMealInteractor(PrepareEditMealOutputBoundary presenter,
                                     ViewMealDataAccessInterface mealDataAccessObject) {
        this.presenter = presenter;
        this.mealDataAccessObject = mealDataAccessObject;
    }

    @Override
    public void execute(PrepareEditMealInputData inputData) {
        try {
            final Meal meal = mealDataAccessObject.getMealById(inputData.getMealId());

            final List<FoodEntryData> foodEntries = new ArrayList<>();
            for (FoodEntry food : meal.getFoodEntries()) {
                final FoodNutritionData nutrition = new FoodNutritionData(food.getNutrition().getCalories(),
                        food.getNutrition().getProtein(), food.getNutrition().getCarbs(),
                        food.getNutrition().getFat());
                foodEntries.add(new FoodEntryData(food.getId(), food.getFoodName(), nutrition,
                        food.getQuantity(), food.getUnit(), food.getGrams()));
            }

            presenter.prepareSuccessView(new PrepareEditMealOutputData(meal.getId(), meal.getDate(), meal.getName(),
                    foodEntries));
        }
        catch (DataAccessException exc) {
            presenter.prepareFailView("Unable to load meal. Please try again.");
        }
    }
}
