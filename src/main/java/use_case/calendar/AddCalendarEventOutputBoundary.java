package use_case.calendar;

public interface AddCalendarEventOutputBoundary {
    void prepareSuccessView(AddCalendarEventOutputData outputData);

    void prepareFailureView(String errorMessage);
}
