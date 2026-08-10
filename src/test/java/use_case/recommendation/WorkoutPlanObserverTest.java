package use_case.recommendation;

import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.List;

import org.junit.jupiter.api.Test;

class WorkoutPlanObserverTest {

    @Test
    void acceptDelegatesToOnWorkoutPlanGenerated() {
        final WorkoutPlanGeneratedEvent[] received = {null};
        final WorkoutPlanObserver observer = event -> received[0] = event;
        final WorkoutPlanGeneratedEvent event = new WorkoutPlanGeneratedEvent("aahir", List.of());

        observer.accept(event);

        assertSame(event, received[0]);
    }
}
