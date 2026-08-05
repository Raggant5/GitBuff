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
    private final Integer exerciseId;
    private final Double weight;
    private final Double distance;
    private final Boolean isCardio;

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
        this.exerciseId = null;
        this.weight = null;
        this.distance = null;
        this.isCardio = false;
    }

    public Exercise(final String name, final int sets, final int reps, final int durationMinutes,
                    final String targetMuscleGroup, final String equipmentRequired,
                    final String instructions, final String videoUrl,
                    final Integer exerciseId, final Double weight, final Double distance,
                    final Boolean isCardio) {
        this.name = name;
        this.sets = sets;
        this.reps = reps;
        this.durationMinutes = durationMinutes;
        this.targetMuscleGroup = targetMuscleGroup;
        this.equipmentRequired = equipmentRequired;
        this.instructions = instructions;
        this.videoUrl = videoUrl;
        this.exerciseId = exerciseId;
        this.weight = weight;
        this.distance = distance;
        this.isCardio = isCardio;
    }

    public String getName() {
        return name;
    }

    public int getSets() {
        return sets;
    }

    public int getReps() {
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

    public Integer getExerciseId() {
        return exerciseId;
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
