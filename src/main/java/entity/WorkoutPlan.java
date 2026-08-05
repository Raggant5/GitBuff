package entity;

import java.util.List;

/**
 * Represents a full daily workout plan containing multiple exercises
 * and estimated energy burn metrics.
 * Linking attributes ensure all exercises match this workout.
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
    private final Integer userId;

    /**
     * Constructor for AI-generated workout plans without database fields.
     *
     * @param date                    scheduled date
     * @param title                   workout name
     * @param description             brief summary
     * @param category                workout category
     * @param subCategory             specific sub-category
     * @param intensityLevel          intensity level
     * @param targetMuscleGroup       target muscle group
     * @param equipmentType           equipment type
     * @param estimatedDurationMinutes estimated total duration in minutes
     * @param estimatedCaloriesBurned total calories burned estimate
     * @param estimatedFatBurnedGrams fat burned in grams
     * @param estimatedCarbsBurnedGrams carbs burned in grams
     * @param exercises               list of exercises
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
        this.userId = null;
    }

    /**
     * Full constructor with all fields including database identifiers.
     *
     * @param id                       workout plan ID
     * @param userId                   user ID foreign key
     * @param date                     scheduled date
     * @param title                    workout name
     * @param description              brief summary
     * @param category                 workout category
     * @param subCategory              specific sub-category
     * @param intensityLevel           intensity level
     * @param targetMuscleGroup        target muscle group
     * @param equipmentType            equipment type
     * @param estimatedDurationMinutes estimated total duration in minutes
     * @param estimatedCaloriesBurned  total calories burned estimate
     * @param estimatedFatBurnedGrams  fat burned in grams
     * @param estimatedCarbsBurnedGrams carbs burned in grams
     * @param exercises                list of exercises
     */
    public WorkoutPlan(final Integer id, final Integer userId, final String date,
                       final String title, final String description,
                       final String category, final String subCategory,
                       final String intensityLevel, final String targetMuscleGroup,
                       final String equipmentType, final int estimatedDurationMinutes,
                       final int estimatedCaloriesBurned, final int estimatedFatBurnedGrams,
                       final int estimatedCarbsBurnedGrams, final List<Exercise> exercises) {
        this.id = id;
        this.userId = userId;
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
    }

    public Integer getId() {
        return id;
    }

    public Integer getUserId() {
        return userId;
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

    public String getCategory() {
        return category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public String getIntensityLevel() {
        return intensityLevel;
    }

    public String getTargetMuscleGroup() {
        return targetMuscleGroup;
    }

    public String getEquipmentType() {
        return equipmentType;
    }

    public int getEstimatedDurationMinutes() {
        return estimatedDurationMinutes;
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
}
