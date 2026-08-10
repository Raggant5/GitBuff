package use_case.calendar.load_events;

/**
 * Input data required to load a user's calendar events.
 */
public class LoadCalendarEventsInputData {
    private final String userId;

    /**
     * Creates input data for the load-calendar-events use case.
     *
     * @param userId the user whose events are requested
     */
    public LoadCalendarEventsInputData(String userId) {
        this.userId = userId;
    }

    /**
     * Returns the user identifier.
     *
     * @return the user identifier
     */
    public String getUserId() {
        return this.userId;
    }
}
