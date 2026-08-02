package use_case.calendar;

public interface RemoveCalendarEventOutputBoundary {
    void prepareSuccessView(RemoveCalendarEventOutputData outputData);

    void prepareFailureView(String errorMessage);
}
