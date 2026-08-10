package use_case.calendar.load_events;

import java.util.List;

import use_case.calendar.CalendarEventData;

public class LoadCalendarEventsOutputData {
    private final List<CalendarEventData> calendarEvents;

    public LoadCalendarEventsOutputData(List<CalendarEventData> calendarEvents) {
        this.calendarEvents = List.copyOf(calendarEvents);
    }

    public List<CalendarEventData> getCalendarEvents() {
        return this.calendarEvents;
    }
}
