package use_case.signup;

import entity.User;

/**
 * DAO interface for the Signup Use Case.
 */
public interface SignupUserDataAccessInterface {

    /**
     * Checks if the given username exists in persistence storage.
     *
     * @param username the username to look for
     * @return true if a user with the given username exists; false otherwise
     */
    boolean existsByName(String username);

    /**
     * Saves the user entity.
     *
     * @param user the user to save
     */
    void save(User user);

    /**
     * Saves the username of the current logged-in user.
     *
     * @param username the username to save
     */
    void setCurrentUsername(String username);
}
