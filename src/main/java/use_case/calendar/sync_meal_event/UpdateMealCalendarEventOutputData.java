package use_case.calendar.sync_meal_event;

import java.util.List;

import use_case.calendar.CalendarEventData;

/**
 * Output Data for the Update Meal Calendar Event Use Case.
 */
public class UpdateMealCalendarEventOutputData {

    private final List<CalendarEventData> calendarEvents;

    /**
     * Creates output data after synchronizing a changed meal.
     *
     * @param calendarEvents the user's updated calendar events
     */
    public UpdateMealCalendarEventOutputData(final List<CalendarEventData> calendarEvents) {
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
