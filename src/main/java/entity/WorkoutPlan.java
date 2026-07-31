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

    public String getDate() {
        return this.date;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
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

    public List<Exercise> getExercises() {
        return this.exercises;
    }
}
