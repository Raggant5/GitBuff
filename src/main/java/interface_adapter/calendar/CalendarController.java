package interface_adapter.calendar;

import use_case.calendar.load_events.LoadCalendarEventsInputBoundary;
import use_case.calendar.load_events.LoadCalendarEventsInputData;

/**
 * Controller for the calendar feature's one remaining view-triggered action: loading the
 * calendar for redisplay.
 */
public class CalendarController {

    private final LoadCalendarEventsInputBoundary loadInteractor;

    public CalendarController(LoadCalendarEventsInputBoundary loadInteractor) {
        this.loadInteractor = loadInteractor;
    }

    /**
     * Executes the Load Calendar Events From Data Use Case.
     *
     * @param userId the currently logged-in user's id
     */
    public void execute(final String userId) {
        if (userId != null && !userId.isBlank()) {
            final LoadCalendarEventsInputData inputData = new LoadCalendarEventsInputData(userId);
            loadInteractor.loadCalendarEvents(inputData);
        }
    }
}
