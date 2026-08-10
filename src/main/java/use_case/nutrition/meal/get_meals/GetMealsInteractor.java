package use_case.nutrition.meal.get_meals;

import entity.Meal;
import use_case.DataAccessException;
import use_case.nutrition.meal.MealData;

import java.util.ArrayList;
import java.util.List;

/**
 * Interactor for fetching a user's saved meals on demand.
 */
public class GetMealsInteractor implements GetMealsInputBoundary {

    private final GetMealsOutputBoundary presenter;
    private final ViewMealDataAccessInterface mealDataAccessObject;

    public GetMealsInteractor(final GetMealsOutputBoundary presenter,
                              final ViewMealDataAccessInterface mealDataAccessObject) {
        this.presenter = presenter;
        this.mealDataAccessObject = mealDataAccessObject;
    }

    @Override
    public void execute(final GetMealsInputData inputData) {
        try {
            final List<Meal> meals = this.mealDataAccessObject.getMealsForUser(inputData.getUserId());
            final List<MealData> mealsData = new ArrayList<>();
            for (final Meal meal : meals) {
                mealsData.add(MealData.from(meal));
            }
            this.presenter.prepareSuccessView(new GetMealsOutputData(mealsData));
        }
        catch (final DataAccessException exception) {
            this.presenter.prepareFailView("Unable to load meals. Please try again.");
        }
    }
}
