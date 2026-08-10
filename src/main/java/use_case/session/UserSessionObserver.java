package use_case.session;

import java.util.function.Consumer;

/**
 * Observer notified when a user logs in.
 */
public interface UserSessionObserver extends Consumer<UserLoggedInEvent> {

    /**
     * Called once, after the Login use case has succeeded.
     *
     * @param event the login event, carrying the use case's output data
     */
    void onUserLoggedIn(UserLoggedInEvent event);

    @Override
    default void accept(UserLoggedInEvent event) {
        onUserLoggedIn(event);
    }
}
