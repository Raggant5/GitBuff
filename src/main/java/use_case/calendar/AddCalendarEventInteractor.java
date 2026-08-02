package use_case.calendar;

import entity.CalendarEvent;

import java.util.List;

public class AddCalendarEventInteractor implements AddCalendarEventInputBoundary{
    private CalendarEventDataAccessInterface outputDAO;
    private AddCalendarEventOutputBoundary outputData;

    public AddCalendarEventInteractor(CalendarEventDataAccessInterface addCalendarEventAccessObject,
                                      AddCalendarEventOutputBoundary addCalendarEventOutputBoundary){
        this.outputDAO = addCalendarEventAccessObject;
        this.outputData = addCalendarEventOutputBoundary;
    }

    @Override
    public void addCalendarEvent(AddCalendarEventInputData addCalendarEventInputData) {
        outputDAO.addCalendarEvent(addCalendarEventInputData.getUserId(),
                addCalendarEventInputData.getTitle(),
                addCalendarEventInputData.getDescription(),
                addCalendarEventInputData.getActivityDate());

        String currentUserID = addCalendarEventInputData.getUserId();
        List<CalendarEvent> userEvents = outputDAO.getUserEvents(currentUserID);
        AddCalendarEventOutputData outputData = new AddCalendarEventOutputData(userEvents);

        this.outputData.prepareSuccessView(outputData);
    }
}
