package use_case.calendar.sync_meals;

import java.util.List;

import use_case.calendar.CalendarEventData;

/**
 * Output Data for the Sync Meal Calendar Events Use Case.
 */
public class SyncMealCalendarEventsOutputData {

    private final List<CalendarEventData> calendarEvents;

    /**
     * Creates output data after synchronizing saved meals.
     *
     * @param calendarEvents the user's updated calendar events
     */
    public SyncMealCalendarEventsOutputData(final List<CalendarEventData> calendarEvents) {
        this.calendarEvents = List.copyOf(calendarEvents);
    }

    /**
     * Returns the user's updated calendar events.
     *
     * @return an immutable list of calendar event data
     */
    public List<CalendarEventData> getCalendarEvents() {
        return this.calendarEvents;
    }
}
