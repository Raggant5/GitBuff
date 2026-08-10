package use_case.calendar.load_events;

import java.util.List;

import entity.CalendarEvent;

/**
 * Output data containing calendar events loaded for a user.
 */
public class LoadCalendarEventsOutputData {
    private final List<CalendarEvent> calendarEvents;

    /**
     * Creates output data containing the loaded calendar events.
     *
     * @param calendarEvents the loaded calendar events
     */
    public LoadCalendarEventsOutputData(List<CalendarEvent> calendarEvents) {
        this.calendarEvents = List.copyOf(calendarEvents);
    }

    /**
     * Returns the loaded calendar events.
     *
     * @return an unmodifiable list of calendar events
     */
    public List<CalendarEvent> getCalendarEvents() {
        return this.calendarEvents;
    }
}
