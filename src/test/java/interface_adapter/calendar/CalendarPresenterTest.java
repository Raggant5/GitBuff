package interface_adapter.calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import entity.CalendarEvent;
import use_case.calendar.add_event.AddCalendarEventOutputData;
import use_case.calendar.load_events.LoadCalendarEventsOutputData;
import use_case.calendar.remove_event.RemoveCalendarEventOutputData;

class CalendarPresenterTest {

    private CalendarViewModel viewModel;
    private CalendarPresenter presenter;
    private AtomicInteger changeCount;

    @BeforeEach
    void setUp() {
        this.viewModel = new CalendarViewModel();
        this.presenter = new CalendarPresenter(this.viewModel);
        this.changeCount = new AtomicInteger();
        this.viewModel.addPropertyChangeListener(event -> this.changeCount.incrementAndGet());
    }

    @Test
    void addSuccessConvertsEntitiesAndClearsError() {
        this.viewModel.getState().setErrorMessage("old error");
        final CalendarEvent event = event("add-1", "Meal", LocalDate.of(2026, 8, 9));

        this.presenter.prepareSuccessView(new AddCalendarEventOutputData(List.of(event)));

        assertCalendarEventWasPresented(event);
        assertNull(this.viewModel.getState().getErrorMessage());
        assertEquals(1, this.changeCount.get());
    }

    @Test
    void removeSuccessConvertsEntities() {
        final CalendarEvent event = event("remove-1", "Workout", LocalDate.of(2026, 8, 10));

        this.presenter.prepareSuccessView(new RemoveCalendarEventOutputData(List.of(event)));

        assertCalendarEventWasPresented(event);
        assertEquals(1, this.changeCount.get());
    }

    @Test
    void loadSuccessConvertsEntities() {
        final CalendarEvent event = event("load-1", "Breakfast", LocalDate.of(2026, 8, 11));

        this.presenter.prepareSuccessView(new LoadCalendarEventsOutputData(List.of(event)));

        assertCalendarEventWasPresented(event);
        assertEquals(1, this.changeCount.get());
    }

    @Test
    void failureSetsErrorAndNotifiesView() {
        this.presenter.prepareFailureView("Calendar unavailable");

        assertEquals("Calendar unavailable", this.viewModel.getState().getErrorMessage());
        assertEquals(1, this.changeCount.get());
    }

    @Test
    void stateCopiesEventListsAndViewModelHasCalendarName() {
        final List<CalendarEventDisplayData> mutableEvents = new ArrayList<>();
        mutableEvents.add(new CalendarEventDisplayData(
                "event-1", "amir", "Meal", "description", LocalDate.now()));

        this.viewModel.getState().setCalendarEvents(mutableEvents);
        mutableEvents.clear();

        assertEquals("Calendar", this.viewModel.getViewName());
        assertEquals(1, this.viewModel.getState().getCalendarEvents().size());
        assertThrows(UnsupportedOperationException.class,
                () -> this.viewModel.getState().getCalendarEvents().clear());
    }

    private void assertCalendarEventWasPresented(CalendarEvent expected) {
        final CalendarEventDisplayData actual =
                this.viewModel.getState().getCalendarEvents().get(0);
        assertEquals(expected.getEventId(), actual.getEventId());
        assertEquals(expected.getUserId(), actual.getUserId());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getActivityDate(), actual.getActivityDate());
    }

    private CalendarEvent event(String eventId, String title, LocalDate date) {
        return new CalendarEvent(eventId, "amir", title, "description", date);
    }
}
