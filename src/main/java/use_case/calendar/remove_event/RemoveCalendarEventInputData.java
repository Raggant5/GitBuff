package use_case.calendar.remove_event;

/**
 * Input data required to remove an event from a user's calendar.
 */
public class RemoveCalendarEventInputData {
    private final String userId;
    private final String eventId;

    /**
     * Creates input data for the remove-calendar-event use case.
     *
     * @param userId the user who owns the calendar
     * @param eventId the identifier of the event to remove
     */
    public RemoveCalendarEventInputData(String userId, String eventId) {
        this.userId = userId;
        this.eventId = eventId;
    }

    /**
     * Returns the user identifier.
     *
     * @return the user identifier
     */
    public String getUserId() {
        return this.userId;
    }

    /**
     * Returns the event identifier.
     *
     * @return the event identifier
     */
    public String getEventId() {
        return this.eventId;
    }
}
