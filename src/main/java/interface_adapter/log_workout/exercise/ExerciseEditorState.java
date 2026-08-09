package interface_adapter.log_workout.exercise;

public class ExerciseEditorState {

    private Integer editingExercisePerformedId;

    private String exerciseName = "";
    private String sets = "";
    private String reps = "";
    private String weight = "";
    private String durationMins = "1.0";
    private String distanceKm = "";
    private boolean isCardio;

    private String setsError = "";
    private String repsError = "";
    private String weightError = "";
    private String durationError = "";
    private String distanceError = "";
    private String submitError = "";

    /**
     * Resets the values when the exercise editor is no longer needed for reuse later.
     */
    public void reset() {
        editingExercisePerformedId = null;

        exerciseName = "";
        sets = "";
        reps = "";
        weight = "";
        durationMins = "1.0";
        distanceKm = "";
        isCardio = false;

        setsError = "";
        repsError = "";
        weightError = "";
        durationError = "";
        distanceError = "";
        submitError = "";
    }

    public Integer getEditingExercisePerformedId() {
        return editingExercisePerformedId;
    }

    public void setEditingExercisePerformedId(Integer editingExercisePerformedId) {
        this.editingExercisePerformedId = editingExercisePerformedId;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public String getSets() {
        return sets;
    }

    public void setSets(String sets) {
        this.sets = sets;
    }

    public String getReps() {
        return reps;
    }

    public void setReps(String reps) {
        this.reps = reps;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getDurationMins() {
        return durationMins;
    }

    public void setDurationMins(String durationMins) {
        this.durationMins = durationMins;
    }

    public String getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(String distanceKm) {
        this.distanceKm = distanceKm;
    }

    public boolean getIsCardio() {
        return isCardio;
    }

    public void setIsCardio(boolean isCardio) {
        this.isCardio = isCardio;
    }

    public String getSetsError() {
        return setsError;
    }

    public void setSetsError(String setsError) {
        this.setsError = setsError;
    }

    public String getRepsError() {
        return repsError;
    }

    public void setRepsError(String repsError) {
        this.repsError = repsError;
    }

    public String getWeightError() {
        return weightError;
    }

    public void setWeightError(String weightError) {
        this.weightError = weightError;
    }

    public String getDurationError() {
        return durationError;
    }

    public void setDurationError(String durationError) {
        this.durationError = durationError;
    }

    public String getDistanceError() {
        return distanceError;
    }

    public void setDistanceError(String distanceError) {
        this.distanceError = distanceError;
    }

    public String getSubmitError() {
        return submitError;
    }

    public void setSubmitError(String submitError) {
        this.submitError = submitError;
    }
}
