package use_case.calendar.sync_workouts;

/**
 * Output boundary for the Sync Workout Calendar Events Use Case.
 */
public interface SyncWorkoutCalendarEventsOutputBoundary {

    /**
     * Prepares the success view for the Sync Workout Calendar Events Use Case.
     *
     * @param outputData the user's calendar events after reconciliation
     */
    void prepareSuccessView(SyncWorkoutCalendarEventsOutputData outputData);

    /**
     * Prepares the failure view for the Sync Workout Calendar Events Use Case.
     *
     * @param errorMessage explanation of failure cause
     */
    void prepareFailureView(String errorMessage);
}
