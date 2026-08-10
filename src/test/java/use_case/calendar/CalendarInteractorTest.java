package use_case.calendar;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import entity.CalendarEvent;
import org.junit.jupiter.api.Test;
import use_case.calendar.load_events.LoadCalendarEventsInputData;
import use_case.calendar.load_events.LoadCalendarEventsInteractor;
import use_case.calendar.load_events.LoadCalendarEventsOutputBoundary;
import use_case.calendar.load_events.LoadCalendarEventsOutputData;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CalendarInteractorTest {

    @Test
    void loadEventSuccessIsPresented() {
        final LoadOutputPresenter presenter = new LoadOutputPresenter();
        final InMemoryCalendarDataAccess dataAccess = new InMemoryCalendarDataAccess();
        dataAccess.addCalendarEvent("amir", "Workout", "description", LocalDate.of(2026, 8, 6));
        final LoadCalendarEventsInteractor interactor =
                new LoadCalendarEventsInteractor(dataAccess, presenter);

        interactor.loadCalendarEvents(new LoadCalendarEventsInputData("amir"));

        assertEquals(1, presenter.outputData.getCalendarEvents().size());
        assertEquals("Workout", presenter.outputData.getCalendarEvents().get(0).getTitle());
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

    private static class LoadOutputPresenter
            implements LoadCalendarEventsOutputBoundary {
        private String error;
        private LoadCalendarEventsOutputData outputData;

        @Override
        public void prepareSuccessView(LoadCalendarEventsOutputData data) {
            outputData = data;
        }

        @Override
        public void prepareFailureView(String errorMessage) {
            error = errorMessage;
        }
    }
}
