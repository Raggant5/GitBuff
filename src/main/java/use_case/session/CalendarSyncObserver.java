package use_case.session;

import use_case.calendar.sync_meals.SyncMealCalendarEventsInputBoundary;
import use_case.calendar.sync_meals.SyncMealCalendarEventsInputData;

/**
 * Reconciles the logged-in user's calendar meal events right after login.
 */
public class CalendarSyncObserver implements UserSessionObserver {

    private final SyncMealCalendarEventsInputBoundary syncMealCalendarEventsInteractor;

    public CalendarSyncObserver(final SyncMealCalendarEventsInputBoundary syncMealCalendarEventsInteractor) {
        this.syncMealCalendarEventsInteractor = syncMealCalendarEventsInteractor;
    }

    @Override
    public void onUserLoggedIn(final UserLoggedInEvent event) {
        this.syncMealCalendarEventsInteractor.execute(
                new SyncMealCalendarEventsInputData(event.getData().getUsername()));
    }
}
