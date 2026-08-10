package use_case.calendar.load_events;

public interface LoadCalendarEventsInputBoundary {
    /**
     * Loads Calendar Event Use Case.
     * @param inputData the data necessary to load events such as meal data
     */
    void loadCalendarEvents(LoadCalendarEventsInputData inputData);
}
