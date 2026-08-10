package use_case.calendar.add_event;

/**
 * Defines how the result of adding a calendar event is presented.
 */
public interface AddCalendarEventOutputBoundary {
    /**
     * Presents a successful add-calendar-event result.
     *
     * @param outputData the updated calendar events
     */
    void prepareSuccessView(AddCalendarEventOutputData outputData);

    /**
     * Presents an add-calendar-event failure.
     *
     * @param errorMessage a description of the failure
     */
    void prepareFailureView(String errorMessage);
}
