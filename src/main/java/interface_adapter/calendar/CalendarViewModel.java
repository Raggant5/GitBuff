package interface_adapter.calendar;

import interface_adapter.ViewModel;

/**
 * The View Model for the Calendar View.
 */
public class CalendarViewModel extends ViewModel<CalendarState> {

    /**
     * Constructs a CalendarViewModel instance.
     */
    public CalendarViewModel() {
        super("Calendar");
        setState(new CalendarState());
    }
}
