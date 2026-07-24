package use_case.profile;

import entity.User;

/**
 * DAO for the Edit Profile Use Case.
 */
public interface ProfileUserDataAccessInterface {

    /**
     * Returns the user with the given username.
     * @param username the username to look up
     * @return the user with the given username
     */
    User get(String username);

    /**
     * Saves the user.
     * @param user the user to save
     */
    void save(User user);

    /**
     * Returns the username of the current logged-in user.
     * @return the username of the current logged-in user.
     */
    String getCurrentUsername();
}
