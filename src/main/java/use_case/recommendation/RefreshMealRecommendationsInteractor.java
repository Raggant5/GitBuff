package use_case.recommendation;

import java.util.ArrayList;
import java.util.List;

import entity.MealRecommendation;
import entity.User;

/**
 * Interactor that refreshes a user's meal recommendations only, leaving their workout plans
 * and any calendar-synced workout events untouched.
 */
public class RefreshMealRecommendationsInteractor implements RefreshMealRecommendationsInputBoundary {

    private final RecommendationUserDataAccessInterface userDataAccessObject;
    private final RefreshMealRecommendationsOutputBoundary presenter;
    private final FoodRecommendationDataAccessInterface foodRecommendationDataAccessObject;

    /**
     * Constructs a RefreshMealRecommendationsInteractor instance.
     *
     * @param userDataAccessObject data access object for user profile lookup
     * @param presenter output boundary
     * @param foodRecommendationDataAccessObject meal suggestion data access interface
     */
    public RefreshMealRecommendationsInteractor(final RecommendationUserDataAccessInterface userDataAccessObject,
                                                final RefreshMealRecommendationsOutputBoundary presenter,
                                                final FoodRecommendationDataAccessInterface
                                                        foodRecommendationDataAccessObject) {
        this.userDataAccessObject = userDataAccessObject;
        this.presenter = presenter;
        this.foodRecommendationDataAccessObject = foodRecommendationDataAccessObject;
    }

    @Override
    public void execute() {
        final String username = this.userDataAccessObject.getCurrentUsername();
        if (username == null) {
            this.presenter.prepareFailView("No user is currently logged in.");
        }
        else {
            final User user = this.userDataAccessObject.get(username);
            if (user == null) {
                this.presenter.prepareFailView("No user is currently logged in.");
            }
            else {
                presentRefreshedMealsFor(user);
            }
        }
    }

    private void presentRefreshedMealsFor(final User user) {
        final int dailyCalorieTarget = DailyMacroCalculator.calculateDailyCalorieTarget(user);
        final int dailyProteinGrams = DailyMacroCalculator.calculateDailyProteinGrams(user);

        List<MealRecommendation> meals = new ArrayList<>();
        if (this.foodRecommendationDataAccessObject != null) {
            meals = this.foodRecommendationDataAccessObject.generateMealRecommendations(user, dailyCalorieTarget);
        }

        final RefreshMealRecommendationsOutputData outputData = new RefreshMealRecommendationsOutputData(
                user.getBmi(), dailyCalorieTarget, dailyProteinGrams, meals);

        this.presenter.prepareSuccessView(outputData);
    }
}
