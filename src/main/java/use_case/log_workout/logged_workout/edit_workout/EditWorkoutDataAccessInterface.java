package use_case.log_workout.logged_workout.edit_workout;

import java.util.List;

import entity.LoggedWorkout;

/**
 * DAO for editing workouts.
 */
public interface EditWorkoutDataAccessInterface {

    /**
     * Updates a workout, persisting its exercises (inserting new ones, updating existing ones)
     * and deleting any removed exercises, all as a single atomic operation.
     * @param workout the updated workout
     * @param exerciseIdsToDelete ids of exercises to remove from the workout
     * @return the persisted workout, with generated ids populated on any newly-inserted exercises
     */
    LoggedWorkout editWorkout(LoggedWorkout workout, List<Integer> exerciseIdsToDelete);
}
