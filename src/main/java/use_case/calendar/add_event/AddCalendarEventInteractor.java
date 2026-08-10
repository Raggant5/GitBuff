package use_case.calendar.add_event;

import java.util.List;

import entity.CalendarEvent;
import use_case.calendar.CalendarEventDataAccessInterface;

/**
 * Coordinates adding an event and refreshing the user's calendar events.
 */
public class AddCalendarEventInteractor implements AddCalendarEventInputBoundary {
    private final CalendarEventDataAccessInterface calendarDataAccessObject;
    private final AddCalendarEventOutputBoundary outputBoundary;

    /**
     * Creates an interactor for adding calendar events.
     *
     * @param calendarDataAccessObject the calendar event data access gateway
     * @param outputBoundary the presenter that receives the result
     */
    public AddCalendarEventInteractor(
            CalendarEventDataAccessInterface calendarDataAccessObject,
            AddCalendarEventOutputBoundary outputBoundary) {
        this.calendarDataAccessObject = calendarDataAccessObject;
        this.outputBoundary = outputBoundary;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addCalendarEvent(AddCalendarEventInputData inputData) {
        try {
            this.calendarDataAccessObject.addCalendarEvent(
                    inputData.getUserId(),
                    inputData.getTitle(),
                    inputData.getDescription(),
                    inputData.getActivityDate());

            final List<CalendarEvent> userEvents =
                    this.calendarDataAccessObject.getUserEvents(inputData.getUserId());

            final AddCalendarEventOutputData outputData =
                    new AddCalendarEventOutputData(userEvents);

            this.outputBoundary.prepareSuccessView(outputData);
        }
        catch (IllegalStateException exception) {
            this.outputBoundary.prepareFailureView(exception.getMessage());
        }
    }
}
