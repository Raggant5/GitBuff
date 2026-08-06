package entity;

import java.time.LocalDate;

/**
 * Factory for creating logged workouts.
 */
public class LoggedWorkoutFactory {

    /**
     * Creates a new LoggedWorkout.
     * @param date the date the workout was performed
     * @param userId the id of the user who created it
     * @return the new logged workout
     */
    public LoggedWorkout create(String userId, LocalDate date) {
        return new LoggedWorkout(userId, date);
    }
}
