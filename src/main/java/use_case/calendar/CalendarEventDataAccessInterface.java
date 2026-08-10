package use_case.calendar;

import java.time.LocalDate;
import java.util.List;

import entity.CalendarEvent;

/**
 * Gateway used by calendar use cases to store and retrieve calendar events.
 */
public interface CalendarEventDataAccessInterface {

    /**
     * Add CalendarEvent Use Case.
     * @param userId id of user to add event for
     * @param title the name of the event
     * @param description description of event
     * @param activityDate date of event
     */
    void addCalendarEvent(
            String userId,
            String title,
            String description,
            LocalDate activityDate);

    /**
     * Delete Event From Calendar Use Case.
     * @param userId id of user for event deletion
     * @param eventId id of event to delete
     */
    void removeCalendarEvent(String userId, String eventId);

    /**
     * Get User Events From Calendar Use Case.
     * @param userID id of the user to get events for
     * @return a list of CalendarEvents
     */
    List<CalendarEvent> getUserEvents(String userID);
}
