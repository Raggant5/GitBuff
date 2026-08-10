package use_case.log_workout.exercise_performed.prepare_edit_exercise;

import use_case.log_workout.StrengthDetailsInput;

public class PrepareEditExerciseInputData {

    private final Integer id;
    private final String exerciseName;
    private final StrengthDetailsInput strengthDetailsInput;
    private final double durationMins;
    private final Double distanceKm;
    private final boolean isCardio;

    public PrepareEditExerciseInputData(Integer id, String exerciseName, StrengthDetailsInput strengthDetailsInput,
                                        double durationMins, Double distanceKm, boolean isCardio) {
        this.id = id;
        this.exerciseName = exerciseName;
        this.strengthDetailsInput = strengthDetailsInput;
        this.durationMins = durationMins;
        this.distanceKm = distanceKm;
        this.isCardio = isCardio;
    }

    public Integer getId() {
        return id;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public StrengthDetailsInput getStrengthDetailsInput() {
        return strengthDetailsInput;
    }

    public double getDurationMins() {
        return durationMins;
    }

    public Double getDistanceKm() {
        return distanceKm;
    }

    public boolean getIsCardio() {
        return isCardio;
    }
}
