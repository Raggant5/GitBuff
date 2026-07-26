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

    public CalendarEvent(
            String eventId,
            String userId,
            String title,
            String description,
            LocalDate activityDate) {
        this.eventId = eventId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.activityDate = activityDate;
    }

    public String getEventId() {
        return eventId;
    }

    public String getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getActivityDate() {
        return activityDate;
    }
}
