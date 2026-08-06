package use_case.log_workout.exercise_performed.edit_exercise;

public interface EditExerciseOutputBoundary {

    /**
     * Prepares the success view after editing an exercise performed.
     * @param outputData updated exercise performed data
     */
    void prepareSuccessView(EditExerciseOutputData outputData);

    /**
     * Prepares the failure view.
     * @param errorMessage reason the edit failed
     */
    void prepareFailView(String errorMessage);

}
