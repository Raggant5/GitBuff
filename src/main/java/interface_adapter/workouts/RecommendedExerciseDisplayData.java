package interface_adapter.workouts;

/**
 * Display-only view of a single AI-recommended exercise within a {@link WorkoutPlanDisplayData}.
 */
public class RecommendedExerciseDisplayData {

    private final String name;
    private final Integer sets;
    private final Integer reps;
    private final int durationMinutes;
    private final String targetMuscleGroup;
    private final String equipmentRequired;
    private final String instructions;
    private final String videoUrl;

    /**
     * Constructs a RecommendedExerciseDisplayData instance.
     *
     * @param name exercise name.
     * @param sets number of sets, or {@code null} if not applicable.
     * @param reps number of reps, or {@code null} if not applicable.
     * @param durationMinutes duration in minutes.
     * @param targetMuscleGroup the primary muscle group targeted.
     * @param equipmentRequired the equipment needed to perform the exercise.
     * @param instructions step-by-step performance instructions.
     * @param videoUrl a link to a video demonstration, or blank if unavailable.
     */
    public RecommendedExerciseDisplayData(final String name, final Integer sets, final Integer reps,
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
     * Gets exercise name.
     *
     * @return name string.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets number of sets.
     *
     * @return sets integer, or null.
     */
    public Integer getSets() {
        return this.sets;
    }

    /**
     * Gets number of reps.
     *
     * @return reps integer, or null.
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
     * Gets instructions.
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
}

