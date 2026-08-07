package use_case.calendar.load_events;

public interface LoadCalendarEventsOutputBoundary {
    void prepareSuccessView(LoadCalendarEventsOutputData outputData);

    void prepareFailureView(String errorMessage);
}
