package entity;

import java.time.LocalDate;

/**
 * Represents an activity scheduled for a GitBuff user.
 */
public class CalendarEvent {
    private final String eventId;
    private final String userId;
    private final String title;
    private final String description;
    private final LocalDate activityDate;

    /**
     * Creates a calendar event.
     *
     * @param eventId the Google Calendar event identifier
     * @param userId the GitBuff user identifier
     * @param title the event title
     * @param description the event description
     * @param activityDate the date on which the activity is scheduled
     */
    public CalendarEvent(
            final String eventId,
            final String userId,
            final String title,
            final String description,
            final LocalDate activityDate) {
        this.eventId = eventId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.activityDate = activityDate;
    }

    /**
     * Returns the Google Calendar event identifier.
     *
     * @return the event identifier
     */
    public String getEventId() {
        return eventId;
    }

    /**
     * Returns the identifier of the GitBuff user who owns this event.
     *
     * @return the user identifier
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Returns the event title.
     *
     * @return the event title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the event description.
     *
     * @return the event description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the date on which the activity is scheduled.
     *
     * @return the activity date
     */
    public LocalDate getActivityDate() {
        return activityDate;
    }
}
