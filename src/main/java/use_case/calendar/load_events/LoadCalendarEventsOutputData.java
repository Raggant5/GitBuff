package use_case.calendar.load_events;

import java.util.List;

import use_case.calendar.CalendarEventData;

/**
 * Output data containing the calendar events loaded for a user.
 */
public class LoadCalendarEventsOutputData {
    private final List<CalendarEventData> calendarEvents;

    /**
     * Creates output data for the load-calendar-events use case.
     *
     * @param calendarEvents the loaded calendar events
     */
    public LoadCalendarEventsOutputData(final List<CalendarEventData> calendarEvents) {
        this.calendarEvents = List.copyOf(calendarEvents);
    }

    /**
     * Returns the loaded calendar events.
     *
     * @return an immutable list of calendar event data
     */
    public List<CalendarEventData> getCalendarEvents() {
        return this.calendarEvents;
    }
}
