package interface_adapter.calendar;

import java.time.LocalDate;

/**
 * Display-only view of a calendar event, so state and view classes in this layer don't need
 * to depend on the {@code entity.CalendarEvent} entity directly.
 *
 * <p>Mirrors the same DTO convention already used elsewhere in the codebase (for example
 * {@code interface_adapter.log_workout.exercise.ExercisePerformedDisplayData} and
 * {@code interface_adapter.nutrition.food.FoodEntryDisplayData}): the presenter converts use
 * case output data into this type, and only this type crosses into the state/view classes.
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
