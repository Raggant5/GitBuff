package interface_adapter.calendar;

import interface_adapter.ViewModel;

public class CalendarViewModel extends ViewModel<CalendarState> {
    public CalendarViewModel() {
        super("Calendar");
        setState(new CalendarState());
    }
}
