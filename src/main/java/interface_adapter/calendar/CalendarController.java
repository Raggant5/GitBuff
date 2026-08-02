package interface_adapter.calendar;

import entity.CalendarEvent;
import interface_adapter.login.LoginViewModel;
import use_case.calendar.load_events.LoadCalendarEventsInputBoundary;
import use_case.calendar.load_events.LoadCalendarEventsInputData;

import java.util.List;

public class CalendarController {
    private final LoadCalendarEventsInputBoundary loadInteractor;
    private final LoginViewModel loginViewModel;

    public CalendarController(
            LoadCalendarEventsInputBoundary loadInteractor,
            LoginViewModel loginViewModel) {
        this.loadInteractor = loadInteractor;
        this.loginViewModel = loginViewModel;
    }

    public void loadCalendarEvents() {
        String userID = loginViewModel.getState().getUsername();

        LoadCalendarEventsInputData inputData = new LoadCalendarEventsInputData(userID);
        loadInteractor.loadCalendarEvents(inputData);
    }
}
