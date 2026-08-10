package use_case.calendar.load_events;

import java.util.List;

import entity.CalendarEvent;
import use_case.calendar.CalendarEventDataAccessInterface;

/**
 * Coordinates loading and presenting a user's calendar events.
 */
public class LoadCalendarEventsInteractor implements LoadCalendarEventsInputBoundary {
    private final CalendarEventDataAccessInterface calendarDataAccessObject;
    private final LoadCalendarEventsOutputBoundary outputBoundary;

    /**
     * Creates an interactor for loading calendar events.
     *
     * @param calendarDataAccessObject the calendar event data access gateway
     * @param outputBoundary the presenter that receives the result
     */
    public LoadCalendarEventsInteractor(
            CalendarEventDataAccessInterface calendarDataAccessObject,
            LoadCalendarEventsOutputBoundary outputBoundary) {
        this.calendarDataAccessObject = calendarDataAccessObject;
        this.outputBoundary = outputBoundary;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadCalendarEvents(LoadCalendarEventsInputData inputData) {
        try {
            final List<CalendarEvent> calendarEvents =
                    this.calendarDataAccessObject.getUserEvents(inputData.getUserId());
            final LoadCalendarEventsOutputData outputData =
                    new LoadCalendarEventsOutputData(calendarEvents);
            this.outputBoundary.prepareSuccessView(outputData);
        }
        catch (IllegalStateException exception) {
            this.outputBoundary.prepareFailureView(exception.getMessage());
        }
    }
}
