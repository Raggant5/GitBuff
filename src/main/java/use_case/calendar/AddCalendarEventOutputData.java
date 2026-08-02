package use_case.calendar;

import entity.CalendarEvent;

import java.util.List;

public class AddCalendarEventOutputData {

    private final List<CalendarEvent> calendarEvents;

    public AddCalendarEventOutputData(
            List<CalendarEvent> calendarEvents) {
        this.calendarEvents = List.copyOf(calendarEvents);
    }

    public List<CalendarEvent> getCalendarEvents() {
        return calendarEvents;
    }
}
