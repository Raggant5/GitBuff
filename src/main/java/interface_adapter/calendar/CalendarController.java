package interface_adapter.calendar;

import interface_adapter.login.LoginViewModel;
import use_case.calendar.load_events.LoadCalendarEventsInputBoundary;
import use_case.calendar.load_events.LoadCalendarEventsInputData;

/**
 * Controller for the calendar feature's one remaining view-triggered action: loading the
 * calendar for redisplay.
 */
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
        final String userId = getCurrentUserId();
        if (userId == null) {
            return;
        }

        final LoadCalendarEventsInputData inputData = new LoadCalendarEventsInputData(userId);
        loadInteractor.loadCalendarEvents(inputData);
    }

    private String getCurrentUserId() {
        final String userId = loginViewModel.getState().getUsername();
        if (userId == null || userId.isBlank()) {
            return null;
        }
        return userId;
    }
}
