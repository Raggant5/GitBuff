package use_case.calendar.add_event;

public interface AddCalendarEventOutputBoundary {
    void prepareSuccessView(AddCalendarEventOutputData outputData);

    void prepareFailureView(String errorMessage);
}
