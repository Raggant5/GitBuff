package use_case.calendar.sync_meals;

/**
 * Input boundary for reconciling the user's calendar so its meal events match their saved
 * meals - adding events for meals missing from the calendar, removing events for meals that no
 * longer exist.
 */
public interface SyncMealCalendarEventsInputBoundary {

    /**
     * Executes the sync-meal-calendar-events use case.
     *
     * @param inputData the user to reconcile meal calendar events for
     */
    void execute(SyncMealCalendarEventsInputData inputData);
}
