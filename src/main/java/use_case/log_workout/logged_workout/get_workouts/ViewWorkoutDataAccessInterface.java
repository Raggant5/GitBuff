package use_case.log_workout.logged_workout.get_workouts;

import java.util.List;

import entity.ExercisePerformed;
import entity.LoggedWorkout;

/**
 * DAO for viewing logged workout data.
 */
public interface ViewWorkoutDataAccessInterface {

    /**
     * Gets all workouts for a user.
     * @param userId the user id
     * @return the user's workouts
     */
    List<LoggedWorkout> getWorkoutsForUser(String userId);

    /**
     * Gets the exercises performed for a workout.
     * @param workoutId the workout id
     * @return the exercises performed
     */
    List<ExercisePerformed> getExercisesForWorkout(int workoutId);

    /**
     * Gets a single workout, including its exercises, by id.
     * @param workoutId the workout id
     * @return the workout
     */
    LoggedWorkout getWorkoutById(int workoutId);
}
