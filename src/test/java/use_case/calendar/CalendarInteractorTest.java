package use_case.calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import entity.CalendarEvent;
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

class CalendarInteractorTest {

    private static final LocalDate EVENT_DATE = LocalDate.of(2026, 8, 6);

    @Test
    void addEventStoresAndReturnsUpdatedCalendar() {
        final InMemoryCalendarDataAccess dataAccess = new InMemoryCalendarDataAccess();
        final AddOutputPresenter presenter = new AddOutputPresenter();
        final AddCalendarEventInteractor interactor =
                new AddCalendarEventInteractor(dataAccess, presenter);

        interactor.addCalendarEvent(new AddCalendarEventInputData(
                "amir", "Meal: Lunch", "GitBuff meal ID: 7", EVENT_DATE));

        assertNull(presenter.error);
        assertEquals(1, presenter.outputData.getCalendarEvents().size());
        assertEquals("Meal: Lunch",
                presenter.outputData.getCalendarEvents().get(0).getTitle());
    }

    @Test
    void addEventFailureIsPresented() {
        final AddOutputPresenter presenter = new AddOutputPresenter();
        final InMemoryCalendarDataAccess dataAccess = new InMemoryCalendarDataAccess();
        dataAccess.addFailure = new IllegalStateException("Unable to add event");
        final AddCalendarEventInteractor interactor =
                new AddCalendarEventInteractor(dataAccess, presenter);

        interactor.addCalendarEvent(new AddCalendarEventInputData(
                "amir", "Workout", "description", EVENT_DATE));

        assertEquals("Unable to add event", presenter.error);
        assertNull(presenter.outputData);
    }

    @Test
    void removeEventDeletesAndReturnsUpdatedCalendar() {
        final InMemoryCalendarDataAccess dataAccess = new InMemoryCalendarDataAccess();
        dataAccess.addCalendarEvent("amir", "Workout", "description", EVENT_DATE);
        final String eventId = dataAccess.events.get(0).getEventId();
        final RemoveOutputPresenter presenter = new RemoveOutputPresenter();
        final RemoveCalendarEventInteractor interactor =
                new RemoveCalendarEventInteractor(dataAccess, presenter);

        interactor.removeCalendarEvent(new RemoveCalendarEventInputData("amir", eventId));

        assertNull(presenter.error);
        assertEquals(0, presenter.outputData.getCalendarEvents().size());
    }

    @Test
    void removeEventFailureIsPresented() {
        final RemoveOutputPresenter presenter = new RemoveOutputPresenter();
        final InMemoryCalendarDataAccess dataAccess = new InMemoryCalendarDataAccess();
        dataAccess.removeFailure = new IllegalStateException("Unable to remove event");
        final RemoveCalendarEventInteractor interactor =
                new RemoveCalendarEventInteractor(dataAccess, presenter);

        interactor.removeCalendarEvent(new RemoveCalendarEventInputData("amir", "event-1"));

        assertEquals("Unable to remove event", presenter.error);
        assertNull(presenter.outputData);
    }

    @Test
    void loadEventsReturnsUserCalendar() {
        final InMemoryCalendarDataAccess dataAccess = new InMemoryCalendarDataAccess();
        dataAccess.addCalendarEvent("amir", "Workout", "description", EVENT_DATE);
        dataAccess.addCalendarEvent("other", "Meal", "description", EVENT_DATE);
        final LoadOutputPresenter presenter = new LoadOutputPresenter();
        final LoadCalendarEventsInteractor interactor =
                new LoadCalendarEventsInteractor(dataAccess, presenter);

        interactor.loadCalendarEvents(new LoadCalendarEventsInputData("amir"));

        assertNull(presenter.error);
        assertEquals(1, presenter.outputData.getCalendarEvents().size());
        assertEquals("amir", presenter.outputData.getCalendarEvents().get(0).getUserId());
    }

    @Test
    void loadEventFailureIsPresented() {
        final LoadOutputPresenter presenter = new LoadOutputPresenter();
        final InMemoryCalendarDataAccess dataAccess = new InMemoryCalendarDataAccess();
        dataAccess.loadFailure = new IllegalStateException("Calendar unavailable");
        final LoadCalendarEventsInteractor interactor =
                new LoadCalendarEventsInteractor(dataAccess, presenter);

        interactor.loadCalendarEvents(new LoadCalendarEventsInputData("amir"));

        assertEquals("Calendar unavailable", presenter.error);
        assertNull(presenter.outputData);
    }

