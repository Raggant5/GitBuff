package entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class CalendarEventTest {

    @Test
    void constructorStoresEveryCalendarEventValue() {
        final LocalDate activityDate = LocalDate.of(2026, 8, 9);

        final CalendarEvent event = new CalendarEvent(
                "event-17", "amir", "Workout", "Leg day", activityDate);

        assertEquals("event-17", event.getEventId());
        assertEquals("amir", event.getUserId());
        assertEquals("Workout", event.getTitle());
        assertEquals("Leg day", event.getDescription());
        assertEquals(activityDate, event.getActivityDate());
    }
}
