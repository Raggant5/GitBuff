package use_case.recommendation;

import entity.User;

/**
 * DAO interface for the Recommendation Use Case.
 */
public interface RecommendationUserDataAccessInterface {

    /**
     * Returns the user entity for the given username.
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
}
