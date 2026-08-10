package use_case.calendar.remove_event;

/**
 * Defines the input boundary for removing a calendar event.
 */
public interface RemoveCalendarEventInputBoundary {
    /**
     * Removes the calendar event identified by the supplied input data.
     *
     * @param removeCalendarEventInputData data identifying the event to remove
     */
    void removeCalendarEvent(RemoveCalendarEventInputData removeCalendarEventInputData);
}
