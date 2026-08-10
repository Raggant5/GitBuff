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

    public String getEventId() {
        return this.eventId;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public LocalDate getActivityDate() {
        return this.activityDate;
    }
}
