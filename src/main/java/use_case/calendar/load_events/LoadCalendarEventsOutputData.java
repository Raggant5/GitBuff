package use_case.calendar.load_events;

import entity.CalendarEvent;

import java.util.List;

public class LoadCalendarEventsOutputData {
    private final List<CalendarEvent> calendarEvents;

    public LoadCalendarEventsOutputData(List<CalendarEvent> calendarEvents) {
        this.calendarEvents = List.copyOf(calendarEvents);
    }

    public List<CalendarEvent> getCalendarEvents() {
        return this.calendarEvents;
    }
}
