package use_case;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Subject in the Observer design pattern: keeps a list of subscribers and notifies every one of
 * them when an event is published.
 *
 * @param <T> the event type
 */
public class EventBus<T> implements EventPublisher<T> {

    private final List<Consumer<T>> subscribers = new ArrayList<>();

    /**
     * Subscribes an observer to future events.
     *
     * @param subscriber the observer to notify
     */
    public void subscribe(final Consumer<T> subscriber) {
        this.subscribers.add(subscriber);
    }

    @Override
    public void publish(final T event) {
        for (final Consumer<T> subscriber : this.subscribers) {
            subscriber.accept(event);
        }
    }
}
