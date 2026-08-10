package use_case.recommendation;

import entity.Exercise;

/**
 * Use case boundary DTO mirroring Exercise entity, covering only the fields consumed
 * downstream (display + calendar sync never need category/subCategory/intensityLevel/
 * equipmentType).
 */
public class ExerciseData {

    private final String name;
    private final Integer sets;
    private final Integer reps;
    private final int durationMinutes;
    private final String targetMuscleGroup;
    private final String equipmentRequired;
    private final String instructions;
    private final String videoUrl;

    public ExerciseData(final String name, final Integer sets, final Integer reps, final int durationMinutes,
                        final String targetMuscleGroup, final String equipmentRequired,
                        final String instructions, final String videoUrl) {
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
     * Converts a {@code entity.Exercise} into its boundary DTO form.
     *
     * @param exercise the entity to convert
     * @return the equivalent DTO
     */
    public static ExerciseData from(final Exercise exercise) {
        return new ExerciseData(
                exercise.getName(), exercise.getSets(), exercise.getReps(), exercise.getDurationMinutes(),
                exercise.getTargetMuscleGroup(), exercise.getEquipmentRequired(),
                exercise.getInstructions(), exercise.getVideoUrl());
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
