package use_case.calendar.remove_event;

import java.util.List;

import entity.CalendarEvent;

/**
 * Output data produced after an event is removed from a user's calendar.
 */
public class RemoveCalendarEventOutputData {
    private final List<CalendarEvent> calendarEvents;

    /**
     * Creates output data containing the user's updated calendar events.
     *
     * @param calendarEvents the updated calendar events
     */
    public RemoveCalendarEventOutputData(List<CalendarEvent> calendarEvents) {
        this.calendarEvents = List.copyOf(calendarEvents);
    }

    /**
     * Returns the user's updated calendar events.
     *
     * @return an unmodifiable list of calendar events
     */
    public List<CalendarEvent> getCalendarEvents() {
        return this.calendarEvents;
    }
}
