package interface_adapter.calendar;

import java.util.ArrayList;
import java.util.List;

import use_case.calendar.CalendarEventData;
import use_case.calendar.load_events.LoadCalendarEventsOutputBoundary;
import use_case.calendar.load_events.LoadCalendarEventsOutputData;
import use_case.calendar.sync_meal_event.UpdateMealCalendarEventOutputBoundary;
import use_case.calendar.sync_meal_event.UpdateMealCalendarEventOutputData;
import use_case.calendar.sync_meals.SyncMealCalendarEventsOutputBoundary;
import use_case.calendar.sync_meals.SyncMealCalendarEventsOutputData;
import use_case.calendar.sync_workouts.SyncWorkoutCalendarEventsOutputBoundary;
import use_case.calendar.sync_workouts.SyncWorkoutCalendarEventsOutputData;

/**
 * Presenter for the calendar add, remove, load, and sync use cases.
 */
public class CalendarPresenter implements LoadCalendarEventsOutputBoundary, SyncMealCalendarEventsOutputBoundary,
        SyncWorkoutCalendarEventsOutputBoundary, UpdateMealCalendarEventOutputBoundary {

    private final CalendarViewModel calendarViewModel;

    public CalendarPresenter(final CalendarViewModel calendarViewModel) {
        this.calendarViewModel = calendarViewModel;
    }

    @Override
    public void prepareSuccessView(final LoadCalendarEventsOutputData outputData) {
        this.updateCalendar(outputData.getCalendarEvents());
    }

    @Override
    public void prepareSuccessView(final SyncMealCalendarEventsOutputData outputData) {
        this.updateCalendar(outputData.getCalendarEvents());
    }

    @Override
    public void prepareSuccessView(final SyncWorkoutCalendarEventsOutputData outputData) {
        this.updateCalendar(outputData.getCalendarEvents());
    }

    @Override
    public void prepareSuccessView(final UpdateMealCalendarEventOutputData outputData) {
        this.updateCalendar(outputData.getCalendarEvents());
    }

    @Override
    public void prepareFailureView(final String errorMessage) {
        final CalendarState state = calendarViewModel.getState();
        state.setErrorMessage(errorMessage);
        calendarViewModel.firePropertyChanged();
    }

    private void updateCalendar(final List<CalendarEventData> calendarEvents) {
        final CalendarState state = calendarViewModel.getState();

        final List<CalendarEventDisplayData> displayEvents = new ArrayList<>();
        for (final CalendarEventData event : calendarEvents) {
            displayEvents.add(toDisplayData(event));
        }
        state.setCalendarEvents(displayEvents);
        state.setErrorMessage(null);

        calendarViewModel.firePropertyChanged();
    }

    private CalendarEventDisplayData toDisplayData(final CalendarEventData event) {
        return new CalendarEventDisplayData(
                event.getEventId(), event.getUserId(), event.getTitle(),
                event.getDescription(), event.getActivityDate());
    }
}



