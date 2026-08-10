package use_case.calendar.sync_meals;

import java.util.ArrayList;
import java.util.List;

import entity.CalendarEvent;
import entity.Meal;
import use_case.calendar.CalendarEventData;
import use_case.calendar.CalendarEventDataAccessInterface;
import use_case.nutrition.meal.get_meals.ViewMealDataAccessInterface;

/**
 * Reconciles the user's calendar so its meal events match their saved meals.
 */
public class SyncMealCalendarEventsInteractor implements SyncMealCalendarEventsInputBoundary {

    private static final String MEAL_REFERENCE_PREFIX = "GitBuff meal ID: ";

    private final CalendarEventDataAccessInterface calendarDataAccessObject;
    private final ViewMealDataAccessInterface mealDataAccessObject;
    private final SyncMealCalendarEventsOutputBoundary presenter;

    /**
     * Creates an interactor that reconciles saved meals with calendar events.
     *
     * @param calendarDataAccessObject gateway for calendar operations
     * @param mealDataAccessObject gateway for saved meal data
     * @param presenter output boundary receiving the synchronized calendar
     */
    public SyncMealCalendarEventsInteractor(final CalendarEventDataAccessInterface calendarDataAccessObject,
                                            final ViewMealDataAccessInterface mealDataAccessObject,
                                            final SyncMealCalendarEventsOutputBoundary presenter) {
        this.calendarDataAccessObject = calendarDataAccessObject;
        this.mealDataAccessObject = mealDataAccessObject;
        this.presenter = presenter;
    }

    /**
     * Synchronizes all dated meals belonging to the requested user.
     *
     * @param inputData identifies the user whose meals should be synchronized
     */
    @Override
    public void execute(final SyncMealCalendarEventsInputData inputData) {
        try {
            synchronizeMeals(inputData);
        }
        catch (final IllegalStateException exception) {
            this.presenter.prepareFailureView(exception.getMessage());
        }
    }

    /**
     * Performs the reconciliation of saved meals and calendar events.
     *
     * @param inputData identifies the user whose meals should be synchronized
     */
    private void synchronizeMeals(final SyncMealCalendarEventsInputData inputData) {
        final String userId = inputData.getUserId();

        final List<Meal> desiredMeals = new ArrayList<>();
        for (final Meal meal : this.mealDataAccessObject.getMealsForUser(userId)) {
            if (meal.getDate() != null) {
                desiredMeals.add(meal);
            }
        }

        final List<CalendarEvent> currentEvents = this.calendarDataAccessObject.getUserEvents(userId);
        for (final CalendarEvent event : currentEvents) {
            if (event.getDescription() != null && event.getDescription().startsWith(MEAL_REFERENCE_PREFIX)) {
                final Meal matchingMeal = findMatchingMeal(desiredMeals, event);
                if (matchingMeal == null) {
                    this.calendarDataAccessObject.removeCalendarEvent(userId, event.getEventId());
                }
                else {
                    desiredMeals.remove(matchingMeal);
                }
            }
        }

        for (final Meal meal : desiredMeals) {
            this.calendarDataAccessObject.addCalendarEvent(
                    userId, "Meal: " + meal.getName(), MEAL_REFERENCE_PREFIX + meal.getId(), meal.getDate());
        }

        final List<CalendarEvent> finalEvents = this.calendarDataAccessObject.getUserEvents(userId);
        final List<CalendarEventData> finalEventsData = new ArrayList<>();
        for (final CalendarEvent event : finalEvents) {
            finalEventsData.add(CalendarEventData.from(event));
        }
        this.presenter.prepareSuccessView(new SyncMealCalendarEventsOutputData(finalEventsData));
    }

    /**
     * Finds a saved meal that exactly matches an existing calendar event.
     *
     * @param meals candidate saved meals
     * @param event calendar event being reconciled
     * @return the matching meal, or {@code null} when none exists
     */
    private Meal findMatchingMeal(final List<Meal> meals, final CalendarEvent event) {
        Meal result = null;
        boolean found = false;
        for (final Meal meal : meals) {
            if (!found && ("Meal: " + meal.getName()).equals(event.getTitle())
                    && (MEAL_REFERENCE_PREFIX + meal.getId()).equals(event.getDescription())
                    && meal.getDate().equals(event.getActivityDate())) {
                result = meal;
                found = true;
            }
        }
        return result;
    }
}
