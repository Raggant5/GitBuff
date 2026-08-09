package use_case.nutrition.meal.add_meal;

import java.util.ArrayList;
import java.util.List;

import entity.FoodEntry;
import entity.FoodEntryFactory;
import entity.FoodNutrition;
import entity.Meal;
import entity.MealFactory;
import use_case.DataAccessException;
import use_case.nutrition.food.FoodEntryData;
import use_case.nutrition.food.FoodEntryInputData;
import use_case.nutrition.food.FoodNutritionData;

public class AddMealInteractor implements AddMealInputBoundary {

    private final AddMealOutputBoundary addMealPresenter;
    private final AddMealDataAccessInterface mealDataAccessObject;
    private final MealFactory mealFactory;
    private final FoodEntryFactory foodEntryFactory;

    public AddMealInteractor(
            final AddMealOutputBoundary addMealPresenter,
            final AddMealDataAccessInterface mealDataAccessObject,
            final MealFactory mealFactory,
            final FoodEntryFactory foodEntryFactory
    ) {
        this.addMealPresenter = addMealPresenter;
        this.mealDataAccessObject = mealDataAccessObject;
        this.mealFactory = mealFactory;
        this.foodEntryFactory = foodEntryFactory;
    }

    @Override
    public void execute(final AddMealInputData addMealInputData) {
        if (addMealInputData.getName() == null || addMealInputData.getName().isBlank()) {
            this.addMealPresenter.prepareFailView("Meal name is required.");
        }
        else {
            try {
                final Meal meal = this.mealFactory.create(addMealInputData.getUserId(), addMealInputData.getDate(),
                        addMealInputData.getName());

                final int mealId = mealDataAccessObject.saveMeal(meal);

                final List<FoodEntryData> savedFoodEntries = new ArrayList<>();
                for (FoodEntryInputData foodData : addMealInputData.getFoodEntries()) {
                    final FoodNutrition nutrition = new FoodNutrition(parseDouble(foodData.getCalories()),
                            parseDouble(foodData.getProtein()), parseDouble(foodData.getCarbs()),
                            parseDouble(foodData.getFat()));
                    final FoodEntry food = foodEntryFactory.create(foodData.getFoodName(), nutrition,
                            foodData.getQuantity(), foodData.getUnit(), foodData.getGrams());
                    food.setMealId(mealId);
                    food.setId(this.mealDataAccessObject.saveFoodEntry(food));
                    final FoodNutritionData savedNutrition = new FoodNutritionData(food.getNutrition().getCalories(),
                            food.getNutrition().getProtein(), food.getNutrition().getCarbs(),
                            food.getNutrition().getFat());
                    savedFoodEntries.add(new FoodEntryData(food.getId(), food.getFoodName(), savedNutrition,
                            food.getQuantity(), food.getUnit(), food.getGrams()));
                }

                this.addMealPresenter.prepareSuccessView(new AddMealOutputData(mealId, meal.getUserId(),
                        meal.getDate(), meal.getName(), savedFoodEntries));
            }
            catch (DataAccessException exc) {
                this.addMealPresenter.prepareFailView("Unable to save meal. Please try again.");
            }
        }
    }

    private double parseDouble(String value) {
        double result = 0;
        try {
            result = Double.parseDouble(value);
        }
        catch (NumberFormatException exc) {
            result = 0;
        }
        return result;
    }
}
