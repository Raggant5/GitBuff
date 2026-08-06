package interface_adapter.recommendation;

import interface_adapter.nutrition.NutritionState;
import interface_adapter.nutrition.NutritionViewModel;
import interface_adapter.workouts.WorkoutsState;
import interface_adapter.workouts.WorkoutsViewModel;
import use_case.recommendation.RecommendationOutputBoundary;
import use_case.recommendation.RecommendationOutputData;

/**
 * Presenter for the Recommendation Use Case. Updates Nutrition and Workouts view models.
 */
public class RecommendationPresenter implements RecommendationOutputBoundary {

    private final NutritionViewModel nutritionViewModel;
    private final WorkoutsViewModel workoutsViewModel;

    /**
     * Constructs a RecommendationPresenter instance.
     *
     * @param nutritionViewModel the nutrition view model
     * @param workoutsViewModel the workouts view model
     */
    public RecommendationPresenter(final NutritionViewModel nutritionViewModel,
                                   final WorkoutsViewModel workoutsViewModel) {
        this.nutritionViewModel = nutritionViewModel;
        this.workoutsViewModel = workoutsViewModel;
    }

    @Override
    public void prepareSuccessView(final RecommendationOutputData outputData) {
        final NutritionState nutritionState = this.nutritionViewModel.getState();
        nutritionState.setBmi(outputData.getBmi());
        nutritionState.setDailyCalorieTarget(outputData.getDailyCalorieTarget());
        nutritionState.setDailyProteinGrams(outputData.getDailyProteinGrams());
        nutritionState.setMessage("");
        this.nutritionViewModel.firePropertyChanged();

        final WorkoutsState workoutsState = this.workoutsViewModel.getState();
        workoutsState.setWorkoutFocus(outputData.getWorkoutFocus());
        workoutsState.setActivityLevelDescription(outputData.getActivityLevelDescription());
        workoutsState.setWorkoutPlans(outputData.getWorkoutPlans());
        workoutsState.setMessage("");
        workoutsState.setLoading(false);
        this.workoutsViewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(final String errorMessage) {
        final NutritionState nutritionState = this.nutritionViewModel.getState();
        nutritionState.setMessage(errorMessage);
        this.nutritionViewModel.firePropertyChanged();

        final WorkoutsState workoutsState = this.workoutsViewModel.getState();
        workoutsState.setMessage(errorMessage);
        workoutsState.setLoading(false);
        this.workoutsViewModel.firePropertyChanged();
    }
}
