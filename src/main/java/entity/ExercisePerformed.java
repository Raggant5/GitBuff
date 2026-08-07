package entity;

/**
 * Represents an exercise performed as part of a logged workout.
 */
public class ExercisePerformed {

    private Integer id;
    private Integer workoutId;
    private String exerciseName;
    private Integer sets;
    private Integer reps;
    private Double weight;
    private double durationMins;
    private Double distanceKm;
    private boolean isCardio;

    /**
     * Constructs a newly performed exercise.
     *
     * @param exerciseName exercise name
     * @param sets number of sets
     * @param reps number of reps
     * @param weight weight used
     * @param durationMins duration in minutes
     * @param distanceKm distance in kilometres
     * @param isCardio whether the exercise is cardio
     */
    public ExercisePerformed(
            final String exerciseName,
            final Integer sets,
            final Integer reps,
            final Double weight,
            final double durationMins,
            final Double distanceKm,
            final boolean isCardio
    ) {
        this(
                null,
                null,
                exerciseName,
                sets,
                reps,
                weight,
                durationMins,
                distanceKm,
                isCardio
        );
    }

    /**
     * Constructs an exercise loaded from persistence.
     *
     * @param id database exercise ID
     * @param workoutId owning workout ID
     * @param exerciseName exercise name
     * @param sets number of sets
     * @param reps number of reps
     * @param weight weight used
     * @param durationMins duration in minutes
     * @param distanceKm distance in kilometres
     * @param isCardio whether the exercise is cardio
     */
    public ExercisePerformed(
            final Integer id,
            final Integer workoutId,
            final String exerciseName,
            final Integer sets,
            final Integer reps,
            final Double weight,
            final double durationMins,
            final Double distanceKm,
            final boolean isCardio
    ) {
        this.id = id;
        this.workoutId = workoutId;
        this.exerciseName = exerciseName;
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
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
        return this.workoutId;
    }

    public void setWorkoutId(final int workoutId) {
        this.workoutId = workoutId;
    }

    public String getExerciseName() {
        return this.exerciseName;
    }

    public void setExerciseName(
            final String exerciseName
    ) {
        this.exerciseName = exerciseName;
    }

    public Integer getSets() {
        return this.sets;
    }

    public void setSets(final Integer sets) {
        this.sets = sets;
    }

    public Integer getReps() {
        return this.reps;
    }

    public void setReps(final Integer reps) {
        this.reps = reps;
    }

    public Double getWeight() {
        return this.weight;
    }

    public void setWeight(final Double weight) {
        this.weight = weight;
    }

    public double getDurationMins() {
        return this.durationMins;
    }

    public void setDurationMins(
            final double durationMins
    ) {
        this.durationMins = durationMins;
    }

    public Double getDistanceKm() {
        return this.distanceKm;
    }

    public void setDistanceKm(
            final Double distanceKm
    ) {
        this.distanceKm = distanceKm;
    }

    public boolean getIsCardio() {
        return this.isCardio;
    }

    public void setIsCardio(
            final boolean isCardio
    ) {
        this.isCardio = isCardio;
    }
}