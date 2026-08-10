package use_case.calendar.load_events;

/**
 * Output boundary for presenting loaded calendar events or a loading error.
 */
public interface LoadCalendarEventsOutputBoundary {

    /**
     * Executes if Calendar Load Event Use Case is successful.
     * @param outputData the data passed on
     */
    void prepareSuccessView(LoadCalendarEventsOutputData outputData);

    /**
     * Executes if Calendar Load Event Use Case fails.
     * @param errorMessage the error message to display
     */
    void prepareFailureView(String errorMessage);
}
