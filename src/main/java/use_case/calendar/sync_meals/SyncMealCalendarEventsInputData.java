package use_case.calendar.sync_meals;

/**
 * Input Data for the Sync Meal Calendar Events Use Case.
 */
public class SyncMealCalendarEventsInputData {

    private final String userId;

    /**
     * Creates input data for synchronizing a user's saved meals.
     *
     * @param userId the GitBuff user identifier
     */
    public SyncMealCalendarEventsInputData(final String userId) {
        this.userId = userId;
    }

    /**
     * Returns the GitBuff user identifier.
     *
     * @return the user identifier
     */
    public String getUserId() {
        return this.userId;
    }
}
