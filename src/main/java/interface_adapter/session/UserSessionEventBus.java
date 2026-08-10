package interface_adapter.session;

import java.util.ArrayList;
import java.util.List;

/**
 * Subject in the Observer design pattern: keeps a list of {@link UserSessionObserver}s and
 * notifies every one of them when a user logs in.
 *
 * <p>{@code interface_adapter.login.LoginPresenter} publishes to this bus instead of holding
 * direct references to every feature that cares about login. {@code app.AppBuilder} wires each
 * feature's observer into the same bus instance as it constructs that feature, so subscription
 * order matches construction order but observers don't depend on each other or on being called
 * in any particular order.
 */
public class UserSessionEventBus {

    private final List<UserSessionObserver> observers = new ArrayList<>();

    /**
     * Subscribes an observer to future login events.
     *
     * @param observer the observer to notify
     */
    public void subscribe(final UserSessionObserver observer) {
        this.observers.add(observer);
    }

    /**
     * Notifies every subscribed observer that a user has logged in.
     *
     * @param event the login event to publish
     */
    public void publishUserLoggedIn(final UserLoggedInEvent event) {
        for (final UserSessionObserver observer : this.observers) {
            observer.onUserLoggedIn(event);
        }
    }
}

