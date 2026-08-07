package use_case.log_workout.exercise_performed.prepare_edit_exercise;

public interface PrepareEditExerciseInputBoundary {

    /**
     * Executes the Prepare Edit Exercise use case.
     * @param inputData the data to be edited upon
     */
    void execute(PrepareEditExerciseInputData inputData);

    /**
     * Executes the "Switch to Add Exercise" Use Case.
     */
    void switchToAddExerciseEditor();
}
