package interface_adapter.calendar;

import java.util.List;

/**
 * The state for the Calendar View Model.
 */
public class CalendarState {

    private List<CalendarEventDisplayData> calendarEvents = List.of();
    private String errorMessage;

    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setCalendarEvents(final List<CalendarEventDisplayData> calendarEvents) {
        this.calendarEvents = List.copyOf(calendarEvents);
    }

    public List<CalendarEventDisplayData> getCalendarEvents() {
        return this.calendarEvents;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }
}



