package entity;

/**
 * Factory interface for creating User objects.
 */
public interface UserFactory {

    /**
     * Creates a new User instance.
     *
     * @param name the name of the new user
     * @param password the password of the new user
     * @return the new user
     */
    User create(String name, String password);
}
