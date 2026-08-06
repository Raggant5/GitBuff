package use_case.calendar;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import entity.CalendarEvent;
import org.junit.jupiter.api.Test;
import use_case.calendar.add_event.AddCalendarEventInputData;
import use_case.calendar.add_event.AddCalendarEventInteractor;
import use_case.calendar.add_event.AddCalendarEventOutputBoundary;
import use_case.calendar.add_event.AddCalendarEventOutputData;
import use_case.calendar.load_events.LoadCalendarEventsInputData;
import use_case.calendar.load_events.LoadCalendarEventsInteractor;
import use_case.calendar.load_events.LoadCalendarEventsOutputBoundary;
import use_case.calendar.load_events.LoadCalendarEventsOutputData;
import use_case.calendar.remove_event.RemoveCalendarEventInputData;
import use_case.calendar.remove_event.RemoveCalendarEventInteractor;
import use_case.calendar.remove_event.RemoveCalendarEventOutputBoundary;
import use_case.calendar.remove_event.RemoveCalendarEventOutputData;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CalendarInteractorTest {

    @Test
    void addEventStoresAndReturnsUpdatedCalendar() {
        final InMemoryCalendarDataAccess dataAccess =
                new InMemoryCalendarDataAccess();
        final AddOutputPresenter presenter = new AddOutputPresenter();
        final AddCalendarEventInteractor interactor =
                new AddCalendarEventInteractor(dataAccess, presenter);

        interactor.addCalendarEvent(new AddCalendarEventInputData(
                "amir",
                "Meal: Lunch",
                "GitBuff meal ID: 7",
                LocalDate.of(2026, 8, 6)));

        assertNull(presenter.error);
        assertEquals(1, presenter.outputData.getCalendarEvents().size());
        assertEquals("Meal: Lunch",
                presenter.outputData.getCalendarEvents().get(0).getTitle());
    }

    @Test
    void removeEventDeletesAndReturnsUpdatedCalendar() {
        final InMemoryCalendarDataAccess dataAccess =
                new InMemoryCalendarDataAccess();
        dataAccess.addCalendarEvent(
                "amir", "Workout", "description", LocalDate.now());
        final String eventId = dataAccess.events.get(0).getEventId();
        final RemoveOutputPresenter presenter = new RemoveOutputPresenter();
        final RemoveCalendarEventInteractor interactor =
                new RemoveCalendarEventInteractor(dataAccess, presenter);

        interactor.removeCalendarEvent(
                new RemoveCalendarEventInputData("amir", eventId));

        assertNull(presenter.error);
        assertEquals(0, presenter.outputData.getCalendarEvents().size());
    }

    @Test
    void loadEventFailureIsPresented() {
        final LoadOutputPresenter presenter = new LoadOutputPresenter();
        final CalendarEventDataAccessInterface failingDataAccess =
                new InMemoryCalendarDataAccess() {
                    @Override
                    public List<CalendarEvent> getUserEvents(String userId) {
                        throw new IllegalStateException("Calendar unavailable");
                    }
                };
        final LoadCalendarEventsInteractor interactor =
                new LoadCalendarEventsInteractor(failingDataAccess, presenter);

        interactor.loadCalendarEvents(new LoadCalendarEventsInputData("amir"));

        assertEquals("Calendar unavailable", presenter.error);
    }

    private static class InMemoryCalendarDataAccess
            implements CalendarEventDataAccessInterface {
        private final List<CalendarEvent> events = new ArrayList<>();
        private int nextId = 1;

        @Override
        public void addCalendarEvent(String userId, String title,
                                     String description, LocalDate activityDate) {
            events.add(new CalendarEvent(
                    String.valueOf(nextId++), userId, title,
                    description, activityDate));
        }

        @Override
        public void removeCalendarEvent(String userId, String eventId) {
            events.removeIf(event -> event.getUserId().equals(userId)
                    && event.getEventId().equals(eventId));
        }

        @Override
        public List<CalendarEvent> getUserEvents(String userId) {
            return events.stream()
                    .filter(event -> event.getUserId().equals(userId))
                    .toList();
        }
    }

    private static class AddOutputPresenter
            implements AddCalendarEventOutputBoundary {
        private AddCalendarEventOutputData outputData;
        private String error;

        @Override
        public void prepareSuccessView(AddCalendarEventOutputData data) {
            outputData = data;
        }

        @Override
        public void prepareFailureView(String errorMessage) {
            error = errorMessage;
        }
    }

    private static class RemoveOutputPresenter
            implements RemoveCalendarEventOutputBoundary {
        private RemoveCalendarEventOutputData outputData;
        private String error;

        @Override
        public void prepareSuccessView(RemoveCalendarEventOutputData data) {
            outputData = data;
        }

        @Override
        public void prepareFailureView(String errorMessage) {
            error = errorMessage;
        }
    }

    private static class LoadOutputPresenter
            implements LoadCalendarEventsOutputBoundary {
        private String error;

        @Override
        public void prepareSuccessView(LoadCalendarEventsOutputData data) {
            // Not used by the failure test.
        }

        @Override
        public void prepareFailureView(String errorMessage) {
            error = errorMessage;
        }
    }
}
