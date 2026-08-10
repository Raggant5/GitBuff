package use_case.calendar.sync_meals;

/**
 * Input Data for the Sync Meal Calendar Events Use Case.
 */
public class SyncMealCalendarEventsInputData {

    private final String userId;

    public SyncMealCalendarEventsInputData(final String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return this.userId;
    }
}
