package use_case.calendar.sync_workouts;

/**
 * Input boundary for reconciling the user's calendar so its workout events match their
 * generated workout plan - adding events for scheduled workouts missing from the calendar,
 * removing events for workouts that are no longer scheduled.
 */
public interface SyncWorkoutCalendarEventsInputBoundary {

    /**
     * Executes the sync-workout-calendar-events use case.
     *
     * @param inputData the user and freshly generated workout plans to reconcile against
     */
    void execute(SyncWorkoutCalendarEventsInputData inputData);
}
