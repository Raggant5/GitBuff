package use_case.log_workout.exercise_performed.edit_exercise;

import use_case.log_workout.exercise_performed.ExerciseValidationErrors;

public interface EditExerciseOutputBoundary {

    /**
     * Prepares the success view after editing an exercise performed.
     * @param outputData updated exercise performed data
     */
    void prepareSuccessView(EditExerciseOutputData outputData);

    /**
     * Prepares the failure view.
     * @param errors per-field reason the edit failed
     */
    void prepareFailView(ExerciseValidationErrors errors);

}
