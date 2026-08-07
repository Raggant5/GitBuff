package use_case.share;

import entity.User;

/**
 * Data access interface for retrieving share data.
 */
public interface ShareProgressUserDataAccessInterface {

    /**
     * Gets the currently logged in user.
     *
     * @return current user instance
     */
    User getCurrentUser();

    /**
     * Calculates the all-time total minutes worked out across completed workouts.
     *
     * @param username user name
     * @return total minutes worked out
     */
    int getTotalMinutesWorkedOut(final String username);

    /**
     * Gets total completed workout count.
     *
     * @param username user name
     * @return total completed workouts
     */
    int getTotalCompletedWorkouts(final String username);
}
