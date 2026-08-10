package interface_adapter.calendar;

import java.util.List;

import interface_adapter.nutrition.meal.MealDisplayData;
import interface_adapter.session.MealsSessionObserver;
import interface_adapter.session.UserLoggedInEvent;
import interface_adapter.session.UserSessionObserver;

/**
 * Loads the logged-in user's calendar events and backfills any meal events missing from the
 * calendar.
 *
 * <p>Extracted from {@code interface_adapter.login.LoginPresenter}. Reuses
 * {@link MealsSessionObserver#toDisplayData} rather than depending on
 * {@link MealsSessionObserver} having already run - observers should not depend on one
 * another's execution order.
 */
public class CalendarSyncObserver implements UserSessionObserver {

    private final CalendarController calendarController;

    /**
     * Constructs a CalendarSyncObserver instance.
     *
     * @param calendarController controller for loading and synchronizing calendar events
     */
    public CalendarSyncObserver(final CalendarController calendarController) {
        this.calendarController = calendarController;
    }

    @Override
    public void onUserLoggedIn(final UserLoggedInEvent event) {
        this.calendarController.loadCalendarEvents();

        final List<MealDisplayData> meals = MealsSessionObserver.toDisplayData(event.data().getMeals());
        this.calendarController.synchronizeMeals(meals);
    }
}
