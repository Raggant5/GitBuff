package interface_adapter.recommendation;

import interface_adapter.nutrition.NutritionState;
import interface_adapter.nutrition.NutritionViewModel;
import interface_adapter.workouts.WorkoutsState;
import interface_adapter.workouts.WorkoutsViewModel;
import use_case.recommendation.RecommendationOutputBoundary;
import use_case.recommendation.RecommendationOutputData;

/**
 * The Presenter for the Recommendation Use Case. Updates both the Nutrition
 * and Workouts view models, since a single profile-based recommendation
 * feeds both screens.
 */
public class RecommendationPresenter implements RecommendationOutputBoundary {

    private final NutritionViewModel nutritionViewModel;
    private final WorkoutsViewModel workoutsViewModel;

    public RecommendationPresenter(NutritionViewModel nutritionViewModel, WorkoutsViewModel workoutsViewModel) {
        this.nutritionViewModel = nutritionViewModel;
        this.workoutsViewModel = workoutsViewModel;
    }

    @Override
    public void prepareSuccessView(RecommendationOutputData outputData) {
        final NutritionState nutritionState = nutritionViewModel.getState();
        nutritionState.setBmi(outputData.getBmi());
        nutritionState.setDailyCalorieTarget(outputData.getDailyCalorieTarget());
        nutritionState.setDailyProteinGrams(outputData.getDailyProteinGrams());
        nutritionState.setMessage("");
        nutritionViewModel.firePropertyChanged();

        final WorkoutsState workoutsState = workoutsViewModel.getState();
        workoutsState.setWorkoutFocus(outputData.getWorkoutFocus());
        workoutsState.setActivityLevelDescription(outputData.getActivityLevelDescription());
        workoutsState.setMessage("");
        workoutsViewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final NutritionState nutritionState = nutritionViewModel.getState();
        nutritionState.setMessage(errorMessage);
        nutritionViewModel.firePropertyChanged();

        final WorkoutsState workoutsState = workoutsViewModel.getState();
        workoutsState.setMessage(errorMessage);
        workoutsViewModel.firePropertyChanged();
    }
}
