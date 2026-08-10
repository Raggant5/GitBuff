package use_case.calendar.load_events;

/**
 * Input boundary for loading a user's calendar events.
 */
public interface LoadCalendarEventsInputBoundary {
    /**
     * Loads Calendar Event Use Case.
     * @param inputData the data necessary to load events such as meal data
     */
    void loadCalendarEvents(LoadCalendarEventsInputData inputData);
}
