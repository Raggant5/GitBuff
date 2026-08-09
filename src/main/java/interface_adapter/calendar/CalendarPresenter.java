package interface_adapter.calendar;

import java.util.List;

import entity.CalendarEvent;
import use_case.calendar.add_event.AddCalendarEventOutputBoundary;
import use_case.calendar.add_event.AddCalendarEventOutputData;
import use_case.calendar.load_events.LoadCalendarEventsOutputBoundary;
import use_case.calendar.load_events.LoadCalendarEventsOutputData;
import use_case.calendar.remove_event.RemoveCalendarEventOutputBoundary;
import use_case.calendar.remove_event.RemoveCalendarEventOutputData;

/**
 * Presenter for the calendar add, remove, and load use cases.
 */
public class CalendarPresenter implements AddCalendarEventOutputBoundary, RemoveCalendarEventOutputBoundary,
        LoadCalendarEventsOutputBoundary {

    private final CalendarViewModel calendarViewModel;

    /**
     * Constructs a CalendarPresenter instance.
     *
     * @param calendarViewModel view model managing calendar display state.
     */
    public CalendarPresenter(final CalendarViewModel calendarViewModel) {
        this.calendarViewModel = calendarViewModel;
    }

    @Override
    public void prepareSuccessView(final AddCalendarEventOutputData outputData) {
        this.updateCalendar(outputData.getCalendarEvents());
    }

    @Override
    public void prepareSuccessView(final RemoveCalendarEventOutputData outputData) {
        this.updateCalendar(outputData.getCalendarEvents());
    }

    @Override
    public void prepareSuccessView(final LoadCalendarEventsOutputData outputData) {
        this.updateCalendar(outputData.getCalendarEvents());
    }

    @Override
    public void prepareFailureView(final String errorMessage) {
        final CalendarState state = this.calendarViewModel.getState();
        state.setErrorMessage(errorMessage);
        this.calendarViewModel.firePropertyChanged();
    }

    private void updateCalendar(final List<CalendarEvent> calendarEvents) {
        final CalendarState state = this.calendarViewModel.getState();

        state.setCalendarEvents(calendarEvents.stream()
                .map(this::toDisplayData)
                .toList());
        state.setErrorMessage(null);

        this.calendarViewModel.firePropertyChanged();
    }

    private CalendarEventDisplayData toDisplayData(final CalendarEvent event) {
        return new CalendarEventDisplayData(
                event.getEventId(), event.getUserId(), event.getTitle(),
                event.getDescription(), event.getActivityDate());
    }
}

