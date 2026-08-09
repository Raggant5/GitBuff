package use_case.log_workout.exercise_performed;

import use_case.log_workout.StrengthDetailsInput;

/**
 * Input-side list item for an exercise within a workout being saved - raw strings, the
 * interactor parses when building the entity. Mirrors ExercisePerformedData, which carries
 * the validated numeric form back out.
 */
public class ExercisePerformedInputData {

    private final Integer id;
    private final String exerciseName;
    private final StrengthDetailsInput strengthDetailsInput;
    private final double durationMins;
    private final Double distanceKm;
    private final boolean isCardio;

    public ExercisePerformedInputData(Integer id, String exerciseName, StrengthDetailsInput strengthDetailsInput,
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

    public String getSets() {
        return strengthDetailsInput.getSets();
    }

    public String getReps() {
        return strengthDetailsInput.getReps();
    }

    public String getWeight() {
        return strengthDetailsInput.getWeight();
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
