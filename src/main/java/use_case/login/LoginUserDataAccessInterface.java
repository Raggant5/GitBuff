package use_case.login;

import entity.User;

/**
 * Data Access Object interface for the Login Use Case.
 */
public interface LoginUserDataAccessInterface {

    /**
     * Checks if the given username exists in persistence storage.
     *
     * @param username username to look up
     * @return true if user exists; false otherwise
     */
    boolean existsByName(String username);

    /**
     * Saves user details to persistence storage.
     *
     * @param user user entity to save
     */
    void save(User user);

    /**
     * Returns the user entity for a given username.
     *
     * @param username username to look up
     * @return user entity instance
     */
    User get(String username);

    /**
     * Returns the username of the current logged-in user.
     *
     * @return username string
     */
    String getCurrentUsername();

    /**
     * Saves the username of the current logged-in user.
     *
     * @param username active username
     */
    void setCurrentUsername(String username);
}