    @Test
    void calendarDataObjectsExposeValuesAndCopyOutputLists() {
        final CalendarEvent event = new CalendarEvent(
                "event-1", "amir", "Workout", "description", EVENT_DATE);
        final List<CalendarEvent> mutableEvents = new ArrayList<>(List.of(event));
        final AddCalendarEventInputData addInput = new AddCalendarEventInputData(
                "amir", "Workout", "description", EVENT_DATE);
        final LoadCalendarEventsInputData loadInput = new LoadCalendarEventsInputData("amir");
        final RemoveCalendarEventInputData removeInput =
                new RemoveCalendarEventInputData("amir", "event-1");
        final AddCalendarEventOutputData addOutput =
                new AddCalendarEventOutputData(mutableEvents);
        final LoadCalendarEventsOutputData loadOutput =
                new LoadCalendarEventsOutputData(mutableEvents);
        final RemoveCalendarEventOutputData removeOutput =
                new RemoveCalendarEventOutputData(mutableEvents);

        mutableEvents.clear();

        assertEquals("amir", addInput.getUserId());
        assertEquals("Workout", addInput.getTitle());
        assertEquals("description", addInput.getDescription());
        assertEquals(EVENT_DATE, addInput.getActivityDate());
        assertEquals("amir", loadInput.getUserId());
        assertEquals("amir", removeInput.getUserId());
        assertEquals("event-1", removeInput.getEventId());
        assertEquals(1, addOutput.getCalendarEvents().size());
        assertEquals(1, loadOutput.getCalendarEvents().size());
        assertEquals(1, removeOutput.getCalendarEvents().size());
        assertThrows(UnsupportedOperationException.class,
                () -> addOutput.getCalendarEvents().clear());
        assertThrows(UnsupportedOperationException.class,
                () -> loadOutput.getCalendarEvents().clear());
        assertThrows(UnsupportedOperationException.class,
                () -> removeOutput.getCalendarEvents().clear());
    }

    private static final class InMemoryCalendarDataAccess
            implements CalendarEventDataAccessInterface {
        private final List<CalendarEvent> events = new ArrayList<>();
        private int nextId = 1;
        private IllegalStateException addFailure;
        private IllegalStateException removeFailure;
        private IllegalStateException loadFailure;

        @Override
        public void addCalendarEvent(String userId, String title,
                                     String description, LocalDate activityDate) {
            if (this.addFailure != null) {
                throw this.addFailure;
            }
            this.events.add(new CalendarEvent(
                    String.valueOf(this.nextId++), userId, title,
                    description, activityDate));
        }

        @Override
        public void removeCalendarEvent(String userId, String eventId) {
            if (this.removeFailure != null) {
                throw this.removeFailure;
            }
            this.events.removeIf(event -> {
                return event.getUserId().equals(userId)
                        && event.getEventId().equals(eventId);
            });
        }

        @Override
        public List<CalendarEvent> getUserEvents(String userId) {
            if (this.loadFailure != null) {
                throw this.loadFailure;
            }
            return this.events.stream()
                    .filter(event -> event.getUserId().equals(userId))
                    .toList();
        }
    }

    private static final class AddOutputPresenter implements AddCalendarEventOutputBoundary {
        private AddCalendarEventOutputData outputData;
        private String error;

        @Override
        public void prepareSuccessView(AddCalendarEventOutputData data) {
            this.outputData = data;
        }

        @Override
        public void prepareFailureView(String errorMessage) {
            this.error = errorMessage;
        }
    }

    private static final class RemoveOutputPresenter implements RemoveCalendarEventOutputBoundary {
        private RemoveCalendarEventOutputData outputData;
        private String error;

        @Override
        public void prepareSuccessView(RemoveCalendarEventOutputData data) {
            this.outputData = data;
        }

        @Override
        public void prepareFailureView(String errorMessage) {
            this.error = errorMessage;
        }
    }

    private static final class LoadOutputPresenter implements LoadCalendarEventsOutputBoundary {
        private LoadCalendarEventsOutputData outputData;
        private String error;

        @Override
        public void prepareSuccessView(LoadCalendarEventsOutputData data) {
            this.outputData = data;
        }

        @Override
        public void prepareFailureView(String errorMessage) {
            this.error = errorMessage;
        }
    }
}
