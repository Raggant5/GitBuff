package entity;

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

    public ExercisePerformed(String exerciseName, Integer sets, Integer reps,
                             Double weight, double durationMins, Double distanceKm, boolean isCardio) {
        this.exerciseName = exerciseName;
        this.sets = sets;
        this.reps = reps;
        this.id = null;
        this.workoutId = null;
        this.weight = weight;
        this.durationMins = durationMins;
        this.distanceKm = distanceKm;
        this.isCardio = isCardio;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(int workoutId) {
        this.workoutId = workoutId;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public Integer getSets() {
        return sets;
    }

    public void setSets(Integer sets) {
        this.sets = sets;
    }

    public Integer getReps() {
        return reps;
    }

    public void setReps(Integer reps) {
        this.reps = reps;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public double getDurationMins() {
        return durationMins;
    }

    public void setDurationMins(double durationMins) {
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
