package use_case.calendar;

import entity.CalendarEvent;

import java.util.List;

public class RemoveCalendarEventOutputData {
    private List<CalendarEvent> calendarEvents;

    public RemoveCalendarEventOutputData(List<CalendarEvent> calendarEvents) {
        this.calendarEvents = calendarEvents;
    }

    public List<CalendarEvent> getCalendarEvents() {
        return this.calendarEvents;
    }
}
