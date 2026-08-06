package use_case.log_workout.exercise_performed.delete_exercise;

/**
 * DAO for deleting exercises performed
 */
public interface DeleteExerciseDataAccessInterface {

    /**
     * Deletes an exercise performed.
     * @param exerciseId the exercise performed id
     */
    void deleteExercisePerformed(int exerciseId);
}
