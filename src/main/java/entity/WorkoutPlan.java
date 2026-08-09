package entity;

import java.util.List;

/**
 * Represents a full daily workout plan containing multiple exercises
 * and estimated energy burn metrics.
 */
public class WorkoutPlan {

    private final String date;
    private final String title;
    private final String description;

    private final String category;
    private final String subCategory;
    private final String intensityLevel;
    private final String targetMuscleGroup;
    private final String equipmentType;

    private final int estimatedDurationMinutes;
    private final int estimatedCaloriesBurned;
    private final int estimatedFatBurnedGrams;
    private final int estimatedCarbsBurnedGrams;
    private final List<Exercise> exercises;

    private final Integer id;

    /**
     * Constructs a WorkoutPlan instance.
     *
     * @param date scheduled date.
     * @param title workout name.
     * @param description brief summary.
     * @param category workout category.
     * @param subCategory specific sub-category.
     * @param intensityLevel intensity level.
     * @param targetMuscleGroup target muscle group.
     * @param equipmentType equipment type.
     * @param estimatedDurationMinutes estimated total duration in minutes.
     * @param estimatedCaloriesBurned total calories burned estimate.
     * @param estimatedFatBurnedGrams fat burned in grams.
     * @param estimatedCarbsBurnedGrams carbs burned in grams.
     * @param exercises list of exercises.
     */
    public WorkoutPlan(final String date, final String title, final String description,
                       final String category, final String subCategory,
                       final String intensityLevel, final String targetMuscleGroup,
                       final String equipmentType, final int estimatedDurationMinutes,
                       final int estimatedCaloriesBurned, final int estimatedFatBurnedGrams,
                       final int estimatedCarbsBurnedGrams, final List<Exercise> exercises) {
        this.date = date;
        this.title = title;
        this.description = description;
        this.category = category;
        this.subCategory = subCategory;
        this.intensityLevel = intensityLevel;
        this.targetMuscleGroup = targetMuscleGroup;
        this.equipmentType = equipmentType;
        this.estimatedDurationMinutes = estimatedDurationMinutes;
        this.estimatedCaloriesBurned = estimatedCaloriesBurned;
        this.estimatedFatBurnedGrams = estimatedFatBurnedGrams;
        this.estimatedCarbsBurnedGrams = estimatedCarbsBurnedGrams;
        this.exercises = exercises;
        this.id = null;
    }

    /**
     * Gets the unique identifier of the workout plan.
     *
     * @return the workout plan ID, or null if unassigned.
     */
    public Integer getId() {
        return this.id;
    }

    /**
     * Gets the date of the workout plan.
     *
     * @return date string.
     */
    public String getDate() {
        return this.date;
    }

    /**
     * Gets the title of the workout plan.
     *
     * @return title string.
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Gets the description summary of the workout plan.
     *
     * @return description string.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Gets the workout category.
     *
     * @return category string.
     */
    public String getCategory() {
        return this.category;
    }

    /**
     * Gets the workout sub-category.
     *
     * @return sub-category string.
     */
    public String getSubCategory() {
        return this.subCategory;
    }

    /**
     * Gets the intensity level of the workout plan.
     *
     * @return intensity level string.
     */
    public String getIntensityLevel() {
        return this.intensityLevel;
    }

    /**
     * Gets the target muscle group for the workout plan.
     *
     * @return target muscle group string.
     */
    public String getTargetMuscleGroup() {
        return this.targetMuscleGroup;
    }

    /**
     * Gets the equipment type required.
     *
     * @return equipment type string.
     */
    public String getEquipmentType() {
        return this.equipmentType;
    }

    /**
     * Gets the estimated duration in minutes.
     *
     * @return estimated duration in minutes.
     */
    public int getEstimatedDurationMinutes() {
        return this.estimatedDurationMinutes;
    }

    /**
     * Gets the total estimated calories burned.
     *
     * @return estimated calories.
     */
    public int getEstimatedCaloriesBurned() {
        return this.estimatedCaloriesBurned;
    }

    /**
     * Gets the estimated fat burned in grams.
     *
     * @return fat burned in grams.
     */
    public int getEstimatedFatBurnedGrams() {
        return this.estimatedFatBurnedGrams;
    }

    /**
     * Gets the estimated carbohydrates burned in grams.
     *
     * @return carbs burned in grams.
     */
    public int getEstimatedCarbsBurnedGrams() {
        return this.estimatedCarbsBurnedGrams;
    }

    /**
     * Gets the list of exercises included in this plan.
     *
     * @return list of exercise objects.
     */
    public List<Exercise> getExercises() {
        return this.exercises;
    }
}
