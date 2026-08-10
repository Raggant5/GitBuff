package use_case.calendar.add_event;

import java.time.LocalDate;

/**
 * Input data required to add an event to a user's calendar.
 */
public class AddCalendarEventInputData {
    private final String userId;
    private final String title;
    private final String description;
    private final LocalDate activityDate;

    /**
     * Creates input data for the add-calendar-event use case.
     *
     * @param userId the user whose calendar receives the event
     * @param title the event title
     * @param description the event description
     * @param activityDate the date on which the activity is scheduled
     */
    public AddCalendarEventInputData(
            String userId,
            String title,
            String description,
            LocalDate activityDate) {
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.activityDate = activityDate;
    }

    /**
     * Returns the user identifier.
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
     * Returns the activity date.
     *
     * @return the activity date
     */
    public LocalDate getActivityDate() {
        return activityDate;
    }
}
