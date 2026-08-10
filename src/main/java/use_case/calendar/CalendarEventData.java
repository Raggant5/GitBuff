package use_case.calendar;

import java.time.LocalDate;

import entity.CalendarEvent;

/**
 * Use case boundary DTO mirroring entity.CalendarEvent.
 */
public class CalendarEventData {

    private final String eventId;
    private final String userId;
    private final String title;
    private final String description;
    private final LocalDate activityDate;

    /**
     * Creates calendar event data for transfer across the use-case boundary.
     *
     * @param eventId the Google Calendar event identifier
     * @param userId the GitBuff user identifier
     * @param title the event title
     * @param description the event description
     * @param activityDate the scheduled date
     */
    public CalendarEventData(final String eventId, final String userId, final String title,
                             final String description, final LocalDate activityDate) {
        this.eventId = eventId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.activityDate = activityDate;
    }

    /**
     * Converts a {@code entity.CalendarEvent} into its boundary DTO form.
     *
     * @param event the entity to convert
     * @return the equivalent DTO
     */
    public static CalendarEventData from(final CalendarEvent event) {
        return new CalendarEventData(
                event.getEventId(), event.getUserId(), event.getTitle(),
                event.getDescription(), event.getActivityDate());
    }

    /**
     * Returns the Google Calendar event identifier.
     *
     * @return the event identifier
     */
    public String getEventId() {
        return this.eventId;
    }

    /**
     * Returns the GitBuff user identifier.
     *
     * @return the user identifier
     */
    public String getUserId() {
        return this.userId;
    }

    /**
     * Returns the event title.
     *
     * @return the event title
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Returns the event description.
     *
     * @return the event description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Returns the scheduled date.
     *
     * @return the activity date
     */
    public LocalDate getActivityDate() {
        return this.activityDate;
    }
}
