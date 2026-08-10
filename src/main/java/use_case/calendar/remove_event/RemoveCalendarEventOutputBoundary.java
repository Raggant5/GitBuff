package use_case.calendar.remove_event;

/**
 * Defines how the result of removing a calendar event is presented.
 */
public interface RemoveCalendarEventOutputBoundary {
    /**
     * Presents a successful remove-calendar-event result.
     *
     * @param outputData the updated calendar events
     */
    void prepareSuccessView(RemoveCalendarEventOutputData outputData);

    /**
     * Presents a remove-calendar-event failure.
     *
     * @param errorMessage a description of the failure
     */
    void prepareFailureView(String errorMessage);
}
