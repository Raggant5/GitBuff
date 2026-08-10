package use_case.calendar.add_event;

/**
 * Defines the input boundary for adding a calendar event.
 */
public interface AddCalendarEventInputBoundary {
    /**
     * Adds a calendar event using the supplied input data.
     *
     * @param addCalendarEventInputData the event information supplied by the caller
     */
    void addCalendarEvent(AddCalendarEventInputData addCalendarEventInputData);
}
