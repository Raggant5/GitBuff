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

    /**
     * Constructor for AI-generated exercises.
     *
     * @param name exercise name.
     * @param sets number of sets (nullable).
     * @param reps number of reps (nullable).
     * @param durationMinutes duration in minutes.
     * @param targetMuscleGroup target muscle group.
     * @param equipmentRequired equipment needed.
     * @param instructions step-by-step instructions.
     * @param videoUrl YouTube video URL.
     * @param category workout category.
     * @param subCategory specific sub-category.
     * @param intensityLevel intensity level.
     * @param equipmentType equipment type.
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
    }

    /**
     * Gets exercise ID.
     *
     * @return id integer.
     */
    public Integer getId() {
        return this.id;
    }

    /**
     * Gets exercise name.
     *
     * @return name string.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets sets.
     *
     * @return sets integer.
     */
    public Integer getSets() {
        return this.sets;
    }

    /**
     * Gets reps.
     *
     * @return reps integer.
     */
    public Integer getReps() {
        return this.reps;
    }

    /**
     * Gets duration in minutes.
     *
     * @return duration integer.
     */
    public int getDurationMinutes() {
        return this.durationMinutes;
    }

    /**
     * Gets target muscle group.
     *
     * @return target muscle group string.
     */
    public String getTargetMuscleGroup() {
        return this.targetMuscleGroup;
    }

    /**
     * Gets required equipment.
     *
     * @return equipment string.
     */
    public String getEquipmentRequired() {
        return this.equipmentRequired;
    }

    /**
     * Gets exercise instructions.
     *
     * @return instructions string.
     */
    public String getInstructions() {
        return this.instructions;
    }

    /**
     * Gets video URL.
     *
     * @return video URL string.
     */
    public String getVideoUrl() {
        return this.videoUrl;
    }

    /**
     * Gets workout category.
     *
     * @return category string.
     */
    public String getCategory() {
        return this.category;
    }

    /**
     * Gets sub-category.
     *
     * @return sub-category string.
     */
    public String getSubCategory() {
        return this.subCategory;
    }

    /**
     * Gets intensity level.
     *
     * @return intensity level string.
     */
    public String getIntensityLevel() {
        return this.intensityLevel;
    }

    /**
     * Gets equipment type.
     *
     * @return equipment type string.
     */
    public String getEquipmentType() {
        return this.equipmentType;
    }
}
