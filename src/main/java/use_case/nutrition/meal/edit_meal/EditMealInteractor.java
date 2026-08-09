package use_case.nutrition.meal.edit_meal;

import java.util.ArrayList;
import java.util.List;

import entity.FoodEntry;
import entity.FoodEntryFactory;
import entity.FoodNutrition;
import entity.Meal;
import use_case.DataAccessException;
import use_case.nutrition.food.FoodEntryData;
import use_case.nutrition.food.FoodEntryInputData;
import use_case.nutrition.food.FoodNutritionData;
import use_case.nutrition.meal.get_meals.ViewMealDataAccessInterface;

public class EditMealInteractor implements EditMealInputBoundary {

    private final EditMealOutputBoundary presenter;
    private final EditMealDataAccessInterface dataAccess;
    private final ViewMealDataAccessInterface viewDataAccess;
    private final FoodEntryFactory foodEntryFactory;

    public EditMealInteractor(EditMealOutputBoundary presenter, EditMealDataAccessInterface dataAccess,
                              ViewMealDataAccessInterface viewDataAccess,
                              FoodEntryFactory foodEntryFactory) {
        this.presenter = presenter;
        this.dataAccess = dataAccess;
        this.viewDataAccess = viewDataAccess;
        this.foodEntryFactory = foodEntryFactory;
    }

    @Override
    public void execute(final EditMealInputData inputData) {
        if (inputData.getName() == null || inputData.getName().isBlank()) {
            presenter.prepareFailView("Meal name is required.");
        }
        else {
            try {
                final Meal meal = viewDataAccess.getMealById(inputData.getMealId());
                meal.setName(inputData.getName());

                final List<FoodEntry> foodEntries = new ArrayList<>();
                for (FoodEntryInputData foodData : inputData.getFoodEntries()) {
                    final FoodNutrition nutrition = new FoodNutrition(parseDouble(foodData.getCalories()),
                            parseDouble(foodData.getProtein()), parseDouble(foodData.getCarbs()),
                            parseDouble(foodData.getFat()));
                    final FoodEntry food = foodEntryFactory.create(foodData.getFoodName(), nutrition,
                            foodData.getQuantity(), foodData.getUnit(), foodData.getGrams());
                    final Integer id = foodData.getId();
                    if (id != null && id > 0) {
                        food.setId(id);
                    }
                    food.setMealId(meal.getId());
                    foodEntries.add(food);
                }
                meal.setFoodEntries(foodEntries);

                final Meal savedMeal = dataAccess.editMeal(meal, inputData.getFoodEntryIdsToDelete());

                final List<FoodEntryData> savedFoodEntries = new ArrayList<>();
                for (FoodEntry food : savedMeal.getFoodEntries()) {
                    final FoodNutritionData savedNutrition = new FoodNutritionData(food.getNutrition().getCalories(),
                            food.getNutrition().getProtein(), food.getNutrition().getCarbs(),
                            food.getNutrition().getFat());
                    savedFoodEntries.add(new FoodEntryData(food.getId(), food.getFoodName(), savedNutrition,
                            food.getQuantity(), food.getUnit(), food.getGrams()));
                }

                presenter.prepareSuccessView(new EditMealOutputData(savedMeal.getId(), savedMeal.getDate(),
                        savedMeal.getName(), savedFoodEntries));
            }
            catch (DataAccessException exc) {
                presenter.prepareFailView("Unable to save meal. Please try again.");
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
