package use_case.profile;

import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

class ProfileUpdatedObserverTest {

    @Test
    void acceptDelegatesToOnProfileUpdated() {
        final ProfileUpdatedEvent[] received = {null};
        final ProfileUpdatedObserver observer = event -> received[0] = event;
        final ProfileUpdatedEvent event = new ProfileUpdatedEvent("aahir");

        observer.accept(event);

        assertSame(event, received[0]);
    }
}
