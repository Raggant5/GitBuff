package entity;

/**
 * Represents an exercise performed as part of a logged workout.
 */
public class ExercisePerformed {

    private Integer id;
    private Integer workoutId;
    private String exerciseName;
    private StrengthDetails strengthDetails;
    private double durationMins;
    private Double distanceKm;
    private boolean isCardio;

    /**
     * Constructs a newly performed exercise.
     *
     * @param exerciseName exercise name
     * @param strengthDetails sets, reps, and weight if weightlifting
     * @param durationMins duration in minutes
     * @param distanceKm distance in kilometres
     * @param isCardio whether the exercise is cardio
     */
    public ExercisePerformed(String exerciseName, StrengthDetails strengthDetails,
                             double durationMins, Double distanceKm, boolean isCardio) {
        this.exerciseName = exerciseName;
        this.strengthDetails = strengthDetails;
        this.id = null;
        this.workoutId = null;
        this.durationMins = durationMins;
        this.distanceKm = distanceKm;
        this.isCardio = isCardio;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public Integer getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(final int workoutId) {
        this.workoutId = workoutId;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(final String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public StrengthDetails getStrengthDetails() {
        return strengthDetails;
    }

    /**
     * Get the number of setes or null if cardio exercise.
     * @return number of sets
     */
    public Integer getSets() {
        final Integer result;
        if (strengthDetails == null) {
            result = null;
        }
        else {
            result = strengthDetails.getSets();
        }
        return result;
    }

    /**
     * Get number of reps or null if cardio exercise.
     * @return number of reps
     */
    public Integer getReps() {
        final Integer result;
        if (strengthDetails == null) {
            result = null;
        }
        else {
            result = strengthDetails.getReps();
        }
        return result;
    }

    /**
     * Get weights used in kg if weightlifting exercise.
     * @return value of weights
     */
    public Double getWeight() {
        final Double result;
        if (strengthDetails == null) {
            result = null;
        }
        else {
            result = strengthDetails.getWeight();
        }
        return result;
    }

    public double getDurationMins() {
        return durationMins;
    }

    public void setDurationMins(final double durationMins) {
        this.durationMins = durationMins;
    }

    public Double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(Double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public boolean getIsCardio() {
        return isCardio;
    }

    public void setIsCardio(boolean isCardio) {
        this.isCardio = isCardio;
    }
}
