package interface_adapter.calendar;

import java.util.List;

/**
 * The state for the Calendar View Model.
 */
public class CalendarState {

    private List<CalendarEventDisplayData> calendarEvents = List.of();
    private String errorMessage;

    /**
     * Sets the error message.
     *
     * @param errorMessage error message string.
     */
    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * Sets list of display events.
     *
     * @param calendarEvents display data list.
     */
    public void setCalendarEvents(final List<CalendarEventDisplayData> calendarEvents) {
        this.calendarEvents = List.copyOf(calendarEvents);
    }

    /**
     * Gets display events list.
     *
     * @return display data list.
     */
    public List<CalendarEventDisplayData> getCalendarEvents() {
        return this.calendarEvents;
    }

    /**
     * Gets error message.
     *
     * @return error message string.
     */
    public String getErrorMessage() {
        return this.errorMessage;
    }
}

