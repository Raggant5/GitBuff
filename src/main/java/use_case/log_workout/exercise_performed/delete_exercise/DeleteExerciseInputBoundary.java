package use_case.log_workout.exercise_performed.delete_exercise;

/**
 * Input Boundary for actions which are related to deleting an exercise from a workout.
 */
public interface DeleteExerciseInputBoundary {

    /**
     * Executes the delete exercise use case.
     * @param deleteExerciseInputData the exercise to be deleted
     */
    void execute(DeleteExerciseInputData deleteExerciseInputData);
}
