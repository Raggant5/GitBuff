package use_case.calendar;

import entity.CalendarEvent;

import java.time.LocalDate;
import java.util.List;

public interface CalendarEventDataAccessInterface {
    void addCalendarEvent(
            String userId,
            String title,
            String description,
            LocalDate activityDate);

    void removeCalendarEvent(String userId, String eventId);

    List<CalendarEvent> getUserEvents(String userID);
}
