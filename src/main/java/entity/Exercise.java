package entity;

public class Exercise {
    private final String name;
    private final int sets;
    private final int reps;
    private final int durationMinutes;
    private final String targetMuscleGroup;
    private final String equipmentRequired;
    private final String instructions;
    private final String videoUrl;

    public Exercise(final String name, final int sets, final int reps, final int durationMinutes,
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

    public String getName() { return name; }
    public int getSets() { return sets; }
    public int getReps() { return reps; }
    public int getDurationMinutes() { return durationMinutes; }
    public String getTargetMuscleGroup() { return targetMuscleGroup; }
    public String getEquipmentRequired() { return equipmentRequired; }
    public String getInstructions() { return instructions; }
    public String getVideoUrl() { return videoUrl; }
}
