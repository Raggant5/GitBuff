package use_case.calendar.load_events;

/**
 * Input data identifying the user whose calendar events should be loaded.
 */
public class LoadCalendarEventsInputData {
    private final String userID;

    /**
     * Creates input data for the load-calendar-events use case.
     *
     * @param userID the GitBuff user identifier
     */
    public LoadCalendarEventsInputData(final String userID) {
        this.userID = userID;
    }

    /**
     * Returns the GitBuff user identifier.
     *
     * @return the user identifier
     */
    public String getUserID() {
        return this.userID;
    }
}
