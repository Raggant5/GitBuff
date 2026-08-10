package use_case.calendar;

import java.time.LocalDate;
import java.util.List;

import entity.CalendarEvent;

/**
 * Provides persistence operations required by the calendar use cases.
 */
public interface CalendarEventDataAccessInterface {
    /**
     * Adds an all-day event to the calendar belonging to a user.
     *
     * @param userId the user whose calendar receives the event
     * @param title the event title
     * @param description the event description
     * @param activityDate the date on which the activity is scheduled
     */
    void addCalendarEvent(
            String userId,
            String title,
            String description,
            LocalDate activityDate);

    /**
     * Removes an event from the calendar belonging to a user.
     *
     * @param userId the user who owns the calendar
     * @param eventId the identifier of the event to remove
     */
    void removeCalendarEvent(String userId, String eventId);

    /**
     * Returns all calendar events belonging to a user.
     *
     * @param userId the user whose events are requested
     * @return the user's calendar events
     */
    List<CalendarEvent> getUserEvents(String userId);
}
