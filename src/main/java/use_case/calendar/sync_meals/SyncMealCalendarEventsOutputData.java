package use_case.calendar.sync_meals;

import java.util.List;

import use_case.calendar.CalendarEventData;

/**
 * Output Data for the Sync Meal Calendar Events Use Case.
 */
public class SyncMealCalendarEventsOutputData {

    private final List<CalendarEventData> calendarEvents;

    public SyncMealCalendarEventsOutputData(final List<CalendarEventData> calendarEvents) {
        this.calendarEvents = calendarEvents;
    }

    public List<CalendarEventData> getCalendarEvents() {
        return this.calendarEvents;
    }
}
