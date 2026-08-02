package use_case.calendar;

import entity.CalendarEvent;

import java.util.List;

public class RemoveCalendarEventInteractor {
    private CalendarEventDataAccessInterface outputDAO;
    private RemoveCalendarEventOutputBoundary outputBoundary;

    public RemoveCalendarEventInteractor(CalendarEventDataAccessInterface outputDAO, RemoveCalendarEventOutputBoundary outputBoundary) {
        this.outputDAO = outputDAO;
        this.outputBoundary = outputBoundary;
    }

    public void removeCalendarEvent(RemoveCalendarEventInputData inputData) {
        outputDAO.removeCalendarEvent(inputData.getUserId(), inputData.getEventID());

        String userID = inputData.getUserId();
        List<CalendarEvent> calendarEvents = outputDAO.getUserEvents(userID);
        RemoveCalendarEventOutputData outputData = new RemoveCalendarEventOutputData(calendarEvents);
        outputBoundary.prepareSuccessView(outputData);
    }
}
