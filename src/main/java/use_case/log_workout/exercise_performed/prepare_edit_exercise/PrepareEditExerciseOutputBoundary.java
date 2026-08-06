package use_case.log_workout.exercise_performed.prepare_edit_exercise;

public interface PrepareEditExerciseOutputBoundary {

    /**
     * Prepares the success view for switching to editing exercise mode.
     * @param outputData the exercise data to be edited upon
     */
    void prepareSuccessView(PrepareEditExerciseOutputData outputData);

    /**
     * Executes the "Switch to Add Exercise" Use Case.
     */
    void switchToAddExerciseEditor();
}
