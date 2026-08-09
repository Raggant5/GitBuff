package use_case.log_workout.exercise_performed;

/**
 * Per-field validation errors for the exercise editor form, returned to the
 * output boundary when an add/edit exercise use case fails validation.
 */
public class ExerciseValidationErrors {

    private String setsError = "";
    private String repsError = "";
    private String weightError = "";
    private String durationError = "";
    private String distanceError = "";
    private String generalError = "";

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

    public String getGeneralError() {
        return generalError;
    }

    public void setGeneralError(String generalError) {
        this.generalError = generalError;
    }

    /**
     * Checks if any errors exist within the exercise form input.
     * @return if any errors exist
     */
    public boolean hasErrors() {
        return !setsError.isEmpty() || !repsError.isEmpty() || !weightError.isEmpty()
                || !durationError.isEmpty() || !distanceError.isEmpty() || !generalError.isEmpty();
    }
}
