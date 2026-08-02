package use_case.calendar.remove_event;

import entity.CalendarEvent;

import java.util.List;

public class RemoveCalendarEventOutputData {
    private List<CalendarEvent> calendarEvents;

    public RemoveCalendarEventOutputData(List<CalendarEvent> calendarEvents) {
        this.calendarEvents = List.copyOf(calendarEvents);
    }

    public List<CalendarEvent> getCalendarEvents() {
        return this.calendarEvents;
    }
}
