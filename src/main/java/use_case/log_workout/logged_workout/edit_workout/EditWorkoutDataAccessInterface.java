package use_case.log_workout.logged_workout.edit_workout;

import entity.LoggedWorkout;

/**
 * DAO for editing workouts.
 */
public interface EditWorkoutDataAccessInterface {

    /**
     * Updates a workout.
     * @param workout the updated workout
     */
    void editWorkout(LoggedWorkout workout);
}
