package entity;

/**
 * Represents an exercise in a workout plan.
 * Contains exercise details and linking attributes to ensure proper
 * matching with workout plans.
 */
public class Exercise {

    private final String name;
    private final Integer sets;
    private final Integer reps;
    private final int durationMinutes;
    private final String targetMuscleGroup;
    private final String equipmentRequired;
    private final String instructions;
    private final String videoUrl;

    private final String category;
    private final String subCategory;
    private final String intensityLevel;
    private final String equipmentType;

    private final Integer id;
    private final Integer workoutId;
    private final Double weight;
    private final Double distance;
    private final Boolean isCardio;

    /**
     * Constructor for AI-generated exercises.
     *
     * @param name               exercise name
     * @param sets               number of sets (nullable)
     * @param reps               number of reps (nullable)
     * @param durationMinutes    duration in minutes
     * @param targetMuscleGroup  target muscle group
     * @param equipmentRequired  equipment needed
     * @param instructions       step-by-step instructions
     * @param videoUrl           YouTube video URL
     * @param category           workout category
     * @param subCategory        specific sub-category
     * @param intensityLevel     intensity level
     * @param equipmentType      equipment type
     */
    public Exercise(final String name, final Integer sets, final Integer reps,
                    final int durationMinutes, final String targetMuscleGroup,
                    final String equipmentRequired, final String instructions,
                    final String videoUrl, final String category,
                    final String subCategory, final String intensityLevel,
                    final String equipmentType) {
        this.name = name;
        this.sets = sets;
        this.reps = reps;
        this.durationMinutes = durationMinutes;
        this.targetMuscleGroup = targetMuscleGroup;
        this.equipmentRequired = equipmentRequired;
        this.instructions = instructions;
        this.videoUrl = videoUrl;
        this.category = category;
        this.subCategory = subCategory;
        this.intensityLevel = intensityLevel;
        this.equipmentType = equipmentType;
        this.id = null;
        this.workoutId = null;
        this.weight = null;
        this.distance = null;
        this.isCardio = false;
    }

    /**
     * Full constructor with all fields including database identifiers.
     *
     * @param id                 exercise ID
     * @param workoutId          workout ID foreign key
     * @param name               exercise name
     * @param sets               number of sets (nullable)
     * @param reps               number of reps (nullable)
     * @param durationMinutes    duration in minutes
     * @param targetMuscleGroup  target muscle group
     * @param equipmentRequired  equipment needed
     * @param instructions       step-by-step instructions
     * @param videoUrl           YouTube video URL
     * @param category           workout category
     * @param subCategory        specific sub-category
     * @param intensityLevel     intensity level
     * @param equipmentType      equipment type
     * @param weight             weight used (nullable)
     * @param distance           distance covered (nullable)
     * @param isCardio           whether exercise is cardio
     */
    public Exercise(final Integer id, final Integer workoutId, final String name,
                    final Integer sets, final Integer reps, final int durationMinutes,
                    final String targetMuscleGroup, final String equipmentRequired,
                    final String instructions, final String videoUrl,
                    final String category, final String subCategory,
                    final String intensityLevel, final String equipmentType,
                    final Double weight, final Double distance, final Boolean isCardio) {
        this.id = id;
        this.workoutId = workoutId;
        this.name = name;
        this.sets = sets;
        this.reps = reps;
        this.durationMinutes = durationMinutes;
        this.targetMuscleGroup = targetMuscleGroup;
        this.equipmentRequired = equipmentRequired;
        this.instructions = instructions;
        this.videoUrl = videoUrl;
        this.category = category;
        this.subCategory = subCategory;
        this.intensityLevel = intensityLevel;
        this.equipmentType = equipmentType;
        this.weight = weight;
        this.distance = distance;
        this.isCardio = isCardio;
    }

    public Integer getId() {
        return id;
    }

    public Integer getWorkoutId() {
        return workoutId;
    }

    public String getName() {
        return name;
    }

    public Integer getSets() {
        return sets;
    }

    public Integer getReps() {
        return reps;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public String getTargetMuscleGroup() {
        return targetMuscleGroup;
    }

    public String getEquipmentRequired() {
        return equipmentRequired;
    }

    public String getInstructions() {
        return instructions;
    }

    public String getVideoUrl() {
        return videoUrl;
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

    public String getEquipmentType() {
        return equipmentType;
    }

    public Double getWeight() {
        return weight;
    }

    public Double getDistance() {
        return distance;
    }

    public Boolean getIsCardio() {
        return isCardio;
    }
}
