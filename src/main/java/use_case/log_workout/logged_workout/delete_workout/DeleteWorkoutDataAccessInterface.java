package use_case.log_workout.logged_workout.delete_workout;

/**
 * DAO for deleting workouts.
 */
public interface DeleteWorkoutDataAccessInterface {

    /**
     * Deletes a workout.
     * @param workoutId the workout id
     */
    void deleteWorkout(int workoutId);
}
