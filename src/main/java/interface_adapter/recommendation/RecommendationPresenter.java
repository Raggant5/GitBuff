package interface_adapter.recommendation;

import interface_adapter.calendar.CalendarController;
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
    private final CalendarController calendarController;

    /**
     * Constructs a RecommendationPresenter instance.
     *
     * @param nutritionViewModel the nutrition view model
     * @param workoutsViewModel the workouts view model
     */
    public RecommendationPresenter(final NutritionViewModel nutritionViewModel,
                                   final WorkoutsViewModel workoutsViewModel,
                                   final CalendarController calendarController) {
        this.nutritionViewModel = nutritionViewModel;
        this.workoutsViewModel = workoutsViewModel;
        this.calendarController = calendarController;
    }

    @Override
    public void prepareSuccessView(final RecommendationOutputData outputData) {
        final NutritionState nutritionState = this.nutritionViewModel.getState();
        nutritionState.setBmi(outputData.getBmi());
        nutritionState.setDailyCalorieTarget(outputData.getDailyCalorieTarget());
        nutritionState.setDailyProteinGrams(outputData.getDailyProteinGrams());
        nutritionState.setMealRecommendations(outputData.getMealRecommendations());
        nutritionState.setMessage("");
        this.nutritionViewModel.firePropertyChanged();

        final WorkoutsState workoutsState = this.workoutsViewModel.getState();
        workoutsState.setWorkoutFocus(outputData.getWorkoutFocus());
        workoutsState.setActivityLevelDescription(outputData.getActivityLevelDescription());
        workoutsState.setWorkoutPlans(outputData.getWorkoutPlans());
        workoutsState.setMessage("");
        this.workoutsViewModel.firePropertyChanged();

        if (this.calendarController != null) {
            this.calendarController.replaceWorkoutPlans(outputData.getWorkoutPlans());
        }
    }

    @Override
    public void prepareFailView(final String errorMessage) {
        final NutritionState nutritionState = this.nutritionViewModel.getState();
        nutritionState.setMessage(errorMessage);
        this.nutritionViewModel.firePropertyChanged();

        final WorkoutsState workoutsState = this.workoutsViewModel.getState();
        workoutsState.setMessage(errorMessage);
        this.workoutsViewModel.firePropertyChanged();
    }
}
