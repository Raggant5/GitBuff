package use_case.log_workout.logged_workout.edit_workout;

public interface EditWorkoutInputBoundary {

    /**
     * Executes the Edit Workout use case.
     * @param inputData the input data required to edit a workout
     */
    void execute(EditWorkoutInputData inputData);

}
