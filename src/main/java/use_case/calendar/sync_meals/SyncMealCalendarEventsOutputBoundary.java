package use_case.calendar.sync_meals;

/**
 * Output boundary for the Sync Meal Calendar Events Use Case.
 */
public interface SyncMealCalendarEventsOutputBoundary {

    /**
     * Prepares the success view for the Sync Meal Calendar Events Use Case.
     *
     * @param outputData the user's calendar events after reconciliation
     */
    void prepareSuccessView(SyncMealCalendarEventsOutputData outputData);

    /**
     * Prepares the failure view for the Sync Meal Calendar Events Use Case.
     *
     * @param errorMessage explanation of failure cause
     */
    void prepareFailureView(String errorMessage);
}
