package use_case.nutrition.meal.add_meal;

import java.util.List;

import entity.FoodEntry;
import entity.Meal;
import entity.MealFactory;

public class AddMealInteractor implements AddMealInputBoundary {

    private final AddMealOutputBoundary addMealPresenter;
    private final AddMealDataAccessInterface mealDataAccessObject;
    private final MealFactory mealFactory;

    public AddMealInteractor(
            final AddMealOutputBoundary addMealPresenter,
            final AddMealDataAccessInterface mealDataAccessObject,
            final MealFactory mealFactory
    ) {
        this.addMealPresenter = addMealPresenter;
        this.mealDataAccessObject = mealDataAccessObject;
        this.mealFactory = mealFactory;
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

                final List<FoodEntry> foodEntries = addMealInputData.getFoodEntries();

                meal.getFoodEntries().addAll(foodEntries);
                for (FoodEntry food : foodEntries) {
                    food.setMealId(mealId);
                    food.setId(this.mealDataAccessObject.saveFoodEntry(food));
                }

                this.addMealPresenter.prepareSuccessView(new AddMealOutputData(meal));
            }
            catch (RuntimeException exc) {
                this.addMealPresenter.prepareFailView("Unable to save meal. Please try again.");
            }
        }
    }
}
