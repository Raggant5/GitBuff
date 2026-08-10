package use_case.nutrition.meal;

import static org.junit.jupiter.api.Assertions.assertSame;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class MealChangedObserverTest {

    @Test
    void acceptDelegatesToOnMealChanged() {
        final MealChangedEvent[] received = {null};
        final MealChangedObserver observer = event -> received[0] = event;
        final MealChangedEvent event = new MealChangedEvent("aahir", 1, "Breakfast", LocalDate.of(2026, 1, 1),
                MealChangeType.ADDED);

        observer.accept(event);

        assertSame(event, received[0]);
    }
}
