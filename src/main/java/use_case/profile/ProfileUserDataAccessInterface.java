package use_case.profile;

import entity.User;

/**
 * DAO interface for the Edit Profile Use Case.
 */
public interface ProfileUserDataAccessInterface {

    /**
     * Returns the user entity for the given username.
     *
     * @param username username to look up
     * @return user entity instance
     */
    User get(String username);

    /**
     * Saves user details to persistence storage.
     *
     * @param user user entity to save
     */
    void save(User user);

    /**
     * Returns the username of the current logged-in user.
     *
     * @return username string
     */
    String getCurrentUsername();
}
