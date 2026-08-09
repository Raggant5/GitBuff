package interface_adapter.workouts;

/**
 * Display-only view of a single AI-recommended exercise within a {@link WorkoutPlanDisplayData},
 * so the view in this layer doesn't need to depend on the {@code entity.Exercise} entity
 * directly.
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
     * @param name exercise name
     * @param sets number of sets, or {@code null} if not applicable
     * @param reps number of reps, or {@code null} if not applicable
     * @param durationMinutes duration in minutes
     * @param targetMuscleGroup the primary muscle group targeted
     * @param equipmentRequired the equipment needed to perform the exercise
     * @param instructions step-by-step performance instructions
     * @param videoUrl a link to a video demonstration, or blank if unavailable
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
}

