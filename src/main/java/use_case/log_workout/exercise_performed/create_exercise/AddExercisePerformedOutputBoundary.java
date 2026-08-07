package use_case.log_workout.exercise_performed.create_exercise;

/**
 * The output boundary for the Add Exercise Performed Use Case.
 */
public interface AddExercisePerformedOutputBoundary {
    /**
     * Prepares the success view for the Add Exercise Performed Entry Case.
     * @param outputData the output data
     */
    void prepareSuccessView(AddExercisePerformedOutputData outputData);

    /**
     * Prepares the failure view for the Add Exercise Performed Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}
