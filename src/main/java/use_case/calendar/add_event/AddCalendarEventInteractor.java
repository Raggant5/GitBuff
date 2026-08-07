package use_case.calendar.add_event;

import entity.CalendarEvent;
import use_case.calendar.CalendarEventDataAccessInterface;

import java.util.List;

public class AddCalendarEventInteractor implements AddCalendarEventInputBoundary {
    private final CalendarEventDataAccessInterface outputDAO;
    private final AddCalendarEventOutputBoundary outputBoundary;

    public AddCalendarEventInteractor(CalendarEventDataAccessInterface addCalendarEventAccessObject,
                                      AddCalendarEventOutputBoundary addCalendarEventOutputBoundary){
        this.outputDAO = addCalendarEventAccessObject;
        this.outputBoundary = addCalendarEventOutputBoundary;
    }

    @Override
    public void addCalendarEvent(AddCalendarEventInputData inputData) {
        try {
            outputDAO.addCalendarEvent(
                    inputData.getUserId(),
                    inputData.getTitle(),
                    inputData.getDescription(),
                    inputData.getActivityDate());

            List<CalendarEvent> userEvents =
                    outputDAO.getUserEvents(inputData.getUserId());

            AddCalendarEventOutputData outputData =
                    new AddCalendarEventOutputData(userEvents);

            outputBoundary.prepareSuccessView(outputData);
        }
        catch (IllegalStateException exception) {
            outputBoundary.prepareFailureView(exception.getMessage());
        }
    }
}
