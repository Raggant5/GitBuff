package use_case.log_workout.exercise_performed.edit_exercise;

import use_case.log_workout.StrengthDetailsInput;

public class EditExerciseInputData {

    private final Integer id;
    private final String exerciseName;
    private final StrengthDetailsInput strengthDetailsInput;
    private final boolean isCardio;
    private final String distance;
    private final String duration;

    public EditExerciseInputData(
            Integer id,
            String exerciseName,
            StrengthDetailsInput strengthDetailsInput,
            boolean isCardio,
            String distance,
            String duration) {
        this.id = id;
        this.exerciseName = exerciseName;
        this.strengthDetailsInput = strengthDetailsInput;
        this.isCardio = isCardio;
        this.distance = distance;
        this.duration = duration;
    }

    public Integer getId() {
        return id;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public String getSets() {
        return strengthDetailsInput.getSets();
    }

    public String getReps() {
        return strengthDetailsInput.getReps();
    }

    public String getWeight() {
        return strengthDetailsInput.getWeight();
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
