package use_case.calendar.sync_meal_event;

import java.util.List;

import use_case.calendar.CalendarEventData;

/**
 * Output Data for the Update Meal Calendar Event Use Case.
 */
public class UpdateMealCalendarEventOutputData {

    private final List<CalendarEventData> calendarEvents;

    public UpdateMealCalendarEventOutputData(final List<CalendarEventData> calendarEvents) {
        this.calendarEvents = calendarEvents;
    }

    public List<CalendarEventData> getCalendarEvents() {
        return this.calendarEvents;
    }
}
