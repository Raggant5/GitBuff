package use_case.calendar.sync_workouts;

import java.util.List;

import use_case.calendar.CalendarEventData;

/**
 * Output Data for the Sync Workout Calendar Events Use Case.
 */
public class SyncWorkoutCalendarEventsOutputData {

    private final List<CalendarEventData> calendarEvents;

    /**
     * Creates output data after synchronizing generated workout plans.
     *
     * @param calendarEvents the user's updated calendar events
     */
    public SyncWorkoutCalendarEventsOutputData(final List<CalendarEventData> calendarEvents) {
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
