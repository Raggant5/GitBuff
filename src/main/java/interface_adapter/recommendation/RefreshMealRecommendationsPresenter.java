package interface_adapter.recommendation;

import interface_adapter.nutrition.NutritionState;
import interface_adapter.nutrition.NutritionViewModel;
import use_case.recommendation.RefreshMealRecommendationsOutputBoundary;
import use_case.recommendation.RefreshMealRecommendationsOutputData;

/**
 * Presenter for the Refresh Meal Recommendations Use Case. Updates only the Nutrition view
 * model - unlike the full Recommendation Use Case, refreshing meals never touches workout
 * plans or calendar-synced workout events.
 */
public class RefreshMealRecommendationsPresenter implements RefreshMealRecommendationsOutputBoundary {

    private final NutritionViewModel nutritionViewModel;

    /**
     * Constructs a RefreshMealRecommendationsPresenter instance.
     *
     * @param nutritionViewModel the nutrition view model
     */
    public RefreshMealRecommendationsPresenter(final NutritionViewModel nutritionViewModel) {
        this.nutritionViewModel = nutritionViewModel;
    }

    @Override
    public void prepareSuccessView(final RefreshMealRecommendationsOutputData outputData) {
        final NutritionState nutritionState = this.nutritionViewModel.getState();
        nutritionState.setBmi(outputData.getBmi());
        nutritionState.setDailyCalorieTarget(outputData.getDailyCalorieTarget());
        nutritionState.setDailyProteinGrams(outputData.getDailyProteinGrams());
        nutritionState.setMealRecommendations(outputData.getMealRecommendations());
        nutritionState.setMessage("");
        this.nutritionViewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(final String errorMessage) {
        final NutritionState nutritionState = this.nutritionViewModel.getState();
        nutritionState.setMessage(errorMessage);
        this.nutritionViewModel.firePropertyChanged();
    }
}
