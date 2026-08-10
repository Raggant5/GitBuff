package use_case.calendar.sync_meal_event;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import entity.CalendarEvent;
import use_case.calendar.CalendarEventDataAccessInterface;
import use_case.nutrition.meal.MealChangeType;

/**
 * Unit tests for the Update Meal Calendar Event Interactor - the single-meal sync moved out of
 * {@code interface_adapter.calendar.CalendarController#addMeal}/{@code updateMeal}/
 * {@code removeMeal}.
 */
class UpdateMealCalendarEventInteractorTest {

    @Test
    void addedMealCreatesCalendarEventWithStableReference() {
        final FakeCalendarEventDataAccessObject calendarDao = new FakeCalendarEventDataAccessObject();
        final CapturingPresenter presenter = new CapturingPresenter();

        new UpdateMealCalendarEventInteractor(calendarDao, presenter).execute(
                new UpdateMealCalendarEventInputData(
                        "amir", 12, "Lunch", LocalDate.of(2026, 8, 6), MealChangeType.ADDED));

        assertEquals(1, calendarDao.addedTitles.size());
        assertEquals("Meal: Lunch", calendarDao.addedTitles.get(0));
        assertEquals("GitBuff meal ID: 12", calendarDao.addedDescriptions.get(0));
        assertEquals(0, calendarDao.removedEventIds.size());
    }

    @Test
    void deletedMealRemovesMatchingCalendarEvent() {
        final FakeCalendarEventDataAccessObject calendarDao = new FakeCalendarEventDataAccessObject();
        calendarDao.events.add(new CalendarEvent("google-event-4", "amir", "Meal: Dinner",
                "GitBuff meal ID: 4", LocalDate.now()));
        final CapturingPresenter presenter = new CapturingPresenter();

        new UpdateMealCalendarEventInteractor(calendarDao, presenter).execute(
                new UpdateMealCalendarEventInputData("amir", 4, null, null, MealChangeType.DELETED));

        assertEquals(1, calendarDao.removedEventIds.size());
        assertEquals("google-event-4", calendarDao.removedEventIds.get(0));
        assertEquals(0, calendarDao.addedTitles.size());
    }

    @Test
    void editedMealReplacesCalendarEvent() {
        final FakeCalendarEventDataAccessObject calendarDao = new FakeCalendarEventDataAccessObject();
        calendarDao.events.add(new CalendarEvent("google-event-4", "amir", "Meal: Dinner",
                "GitBuff meal ID: 4", LocalDate.of(2026, 8, 6)));
        final CapturingPresenter presenter = new CapturingPresenter();

        new UpdateMealCalendarEventInteractor(calendarDao, presenter).execute(
                new UpdateMealCalendarEventInputData(
                        "amir", 4, "Late Dinner", LocalDate.of(2026, 8, 7), MealChangeType.EDITED));

        assertEquals(1, calendarDao.removedEventIds.size());
        assertEquals("google-event-4", calendarDao.removedEventIds.get(0));
        assertEquals(1, calendarDao.addedTitles.size());
        assertEquals("Meal: Late Dinner", calendarDao.addedTitles.get(0));
    }

    @Test
    void addedMealWithNoMatchingExistingEventLeavesOtherEventsUntouched() {
        final FakeCalendarEventDataAccessObject calendarDao = new FakeCalendarEventDataAccessObject();
        calendarDao.events.add(new CalendarEvent("google-event-1", "amir", "Meal: Breakfast",
                "GitBuff meal ID: 1", LocalDate.now()));
        final CapturingPresenter presenter = new CapturingPresenter();

        new UpdateMealCalendarEventInteractor(calendarDao, presenter).execute(
                new UpdateMealCalendarEventInputData(
                        "amir", 12, "Lunch", LocalDate.of(2026, 8, 6), MealChangeType.ADDED));

        assertEquals(0, calendarDao.removedEventIds.size());
        assertEquals(1, calendarDao.addedTitles.size());
        assertEquals("Meal: Lunch", calendarDao.addedTitles.get(0));
    }

    @Test
    void calendarFailureIsSentToFailureView() {
        final FakeCalendarEventDataAccessObject calendarDao = new FakeCalendarEventDataAccessObject();
        calendarDao.failureMessage = "Calendar unavailable";
        final CapturingPresenter presenter = new CapturingPresenter();

        new UpdateMealCalendarEventInteractor(calendarDao, presenter).execute(
                new UpdateMealCalendarEventInputData(
                        "amir", 12, "Lunch", LocalDate.of(2026, 8, 6), MealChangeType.ADDED));

        assertEquals("Calendar unavailable", presenter.failureMessage);
    }

    private static final class FakeCalendarEventDataAccessObject implements CalendarEventDataAccessInterface {
        private final List<CalendarEvent> events = new ArrayList<>();
        private final List<String> addedTitles = new ArrayList<>();
        private final List<String> addedDescriptions = new ArrayList<>();
        private final List<String> removedEventIds = new ArrayList<>();
        private int nextId = 1;
        private String failureMessage;

        @Override
        public void addCalendarEvent(final String userId, final String title, final String description,
                                     final LocalDate activityDate) {
            events.add(new CalendarEvent("generated-" + nextId++, userId, title, description, activityDate));
            addedTitles.add(title);
            addedDescriptions.add(description);
        }

        @Override
        public void removeCalendarEvent(final String userId, final String eventId) {
            events.removeIf(event -> event.getEventId().equals(eventId));
            removedEventIds.add(eventId);
        }

        @Override
        public List<CalendarEvent> getUserEvents(final String userID) {
            if (failureMessage != null) {
                throw new IllegalStateException(failureMessage);
            }
            return new ArrayList<>(events);
        }
    }

    private static final class CapturingPresenter implements UpdateMealCalendarEventOutputBoundary {
        private String failureMessage;

        @Override
        public void prepareSuccessView(final UpdateMealCalendarEventOutputData outputData) {
            // tests assert on the DAO's captured calls instead
        }

        @Override
        public void prepareFailureView(final String errorMessage) {
            this.failureMessage = errorMessage;
        }
    }
}
