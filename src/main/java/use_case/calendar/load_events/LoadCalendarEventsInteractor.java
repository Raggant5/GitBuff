package use_case.calendar.load_events;

import entity.CalendarEvent;
import use_case.calendar.CalendarEventData;
import use_case.calendar.CalendarEventDataAccessInterface;

import java.util.ArrayList;
import java.util.List;

public class LoadCalendarEventsInteractor implements LoadCalendarEventsInputBoundary {
    private final CalendarEventDataAccessInterface calendarDAO;
    private final LoadCalendarEventsOutputBoundary outputBoundary;

    public LoadCalendarEventsInteractor(CalendarEventDataAccessInterface calendarDAO, LoadCalendarEventsOutputBoundary outputBoundary) {
        this.calendarDAO = calendarDAO;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void loadCalendarEvents(LoadCalendarEventsInputData inputData) {
        try {
            List<CalendarEvent> calendarEvents = calendarDAO.getUserEvents(inputData.getUserID());
            List<CalendarEventData> calendarEventsData = new ArrayList<>();
            for (CalendarEvent event : calendarEvents) {
                calendarEventsData.add(CalendarEventData.from(event));
            }
            LoadCalendarEventsOutputData outputData = new LoadCalendarEventsOutputData(calendarEventsData);
            outputBoundary.prepareSuccessView(outputData);
        }
        catch (IllegalStateException exception) {
            outputBoundary.prepareFailureView(exception.getMessage());
        }
    }
}
