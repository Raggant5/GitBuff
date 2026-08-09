package use_case.log_workout.exercise_performed.create_exercise;

import use_case.log_workout.StrengthDetailsData;

/**
 * Output Data for the Add Exercise Performed Use Case.
 */
public class AddExercisePerformedOutputData {

    private final Integer id;
    private final String exerciseName;
    private final StrengthDetailsData strengthDetailsData;
    private final double durationMins;
    private final Double distanceKm;
    private final boolean isCardio;

    public AddExercisePerformedOutputData(Integer id, String exerciseName, StrengthDetailsData strengthDetailsData,
                                          double durationMins, Double distanceKm, boolean isCardio) {
        this.id = id;
        this.exerciseName = exerciseName;
        this.strengthDetailsData = strengthDetailsData;
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

    public StrengthDetailsData getStrengthDetailsData() {
        return strengthDetailsData;
    }

    public Integer getSets() {
        return strengthDetailsData.getSets();
    }

    public Integer getReps() {
        return strengthDetailsData.getReps();
    }

    public Double getWeight() {
        return strengthDetailsData.getWeight();
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
