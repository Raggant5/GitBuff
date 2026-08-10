package use_case.calendar.sync_workouts;

import java.util.List;

import use_case.calendar.CalendarEventData;

/**
 * Output Data for the Sync Workout Calendar Events Use Case.
 */
public class SyncWorkoutCalendarEventsOutputData {

    private final List<CalendarEventData> calendarEvents;

    public SyncWorkoutCalendarEventsOutputData(final List<CalendarEventData> calendarEvents) {
        this.calendarEvents = calendarEvents;
    }

    public List<CalendarEventData> getCalendarEvents() {
        return this.calendarEvents;
    }
}
