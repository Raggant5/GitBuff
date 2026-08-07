package use_case.calendar.remove_event;

import entity.CalendarEvent;
import use_case.calendar.CalendarEventDataAccessInterface;

import java.util.List;

public class RemoveCalendarEventInteractor implements RemoveCalendarEventInputBoundary {
    private CalendarEventDataAccessInterface outputDAO;
    private RemoveCalendarEventOutputBoundary outputBoundary;

    public RemoveCalendarEventInteractor(CalendarEventDataAccessInterface outputDAO, RemoveCalendarEventOutputBoundary outputBoundary) {
        this.outputDAO = outputDAO;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void removeCalendarEvent(RemoveCalendarEventInputData inputData) {
        try {
            outputDAO.removeCalendarEvent(inputData.getUserId(), inputData.getEventID());

            String userID = inputData.getUserId();
            List<CalendarEvent> calendarEvents = outputDAO.getUserEvents(userID);
            RemoveCalendarEventOutputData outputData = new RemoveCalendarEventOutputData(calendarEvents);
            outputBoundary.prepareSuccessView(outputData);
        }
        catch (IllegalStateException ex) {
            outputBoundary.prepareFailureView(ex.getMessage());
        }

    }
}
