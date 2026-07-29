package use_case.nutrition.meal;

import java.util.List;

import entity.FoodEntry;
import entity.Meal;
import entity.MealFactory;

public class AddMealInteractor implements AddMealInputBoundary {

    private final AddMealOutputBoundary addMealPresenter;
    private final AddMealDataAccessInterface mealDataAccessObject;
    private final MealFactory mealFactory;

    public AddMealInteractor(AddMealOutputBoundary addMealPresenter, AddMealDataAccessInterface mealDataAccessObject,
                             MealFactory mealFactory) {
        this.addMealPresenter = addMealPresenter;
        this.mealDataAccessObject = mealDataAccessObject;
        this.mealFactory = mealFactory;
    }

    @Override
    public void execute(AddMealInputData addMealInputData) {
        final Meal meal = mealFactory.create(addMealInputData.getUserId(), addMealInputData.getDate(),
                addMealInputData.getName());
        final int mealId = mealDataAccessObject.saveMeal(meal);
        final List<FoodEntry> foodEntries = addMealInputData.getFoodEntries();
        meal.getFoodEntries().addAll(foodEntries);
        for (FoodEntry food : foodEntries) {
            food.setMealId(mealId);
            food.setId(mealDataAccessObject.saveFoodEntry(food));
        }
        addMealPresenter.prepareSuccessView(new AddMealOutputData(meal));
    }
}
