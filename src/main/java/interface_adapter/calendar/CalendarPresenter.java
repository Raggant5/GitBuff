package interface_adapter.calendar;

import entity.CalendarEvent;
import use_case.calendar.add_event.AddCalendarEventOutputBoundary;
import use_case.calendar.add_event.AddCalendarEventOutputData;
import use_case.calendar.load_events.LoadCalendarEventsOutputData;
import use_case.calendar.remove_event.RemoveCalendarEventOutputBoundary;
import use_case.calendar.load_events.LoadCalendarEventsOutputBoundary;
import use_case.calendar.remove_event.RemoveCalendarEventOutputData;

import java.util.List;

public class CalendarPresenter implements AddCalendarEventOutputBoundary, RemoveCalendarEventOutputBoundary,
        LoadCalendarEventsOutputBoundary {
    private CalendarViewModel calendarViewModel;

    public CalendarPresenter(CalendarViewModel calendarViewModel) {
        this.calendarViewModel = calendarViewModel;
    }

    @Override
    public void prepareSuccessView(AddCalendarEventOutputData outputData) {
        this.updateCalendar(outputData.getCalendarEvents());
    }

    @Override
    public void prepareSuccessView(RemoveCalendarEventOutputData outputData) {
        this.updateCalendar(outputData.getCalendarEvents());
    }

    @Override
    public void prepareSuccessView(LoadCalendarEventsOutputData outputData) {
        this.updateCalendar(outputData.getCalendarEvents());
    }

    @Override
    public void prepareFailureView(String errorMessage) {
        CalendarState state = calendarViewModel.getState();
        state.setErrorMessage(errorMessage);
        calendarViewModel.firePropertyChanged();
    }

    private void updateCalendar(List<CalendarEvent> calendarEvents) {
        CalendarState state = calendarViewModel.getState();

        state.setCalendarEvents(calendarEvents);
        state.setErrorMessage(null);

        calendarViewModel.firePropertyChanged();
    }
}
