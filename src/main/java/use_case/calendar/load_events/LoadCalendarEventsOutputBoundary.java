package use_case.calendar.load_events;

/**
 * Defines how loaded calendar events are presented.
 */
public interface LoadCalendarEventsOutputBoundary {
    /**
     * Presents calendar events that were loaded successfully.
     *
     * @param outputData the loaded calendar events
     */
    void prepareSuccessView(LoadCalendarEventsOutputData outputData);

    /**
     * Presents a load-calendar-events failure.
     *
     * @param errorMessage a description of the failure
     */
    void prepareFailureView(String errorMessage);
}
