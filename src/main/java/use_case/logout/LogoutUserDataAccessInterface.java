package use_case.logout;

/**
 * DAO interface for the Logout Use Case.
 */
public interface LogoutUserDataAccessInterface {

    /**
     * Returns the username of the current user of the application.
     *
     * @return username of current user
     */
    String getCurrentUsername();

    /**
     * Sets the username indicating who is the current active user.
     *
     * @param username new active username
     */
    void setCurrentUsername(String username);
}
