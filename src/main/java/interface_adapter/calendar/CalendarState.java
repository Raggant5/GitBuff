package interface_adapter.calendar;

import entity.CalendarEvent;

import java.util.List;

public class CalendarState {
    private List<CalendarEvent> calendarEvents = List.of();
    private String errorMessage;

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setCalendarEvents(List<CalendarEvent> calendarEvents) {
        this.calendarEvents = List.copyOf(calendarEvents);
    }

    public List<CalendarEvent> getCalendarEvents() {
        return calendarEvents;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
