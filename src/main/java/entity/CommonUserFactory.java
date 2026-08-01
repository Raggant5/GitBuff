package entity;

/**
 * Factory for creating CommonUser objects.
 */
public class CommonUserFactory implements UserFactory {

    @Override
    public User create(final String name, final String password) {
        return new CommonUser(name, password);
    }
}
