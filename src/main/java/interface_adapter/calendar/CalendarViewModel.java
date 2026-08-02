package interface_adapter.calendar;

import interface_adapter.ViewModel;
import use_case.calendar.add_event.AddCalendarEventOutputData;
import use_case.calendar.load_events.LoadCalendarEventsOutputData;
import use_case.calendar.remove_event.RemoveCalendarEventOutputData;

public class CalendarViewModel extends ViewModel<CalendarState> {
    public CalendarViewModel() {
        super("Calendar");
        setState(new CalendarState());
    }
}
