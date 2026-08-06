package use_case.log_workout.exercise_performed.edit_exercise;

import entity.ExercisePerformed;

/**
 * DAO for editing exercises performed.
 */
public interface EditExerciseDataAccessInterface {

    /**
     * Updates an exercise performed.
     * @param exercisePerformed the updated exercise performed
     */
    void editExercisePerformed(ExercisePerformed exercisePerformed);
}
