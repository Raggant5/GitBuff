package use_case.recommendation;

import entity.User;

/**
 * DAO for the Recommendation Use Case.
 */
public interface RecommendationUserDataAccessInterface {

    /**
     * Returns the user with the given username.
     * @param username the username to look up
     * @return the user with the given username
     */
    User get(String username);

    /**
     * Returns the username of the current logged-in user.
     * @return the username of the current logged-in user.
     */
    String getCurrentUsername();
}
