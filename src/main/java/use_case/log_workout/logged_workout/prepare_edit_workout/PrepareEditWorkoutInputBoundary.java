package use_case.log_workout.logged_workout.prepare_edit_workout;

public interface PrepareEditWorkoutInputBoundary {

    /**
     * Executes the Prepare Edit Workout use case.
     * @param inputData the workout to be edited upon
     */
    void execute(PrepareEditWorkoutInputData inputData);

}
