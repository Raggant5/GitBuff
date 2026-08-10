package use_case.calendar.load_events;

import java.util.ArrayList;
import java.util.List;

import entity.CalendarEvent;
import use_case.calendar.CalendarEventData;
import use_case.calendar.CalendarEventDataAccessInterface;

public class LoadCalendarEventsInteractor implements LoadCalendarEventsInputBoundary {
    private final CalendarEventDataAccessInterface calendarDataAccessObject;
    private final LoadCalendarEventsOutputBoundary outputBoundary;

    public LoadCalendarEventsInteractor(CalendarEventDataAccessInterface calendarDataAccessObject,
                                        LoadCalendarEventsOutputBoundary outputBoundary) {
        this.calendarDataAccessObject = calendarDataAccessObject;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void loadCalendarEvents(LoadCalendarEventsInputData inputData) {
        try {
            final List<CalendarEvent> calendarEvents = calendarDataAccessObject.getUserEvents(inputData.getUserID());
            final List<CalendarEventData> calendarEventsData = new ArrayList<>();
            for (CalendarEvent event : calendarEvents) {
                calendarEventsData.add(CalendarEventData.from(event));
            }
            final LoadCalendarEventsOutputData outputData = new LoadCalendarEventsOutputData(calendarEventsData);
            outputBoundary.prepareSuccessView(outputData);
        }
        catch (IllegalStateException exception) {
            outputBoundary.prepareFailureView(exception.getMessage());
        }
    }
}
