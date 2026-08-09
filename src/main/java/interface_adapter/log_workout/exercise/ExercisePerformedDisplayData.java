package interface_adapter.log_workout.exercise;

/**
 * Display-only view of an exercise performed, so panels/state in this layer don't need
 * to depend on the use case DTO directly.
 */
public class ExercisePerformedDisplayData {

    private final Integer id;
    private final String exerciseName;
    private final StrengthDetailsDisplayData strengthDetailsDisplayData;
    private final double durationMins;
    private final Double distanceKm;
    private final boolean isCardio;

    public ExercisePerformedDisplayData(Integer id, String exerciseName,
                                        StrengthDetailsDisplayData strengthDetailsDisplayData,
                                        double durationMins, Double distanceKm, boolean isCardio) {
        this.id = id;
        this.exerciseName = exerciseName;
        this.strengthDetailsDisplayData = strengthDetailsDisplayData;
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

    public StrengthDetailsDisplayData getStrengthDetailsDisplayData() {
        return strengthDetailsDisplayData;
    }

    public String getSets() {
        return strengthDetailsDisplayData.getSets();
    }

    public String getReps() {
        return strengthDetailsDisplayData.getReps();
    }

    public String getWeight() {
        return strengthDetailsDisplayData.getWeight();
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
