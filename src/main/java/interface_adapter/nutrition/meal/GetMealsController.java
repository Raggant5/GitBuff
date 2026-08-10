package interface_adapter.nutrition.meal;

import use_case.nutrition.meal.get_meals.GetMealsInputBoundary;
import use_case.nutrition.meal.get_meals.GetMealsInputData;

/**
 * Controller for the Get Meals Use Case. Called when the user navigates to the Nutrition tab,
 * so meals load on demand instead of being fetched eagerly for every user at login.
 */
public class GetMealsController {

    private final GetMealsInputBoundary getMealsInteractor;

    public GetMealsController(final GetMealsInputBoundary getMealsInteractor) {
        this.getMealsInteractor = getMealsInteractor;
    }

    /**
     * Executes the Get Meals Use Case for the currently logged-in user.
     *
     * @param userId the currently logged-in user's id
     */
    public void execute(final String userId) {
        if (userId != null && !userId.isBlank()) {
            this.getMealsInteractor.execute(new GetMealsInputData(userId));
        }
    }
}
