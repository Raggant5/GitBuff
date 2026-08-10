package use_case.calendar.remove_event;

import java.util.List;

import entity.CalendarEvent;
import use_case.calendar.CalendarEventDataAccessInterface;

/**
 * Coordinates removing an event and refreshing the user's calendar events.
 */
public class RemoveCalendarEventInteractor implements RemoveCalendarEventInputBoundary {
    private final CalendarEventDataAccessInterface calendarDataAccessObject;
    private final RemoveCalendarEventOutputBoundary outputBoundary;

    /**
     * Creates an interactor for removing calendar events.
     *
     * @param calendarDataAccessObject the calendar event data access gateway
     * @param outputBoundary the presenter that receives the result
     */
    public RemoveCalendarEventInteractor(
            CalendarEventDataAccessInterface calendarDataAccessObject,
            RemoveCalendarEventOutputBoundary outputBoundary) {
        this.calendarDataAccessObject = calendarDataAccessObject;
        this.outputBoundary = outputBoundary;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeCalendarEvent(RemoveCalendarEventInputData inputData) {
        try {
            this.calendarDataAccessObject.removeCalendarEvent(
                    inputData.getUserId(), inputData.getEventId());

            final String userId = inputData.getUserId();
            final List<CalendarEvent> calendarEvents =
                    this.calendarDataAccessObject.getUserEvents(userId);
            final RemoveCalendarEventOutputData outputData =
                    new RemoveCalendarEventOutputData(calendarEvents);
            this.outputBoundary.prepareSuccessView(outputData);
        }
        catch (IllegalStateException exception) {
            this.outputBoundary.prepareFailureView(exception.getMessage());
        }
    }
}
