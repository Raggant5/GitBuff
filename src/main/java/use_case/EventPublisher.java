package use_case;

/**
 * Boundary an interactor depends on to announce an event, without knowing who is listening or
 * how many features react to it.
 *
 * @param <T> the event type
 */
public interface EventPublisher<T> {

    /**
     * Publishes an event to every subscribed observer.
     *
     * @param event the event to publish
     */
    void publish(T event);
}
