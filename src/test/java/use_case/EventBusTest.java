package use_case;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class EventBusTest {

    @Test
    void publishNotifiesAllSubscribers() {
        final EventBus<String> eventBus = new EventBus<>();
        final List<String> firstReceived = new ArrayList<>();
        final List<String> secondReceived = new ArrayList<>();
        eventBus.subscribe(firstReceived::add);
        eventBus.subscribe(secondReceived::add);

        eventBus.publish("event-1");

        assertEquals(List.of("event-1"), firstReceived);
        assertEquals(List.of("event-1"), secondReceived);
    }

    @Test
    void publishWithNoSubscribersDoesNothing() {
        final EventBus<String> eventBus = new EventBus<>();

        eventBus.publish("event-1");
    }
}
