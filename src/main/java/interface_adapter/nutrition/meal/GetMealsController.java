package interface_adapter.nutrition.meal;

import interface_adapter.login.LoginViewModel;
import use_case.nutrition.meal.get_meals.GetMealsInputBoundary;
import use_case.nutrition.meal.get_meals.GetMealsInputData;

/**
 * Controller for the Get Meals Use Case. Called when the user navigates to the Nutrition tab,
 * so meals load on demand instead of being fetched eagerly for every user at login.
 */
public class GetMealsController {

    private final GetMealsInputBoundary getMealsInteractor;
    private final LoginViewModel loginViewModel;

    public GetMealsController(final GetMealsInputBoundary getMealsInteractor, final LoginViewModel loginViewModel) {
        this.getMealsInteractor = getMealsInteractor;
        this.loginViewModel = loginViewModel;
    }

    /**
     * Executes the Get Meals Use Case for the currently logged-in user.
     */
    public void execute() {
        final String userId = this.loginViewModel.getState().getUsername();
        if (userId != null && !userId.isBlank()) {
            this.getMealsInteractor.execute(new GetMealsInputData(userId));
        }
    }
}
