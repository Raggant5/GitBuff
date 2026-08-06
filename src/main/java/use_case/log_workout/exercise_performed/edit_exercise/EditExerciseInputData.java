package use_case.log_workout.exercise_performed.edit_exercise;

import entity.ExercisePerformed;

public class EditExerciseInputData {

    private final ExercisePerformed exercisePerformed;
    private final String exerciseName;
    private final String sets;
    private final String reps;
    private final String weight;
    private final boolean isCardio;
    private final String distance;
    private final String duration;

    public EditExerciseInputData(
            ExercisePerformed exercisePerformed,
            String exerciseName,
            String sets,
            String reps,
            String weight,
            boolean isCardio,
            String distance,
            String duration
    ) {
        this.exercisePerformed = exercisePerformed;
        this.exerciseName = exerciseName;
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
        this.isCardio = isCardio;
        this.distance = distance;
        this.duration = duration;
    }

    public ExercisePerformed getExercisePerformed() {
        return exercisePerformed;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public String getSets() {
        return sets;
    }

    public String getReps() {
        return reps;
    }

    public String getWeight() {
        return weight;
    }

    public boolean isCardio() {
        return isCardio;
    }

    public String getDistance() {
        return distance;
    }

    public String getDuration() {
        return duration;
    }
}
