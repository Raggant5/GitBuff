package use_case.calendar.load_events;

import java.util.ArrayList;
import java.util.List;

import entity.CalendarEvent;
import use_case.calendar.CalendarEventData;
import use_case.calendar.CalendarEventDataAccessInterface;

/**
 * Loads a user's calendar events and sends the result to the output boundary.
 */
public class LoadCalendarEventsInteractor implements LoadCalendarEventsInputBoundary {
    private final CalendarEventDataAccessInterface calendarDataAccessObject;
    private final LoadCalendarEventsOutputBoundary outputBoundary;

    /**
     * Creates an interactor for loading calendar events.
     *
     * @param calendarDataAccessObject gateway for retrieving calendar events
     * @param outputBoundary presenter receiving the result
     */
    public LoadCalendarEventsInteractor(final CalendarEventDataAccessInterface calendarDataAccessObject,
                                        final LoadCalendarEventsOutputBoundary outputBoundary) {
        this.calendarDataAccessObject = calendarDataAccessObject;
        this.outputBoundary = outputBoundary;
    }

    /**
     * Loads the requested user's calendar events.
     *
     * @param inputData the user whose events should be loaded
     */
    @Override
    public void loadCalendarEvents(final LoadCalendarEventsInputData inputData) {
        try {
            final List<CalendarEvent> calendarEvents = calendarDataAccessObject.getUserEvents(inputData.getUserID());
            final List<CalendarEventData> calendarEventsData = new ArrayList<>();
            for (final CalendarEvent event : calendarEvents) {
                calendarEventsData.add(CalendarEventData.from(event));
            }
            final LoadCalendarEventsOutputData outputData = new LoadCalendarEventsOutputData(calendarEventsData);
            outputBoundary.prepareSuccessView(outputData);
        }
        catch (final IllegalStateException exception) {
            outputBoundary.prepareFailureView(exception.getMessage());
        }
    }
}
