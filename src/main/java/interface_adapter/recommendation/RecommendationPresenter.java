package interface_adapter.recommendation;

import java.util.List;

import entity.Exercise;
import entity.WorkoutPlan;
import interface_adapter.calendar.CalendarController;
import interface_adapter.nutrition.NutritionState;
import interface_adapter.nutrition.NutritionViewModel;
import interface_adapter.workouts.RecommendedExerciseDisplayData;
import interface_adapter.workouts.WorkoutPlanDisplayData;
import interface_adapter.workouts.WorkoutsState;
import interface_adapter.workouts.WorkoutsViewModel;
import use_case.recommendation.RecommendationOutputBoundary;
import use_case.recommendation.RecommendationOutputData;

/**
 * Presenter for the Recommendation Use Case. Updates Nutrition and Workouts view models.
 *
 * <p>Converts the {@code entity.WorkoutPlan}/{@code entity.Exercise} entities returned by the
 * use case layer into {@link WorkoutPlanDisplayData}/{@link RecommendedExerciseDisplayData}
 * before they reach {@link WorkoutsState} - the use case layer is allowed to depend on
 * entities, but this interface_adapter layer is not.
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
     * @param calendarController controller used to reflect new workout plans on the calendar
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
        final List<WorkoutPlanDisplayData> workoutPlans = outputData.getWorkoutPlans().stream()
                .map(this::toDisplayData)
                .toList();
        workoutsState.setWorkoutPlans(workoutPlans);
        workoutsState.setMessage("");
        workoutsState.setLoading(false);
        this.workoutsViewModel.firePropertyChanged();

        if (this.calendarController != null) {
            this.calendarController.replaceWorkoutPlans(workoutPlans);
        }
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

    private WorkoutPlanDisplayData toDisplayData(final WorkoutPlan plan) {
        final List<RecommendedExerciseDisplayData> exercises = plan.getExercises().stream()
                .map(this::toDisplayData)
                .toList();
        return new WorkoutPlanDisplayData(
                plan.getDate(), plan.getTitle(), plan.getDescription(),
                plan.getEstimatedDurationMinutes(), plan.getEstimatedCaloriesBurned(),
                plan.getEstimatedFatBurnedGrams(), plan.getEstimatedCarbsBurnedGrams(), exercises);
    }

    private RecommendedExerciseDisplayData toDisplayData(final Exercise exercise) {
        return new RecommendedExerciseDisplayData(
                exercise.getName(), exercise.getSets(), exercise.getReps(), exercise.getDurationMinutes(),
                exercise.getTargetMuscleGroup(), exercise.getEquipmentRequired(),
                exercise.getInstructions(), exercise.getVideoUrl());
    }
}


