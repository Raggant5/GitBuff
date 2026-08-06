package use_case.log_workout.exercise_performed.delete_exercise;

/**
 * Input Boundary for actions which are related to deleting food from the food history.
 */
public interface DeleteExerciseInputBoundary {

    /**
     * Executes the delete food use case.
     * @param deleteExerciseInputData the food id for food to be deleted
     */
    void execute(DeleteExerciseInputData deleteExerciseInputData);
}
