package interface_adapter.calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import use_case.calendar.CalendarEventData;
import use_case.calendar.load_events.LoadCalendarEventsOutputData;
import use_case.calendar.sync_meal_event.UpdateMealCalendarEventOutputData;
import use_case.calendar.sync_meals.SyncMealCalendarEventsOutputData;
import use_case.calendar.sync_workouts.SyncWorkoutCalendarEventsOutputData;

class CalendarPresenterTest {

    private static List<CalendarEventData> oneEvent() {
        return List.of(new CalendarEventData(
                "event-1", "amir", "Workout: Leg Day", "GitBuff workout schedule", LocalDate.of(2026, 8, 10)));
    }

    @Test
    void prepareSuccessViewFromLoadCalendarEventsUpdatesState() {
        final CalendarViewModel viewModel = new CalendarViewModel();
        final CalendarPresenter presenter = new CalendarPresenter(viewModel);

        presenter.prepareSuccessView(new LoadCalendarEventsOutputData(oneEvent()));

        assertEquals(1, viewModel.getState().getCalendarEvents().size());
        assertEquals("Workout: Leg Day", viewModel.getState().getCalendarEvents().get(0).getTitle());
        assertNull(viewModel.getState().getErrorMessage());
    }

    @Test
    void prepareSuccessViewFromSyncMealCalendarEventsUpdatesState() {
        final CalendarViewModel viewModel = new CalendarViewModel();
        final CalendarPresenter presenter = new CalendarPresenter(viewModel);

        presenter.prepareSuccessView(new SyncMealCalendarEventsOutputData(oneEvent()));

        assertEquals(1, viewModel.getState().getCalendarEvents().size());
    }

    @Test
    void prepareSuccessViewFromSyncWorkoutCalendarEventsUpdatesState() {
        final CalendarViewModel viewModel = new CalendarViewModel();
        final CalendarPresenter presenter = new CalendarPresenter(viewModel);

        presenter.prepareSuccessView(new SyncWorkoutCalendarEventsOutputData(oneEvent()));

        assertEquals(1, viewModel.getState().getCalendarEvents().size());
    }

    @Test
    void prepareSuccessViewFromUpdateMealCalendarEventUpdatesState() {
        final CalendarViewModel viewModel = new CalendarViewModel();
        final CalendarPresenter presenter = new CalendarPresenter(viewModel);

        presenter.prepareSuccessView(new UpdateMealCalendarEventOutputData(oneEvent()));

        assertEquals(1, viewModel.getState().getCalendarEvents().size());
    }

    @Test
    void prepareFailureViewSetsErrorMessage() {
        final CalendarViewModel viewModel = new CalendarViewModel();
        final CalendarPresenter presenter = new CalendarPresenter(viewModel);

        presenter.prepareFailureView("Calendar unavailable");

        assertEquals("Calendar unavailable", viewModel.getState().getErrorMessage());
    }
}
