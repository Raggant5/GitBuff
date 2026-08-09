package interface_adapter.workouts;

import java.util.List;

/**
 * Display-only view of a single day's AI-recommended workout plan, so
 * {@link WorkoutsState} and {@code view.WorkoutsView} don't need to depend on the
 * {@code entity.WorkoutPlan} entity directly.
 *
 * <p>Mirrors the DTO convention already used elsewhere in the codebase (for example
 * {@code interface_adapter.log_workout.workout.LoggedWorkoutDisplayData}):
 * {@code interface_adapter.recommendation.RecommendationPresenter} converts
 * {@code use_case.recommendation.RecommendationOutputData}'s entities into this type before
 * they reach the state or the view.
 */
public class WorkoutPlanDisplayData {

    private final String date;
    private final String title;
    private final String description;
    private final int estimatedDurationMinutes;
    private final int estimatedCaloriesBurned;
    private final int estimatedFatBurnedGrams;
    private final int estimatedCarbsBurnedGrams;
    private final List<RecommendedExerciseDisplayData> exercises;

    /**
     * Constructs a WorkoutPlanDisplayData instance.
     *
     * @param date scheduled date, as free text (e.g. "Monday, Aug 3")
     * @param title workout name
     * @param description brief summary of the workout
     * @param estimatedDurationMinutes estimated total duration in minutes
     * @param estimatedCaloriesBurned estimated total calories burned
     * @param estimatedFatBurnedGrams estimated fat burned, in grams
     * @param estimatedCarbsBurnedGrams estimated carbohydrates burned, in grams
     * @param exercises the exercises that make up this workout, empty for a rest day
     */
    public WorkoutPlanDisplayData(final String date, final String title, final String description,
                                  final int estimatedDurationMinutes, final int estimatedCaloriesBurned,
                                  final int estimatedFatBurnedGrams, final int estimatedCarbsBurnedGrams,
                                  final List<RecommendedExerciseDisplayData> exercises) {
        this.date = date;
        this.title = title;
        this.description = description;
        this.estimatedDurationMinutes = estimatedDurationMinutes;
        this.estimatedCaloriesBurned = estimatedCaloriesBurned;
        this.estimatedFatBurnedGrams = estimatedFatBurnedGrams;
        this.estimatedCarbsBurnedGrams = estimatedCarbsBurnedGrams;
        this.exercises = exercises;
    }

    public String getDate() {
        return this.date;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public int getEstimatedDurationMinutes() {
        return this.estimatedDurationMinutes;
    }

    public int getEstimatedCaloriesBurned() {
        return this.estimatedCaloriesBurned;
    }

    public int getEstimatedFatBurnedGrams() {
        return this.estimatedFatBurnedGrams;
    }

    public int getEstimatedCarbsBurnedGrams() {
        return this.estimatedCarbsBurnedGrams;
    }

    public List<RecommendedExerciseDisplayData> getExercises() {
        return this.exercises;
    }
}


