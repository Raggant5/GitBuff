package entity;

/**
 * Represents an exercise with structured metrics, instructions, and equipment requirements.
 */
public class Exercise {

    private final String name;
    private final int sets;
    private final int reps;
    private final int durationMinutes;
    private final String targetMuscleGroup;
    private final String equipmentRequired;
    private final String instructions;
    private final String videoUrl;

    /**
     * Constructs an Exercise instance.
     *
     * @param name exercise title
     * @param sets number of prescribed sets
     * @param reps number of repetitions per set
     * @param durationMinutes estimated duration in minutes
     * @param targetMuscleGroup target muscle group or body section
     * @param equipmentRequired equipment needed for execution
     * @param instructions step-by-step guidance
     * @param videoUrl link to search or view video/GIF demo
     */
    public Exercise(final String name, final int sets, final int reps,
                    final int durationMinutes, final String targetMuscleGroup,
                    final String equipmentRequired, final String instructions,
                    final String videoUrl) {
        this.name = name;
        this.sets = sets;
        this.reps = reps;
        this.durationMinutes = durationMinutes;
        this.targetMuscleGroup = targetMuscleGroup;
        this.equipmentRequired = equipmentRequired;
        this.instructions = instructions;
        this.videoUrl = videoUrl;
    }

    /**
     * Gets the exercise name.
     *
     * @return exercise name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the number of prescribed sets.
     *
     * @return sets count
     */
    public int getSets() {
        return this.sets;
    }

    /**
     * Gets the number of repetitions per set.
     *
     * @return reps count
     */
    public int getReps() {
        return this.reps;
    }

    /**
     * Gets the duration of the exercise in minutes.
     *
     * @return duration in minutes
     */
    public int getDurationMinutes() {
        return this.durationMinutes;
    }

    /**
     * Gets the targeted muscle group.
     *
     * @return muscle group name
     */
    public String getTargetMuscleGroup() {
        return this.targetMuscleGroup;
    }

    /**
     * Gets the equipment required for this exercise.
     *
     * @return equipment required string
     */
    public String getEquipmentRequired() {
        return this.equipmentRequired;
    }

    /**
     * Gets step-by-step exercise instructions.
     *
     * @return instructions string
     */
    public String getInstructions() {
        return this.instructions;
    }

    /**
     * Gets the link to video or visual demonstration.
     *
     * @return video URL string
     */
    public String getVideoUrl() {
        return this.videoUrl;
    }
}
