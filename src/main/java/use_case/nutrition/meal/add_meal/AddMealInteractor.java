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
        final Meal meal = this.mealFactory.create(
                addMealInputData.getUserId(),
                addMealInputData.getDate(),
                addMealInputData.getName()
        );

        final int mealId =
                this.mealDataAccessObject.saveMeal(meal);

        final List<FoodEntry> foodEntries =
                addMealInputData.getFoodEntries();

        System.out.println(
                "Interactor received "
                        + foodEntries.size()
                        + " foods for meal ID "
                        + mealId
        );

        for (FoodEntry food : foodEntries) {
            food.setMealId(mealId);

            System.out.println(
                    "Saving food \""
                            + food.getFoodName()
                            + "\" for meal ID "
                            + mealId
            );

            final int foodId =
                    this.mealDataAccessObject.saveFoodEntry(food);

            food.setId(foodId);
        }

        meal.getFoodEntries().addAll(foodEntries);

        this.addMealPresenter.prepareSuccessView(
                new AddMealOutputData(meal)
        );
    }
}