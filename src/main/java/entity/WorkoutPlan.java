package entity;

import java.util.List;

public class WorkoutPlan {

    private final String date;
    private final String title;
    private final String description;
    private final int estimatedCaloriesBurned;
    private final int estimatedFatBurnedGrams;
    private final int estimatedCarbsBurnedGrams;
    private final List<Exercise> exercises;
    private final Integer id;
    private final Integer userId;

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
        this.id = null;
        this.userId = null;
    }

    public WorkoutPlan(final String date, final String title, final String description,
                       final int estimatedCaloriesBurned, final int estimatedFatBurnedGrams,
                       final int estimatedCarbsBurnedGrams, final List<Exercise> exercises,
                       final Integer id, final Integer userId) {
        this.date = date;
        this.title = title;
        this.description = description;
        this.estimatedCaloriesBurned = estimatedCaloriesBurned;
        this.estimatedFatBurnedGrams = estimatedFatBurnedGrams;
        this.estimatedCarbsBurnedGrams = estimatedCarbsBurnedGrams;
        this.exercises = exercises;
        this.id = id;
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getEstimatedCaloriesBurned() {
        return estimatedCaloriesBurned;
    }

    public int getEstimatedFatBurnedGrams() {
        return estimatedFatBurnedGrams;
    }

    public int getEstimatedCarbsBurnedGrams() {
        return estimatedCarbsBurnedGrams;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public Integer getId() {
        return id;
    }

    public Integer getUserId() {
        return userId;
    }
}
