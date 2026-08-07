package use_case.log_workout.logged_workout.prepare_edit_workout;

public interface PrepareEditWorkoutOutputBoundary {

    /**
     * Prepares the success view for switching to editing workout mode.
     * @param outputData the workout to be edited upon
     */
    void prepareSuccessView(PrepareEditWorkoutOutputData outputData);

}
