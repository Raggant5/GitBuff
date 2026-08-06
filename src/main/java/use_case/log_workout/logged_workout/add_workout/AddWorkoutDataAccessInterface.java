package use_case.log_workout.logged_workout.add_workout;

import entity.ExercisePerformed;
import entity.LoggedWorkout;

/**
 * DAO for adding a logged workout.
 */
public interface AddWorkoutDataAccessInterface {

    /**
     * Saves the workout.
     *
     * @param workout the workout to save
     * @return the generated workout id
     */
    int saveWorkout(LoggedWorkout workout);

    /**
     * Saves an exercise performed.
     *
     * @param exercisePerformed the exercise performed to save
     * @return the generated exercise performed id
     */
    int saveExercisePerformed(ExercisePerformed exercisePerformed);
}
