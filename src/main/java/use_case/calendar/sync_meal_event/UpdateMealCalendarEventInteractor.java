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

    public UpdateMealCalendarEventInteractor(final CalendarEventDataAccessInterface calendarDataAccessObject,
                                             final UpdateMealCalendarEventOutputBoundary presenter) {
        this.calendarDataAccessObject = calendarDataAccessObject;
        this.presenter = presenter;
    }

    @Override
    public void execute(final UpdateMealCalendarEventInputData inputData) {
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
