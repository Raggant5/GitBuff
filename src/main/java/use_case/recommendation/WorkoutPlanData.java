package use_case.recommendation;

import java.util.ArrayList;
import java.util.List;

import entity.Exercise;
import entity.WorkoutPlan;

/**
 * Use case boundary DTO mirroring entity WorkoutPlan.
 */
public class WorkoutPlanData {

    private final String date;
    private final String title;
    private final String description;
    private final int estimatedDurationMinutes;
    private final int estimatedCaloriesBurned;
    private final int estimatedFatBurnedGrams;
    private final int estimatedCarbsBurnedGrams;
    private final List<ExerciseData> exercises;

    public WorkoutPlanData(final String date, final String title, final String description,
                           final int estimatedDurationMinutes, final int estimatedCaloriesBurned,
                           final int estimatedFatBurnedGrams, final int estimatedCarbsBurnedGrams,
                           final List<ExerciseData> exercises) {
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
     * Converts a WorkoutPlan entity into its boundary DTO form.
     *
     * @param plan the entity to convert
     * @return the equivalent DTO
     */
    public static WorkoutPlanData from(final WorkoutPlan plan) {
        final List<ExerciseData> exercises = new ArrayList<>();
        for (final Exercise exercise : plan.getExercises()) {
            exercises.add(ExerciseData.from(exercise));
        }
        return new WorkoutPlanData(
                plan.getDate(), plan.getTitle(), plan.getDescription(), plan.getEstimatedDurationMinutes(),
                plan.getEstimatedCaloriesBurned(), plan.getEstimatedFatBurnedGrams(),
                plan.getEstimatedCarbsBurnedGrams(), exercises);
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

    public List<ExerciseData> getExercises() {
        return this.exercises;
    }
}
