package use_case.calendar.load_events;

/**
 * Defines the input boundary for loading a user's calendar events.
 */
public interface LoadCalendarEventsInputBoundary {
    /**
     * Loads the calendar events for the requested user.
     *
     * @param inputData data identifying the user whose events are requested
     */
    void loadCalendarEvents(LoadCalendarEventsInputData inputData);
}
