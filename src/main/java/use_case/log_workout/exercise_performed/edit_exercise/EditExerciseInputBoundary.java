package use_case.log_workout.exercise_performed.edit_exercise;

public interface EditExerciseInputBoundary {

    /**
     * Executes the Edit Exercise use case.
     * @param inputData the data needed to edit an exercise performed
     */
    void execute(EditExerciseInputData inputData);

}
