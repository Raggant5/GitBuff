package use_case.share;

import java.util.List;

import entity.LoggedWorkout;
import entity.User;

/**
 * Data access interface for retrieving user profile and logged workout data to share.
 */
public interface ShareProgressUserDataAccessInterface {

    /**
     * Gets the currently logged in user.
     *
     * @return current user instance
     */
    User getCurrentUser();

    /**
     * Gets total completed workout count for user.
     *
     * @param username username
     * @return count of logged workouts
     */
    int getTotalCompletedWorkouts(String username);

    /**
     * Gets all-time total minutes worked out for user.
     *
     * @param username username
     * @return total minutes worked out
     */
    double getTotalMinutesWorkedOut(String username);

    /**
     * Gets logged workout list for user.
     *
     * @param username username
     * @return list of logged workouts
     */
    List<LoggedWorkout> getWorkoutsForUser(String username);
}
