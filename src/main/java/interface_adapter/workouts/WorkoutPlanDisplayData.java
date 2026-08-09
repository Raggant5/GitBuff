package interface_adapter.workouts;

import java.util.List;

/**
 * Display-only view of a single day's AI-recommended workout plan.
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
     * @param date scheduled date, as free text (e.g. "Monday, Aug 3").
     * @param title workout name.
     * @param description brief summary of the workout.
     * @param estimatedDurationMinutes estimated total duration in minutes.
     * @param estimatedCaloriesBurned estimated total calories burned.
     * @param estimatedFatBurnedGrams estimated fat burned, in grams.
     * @param estimatedCarbsBurnedGrams estimated carbohydrates burned, in grams.
     * @param exercises the exercises that make up this workout, empty for a rest day.
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

    /**
     * Gets workout scheduled date.
     *
     * @return date string.
     */
    public String getDate() {
        return this.date;
    }

    /**
     * Gets workout title.
     *
     * @return title string.
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Gets workout description.
     *
     * @return description string.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Gets estimated duration in minutes.
     *
     * @return duration in minutes.
     */
    public int getEstimatedDurationMinutes() {
        return this.estimatedDurationMinutes;
    }

    /**
     * Gets estimated calories burned.
     *
     * @return calories integer.
     */
    public int getEstimatedCaloriesBurned() {
        return this.estimatedCaloriesBurned;
    }

    /**
     * Gets estimated fat burned in grams.
     *
     * @return fat burned in grams.
     */
    public int getEstimatedFatBurnedGrams() {
        return this.estimatedFatBurnedGrams;
    }

    /**
     * Gets estimated carbohydrates burned in grams.
     *
     * @return carbs burned in grams.
     */
    public int getEstimatedCarbsBurnedGrams() {
        return this.estimatedCarbsBurnedGrams;
    }

    /**
     * Gets recommended exercises list.
     *
     * @return list of recommended exercises.
     */
    public List<RecommendedExerciseDisplayData> getExercises() {
        return this.exercises;
    }
}

