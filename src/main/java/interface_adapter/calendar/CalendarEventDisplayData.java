package interface_adapter.calendar;

import java.time.LocalDate;

/**
 * Display-only view of a calendar event.
 */
public class CalendarEventDisplayData {

    private final String eventId;
    private final String userId;
    private final String title;
    private final String description;
    private final LocalDate activityDate;

    /**
     * Constructs a CalendarEventDisplayData instance.
     *
     * @param eventId the calendar provider's event id
     * @param userId the id of the user the event belongs to
     * @param title the event title
     * @param description the event description
     * @param activityDate the date the event occurs on
     */
    public CalendarEventDisplayData(final String eventId, final String userId, final String title,
                                    final String description, final LocalDate activityDate) {
        this.eventId = eventId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.activityDate = activityDate;
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
