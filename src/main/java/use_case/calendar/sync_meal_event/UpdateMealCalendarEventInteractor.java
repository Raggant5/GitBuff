package use_case.calendar.sync_meal_event;

import java.util.ArrayList;
import java.util.List;

import entity.CalendarEvent;
import use_case.calendar.CalendarEventData;
import use_case.calendar.CalendarEventDataAccessInterface;
import use_case.nutrition.meal.MealChangeType;

/**
 * Adds, replaces, or removes a single meal's calendar event, mirroring what happened to that
 * meal.
 */
public class UpdateMealCalendarEventInteractor implements UpdateMealCalendarEventInputBoundary {

    private static final String MEAL_REFERENCE_PREFIX = "GitBuff meal ID: ";

    private final CalendarEventDataAccessInterface calendarDataAccessObject;
    private final UpdateMealCalendarEventOutputBoundary presenter;

    /**
     * Creates an interactor for synchronizing one changed meal.
     *
     * @param calendarDataAccessObject gateway for calendar operations
     * @param presenter output boundary receiving the updated calendar
     */
    public UpdateMealCalendarEventInteractor(final CalendarEventDataAccessInterface calendarDataAccessObject,
                                             final UpdateMealCalendarEventOutputBoundary presenter) {
        this.calendarDataAccessObject = calendarDataAccessObject;
        this.presenter = presenter;
    }

    /**
     * Adds, replaces, or removes the calendar event associated with the changed meal.
     *
     * @param inputData information describing the meal change
     */
    @Override
    public void execute(final UpdateMealCalendarEventInputData inputData) {
        try {
            synchronizeMeal(inputData);
        }
        catch (final IllegalStateException exception) {
            this.presenter.prepareFailureView(exception.getMessage());
        }
    }

    /**
     * Performs the calendar operations required by one meal change.
     *
     * @param inputData information describing the meal change
     */
    private void synchronizeMeal(final UpdateMealCalendarEventInputData inputData) {
        final String userId = inputData.getUserId();
        final String description = MEAL_REFERENCE_PREFIX + inputData.getMealId();

        final List<CalendarEvent> currentEvents = this.calendarDataAccessObject.getUserEvents(userId);
        final CalendarEvent existing = findEventWithDescription(currentEvents, description);

        if (inputData.getChangeType() != MealChangeType.ADDED && existing != null) {
            this.calendarDataAccessObject.removeCalendarEvent(userId, existing.getEventId());
        }
        if (inputData.getChangeType() != MealChangeType.DELETED && inputData.getDate() != null) {
            this.calendarDataAccessObject.addCalendarEvent(
                    userId, "Meal: " + inputData.getName(), description, inputData.getDate());
        }

        final List<CalendarEvent> finalEvents = this.calendarDataAccessObject.getUserEvents(userId);
        final List<CalendarEventData> finalEventsData = new ArrayList<>();
        for (final CalendarEvent event : finalEvents) {
            finalEventsData.add(CalendarEventData.from(event));
        }
        this.presenter.prepareSuccessView(new UpdateMealCalendarEventOutputData(finalEventsData));
    }

    /**
     * Finds the event associated with a stored meal reference.
     *
     * @param events the calendar events to search
     * @param description the stable meal-reference description
     * @return the matching event, or {@code null} when none exists
     */
    private CalendarEvent findEventWithDescription(final List<CalendarEvent> events, final String description) {
        CalendarEvent result = null;
        boolean found = false;
        for (final CalendarEvent event : events) {
            if (!found && description.equals(event.getDescription())) {
                result = event;
                found = true;
            }
        }
        return result;
    }
}
