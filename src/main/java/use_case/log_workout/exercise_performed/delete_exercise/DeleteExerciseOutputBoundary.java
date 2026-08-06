package use_case.log_workout.exercise_performed.delete_exercise;

/**
 * The output boundary for the Delete Exercise Use Case.
 */
public interface DeleteExerciseOutputBoundary {
    /**
     * Prepares the success view for the Delete Exercise Case.
     * @param deleteExerciseOutputData contains the exercise id to be deleted
     */
    void prepareSuccessView(DeleteExerciseOutputData deleteExerciseOutputData);
}
