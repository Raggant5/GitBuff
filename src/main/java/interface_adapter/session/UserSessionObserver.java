package interface_adapter.session;

/**
 * Observer notified when a user logs in.
 *
 * <p>Each feature that previously needed to react to login (populating its own view model,
 * kicking off its own refresh) implements this interface and subscribes to a
 * {@link UserSessionEventBus} instead of being called directly by
 * {@code interface_adapter.login.LoginPresenter}. This is the Observer design pattern applied
 * across features rather than within a single {@code ViewModel} (see
 * {@code interface_adapter.ViewModel}, which already uses
 * {@link java.beans.PropertyChangeSupport} for the same pattern at the view-binding level):
 * {@code LoginPresenter} (the subject) no longer needs to know how many features care about
 * login, or what each of them does about it - new features subscribe without
 * {@code LoginPresenter} changing at all (Open/Closed Principle), and {@code LoginPresenter}
 * itself shrinks to just its own actual responsibility (Single Responsibility Principle).
 */
public interface UserSessionObserver {

    /**
     * Called once, after the Login use case has succeeded.
     *
     * @param event the login event, carrying the use case's output data
     */
    void onUserLoggedIn(UserLoggedInEvent event);
}

