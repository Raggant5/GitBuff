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
    }

    public Integer getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Integer getSets() {
        return this.sets;
    }

    public Integer getReps() {
        return this.reps;
    }

    public int getDurationMinutes() {
        return this.durationMinutes;
    }

    public String getTargetMuscleGroup() {
        return this.targetMuscleGroup;
    }

    public String getEquipmentRequired() {
        return this.equipmentRequired;
    }

    public String getInstructions() {
        return this.instructions;
    }

    public String getVideoUrl() {
        return this.videoUrl;
    }

    public String getCategory() {
        return this.category;
    }

    public String getSubCategory() {
        return this.subCategory;
    }

    public String getIntensityLevel() {
        return this.intensityLevel;
    }

    public String getEquipmentType() {
        return this.equipmentType;
    }
}
