package entity;

import java.util.List;

/**
 * Represents a full daily workout plan containing multiple exercises and estimated energy burn.
 */
public class WorkoutPlan {

    private final String date;
    private final String title;
    private final String description;
    private final int estimatedCaloriesBurned;
    private final int estimatedFatBurnedGrams;
    private final int estimatedCarbsBurnedGrams;
    private final List<Exercise> exercises;

    /**
     * Constructs a WorkoutPlan instance.
     *
     * @param date scheduled date or day identifier
     * @param title workout name
     * @param description brief summary
     * @param estimatedCaloriesBurned total calories estimated to burn
     * @param estimatedFatBurnedGrams fat burned in grams
     * @param estimatedCarbsBurnedGrams carbs burned in grams
     * @param exercises list of prescribed exercises
     */
    public WorkoutPlan(final String date, final String title, final String description,
                       final int estimatedCaloriesBurned, final int estimatedFatBurnedGrams,
                       final int estimatedCarbsBurnedGrams, final List<Exercise> exercises) {
        this.date = date;
        this.title = title;
        this.description = description;
        this.estimatedCaloriesBurned = estimatedCaloriesBurned;
        this.estimatedFatBurnedGrams = estimatedFatBurnedGrams;
        this.estimatedCarbsBurnedGrams = estimatedCarbsBurnedGrams;
        this.exercises = exercises;
    }

    /**
     * Gets the date or day label of the plan.
     *
     * @return date string
     */
    public String getDate() {
        return this.date;
    }

    /**
     * Gets the title of the workout plan.
     *
     * @return title string
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Gets the description summary of the workout.
     *
     * @return description string
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Gets the estimated total calories burned.
     *
     * @return calories burned
     */
    public int getEstimatedCaloriesBurned() {
        return this.estimatedCaloriesBurned;
    }

    /**
     * Gets the estimated fat burned in grams.
     *
     * @return fat burned in grams
     */
    public int getEstimatedFatBurnedGrams() {
        return this.estimatedFatBurnedGrams;
    }

    /**
     * Gets the estimated carbs burned in grams.
     *
     * @return carbs burned in grams
     */
    public int getEstimatedCarbsBurnedGrams() {
        return this.estimatedCarbsBurnedGrams;
    }

    /**
     * Gets the list of prescribed exercises.
     *
     * @return list of exercise objects
     */
    public List<Exercise> getExercises() {
        return this.exercises;
    }
}
