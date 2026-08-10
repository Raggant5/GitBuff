package use_case.calendar.sync_meals;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import entity.CalendarEvent;
import entity.FoodEntry;
import entity.Meal;
import use_case.calendar.CalendarEventDataAccessInterface;
import use_case.nutrition.meal.get_meals.ViewMealDataAccessInterface;

/**
 * Unit tests for the Sync Meal Calendar Events Interactor - the bulk reconciliation moved out
 * of {@code interface_adapter.calendar.CalendarController#synchronizeMeals}.
 */
class SyncMealCalendarEventsInteractorTest {

    @Test
    void backfillsMissingMealsWithoutDuplicatingExistingEvents() {
        final Meal existingMeal = new Meal("amir", LocalDate.of(2026, 8, 6), "Lunch");
        existingMeal.setId(12);
        final Meal missingMeal = new Meal("amir", LocalDate.of(2026, 8, 7), "Dinner");
        missingMeal.setId(13);

        final FakeCalendarEventDataAccessObject calendarDao = new FakeCalendarEventDataAccessObject();
        calendarDao.events.add(new CalendarEvent("google-event-12", "amir", "Meal: Lunch",
                "GitBuff meal ID: 12", existingMeal.getDate()));
        final FakeViewMealDataAccessObject mealDao = new FakeViewMealDataAccessObject(
                List.of(existingMeal, missingMeal));
        final CapturingPresenter presenter = new CapturingPresenter();

        new SyncMealCalendarEventsInteractor(calendarDao, mealDao, presenter)
                .execute(new SyncMealCalendarEventsInputData("amir"));

        assertEquals(1, calendarDao.addedTitles.size());
        assertEquals("Meal: Dinner", calendarDao.addedTitles.get(0));
        assertEquals(0, calendarDao.removedEventIds.size());
    }

    @Test
    void removesEventForMealThatNoLongerExists() {
        final FakeCalendarEventDataAccessObject calendarDao = new FakeCalendarEventDataAccessObject();
        calendarDao.events.add(new CalendarEvent("google-event-4", "amir", "Meal: Dinner",
                "GitBuff meal ID: 4", LocalDate.now()));
        final FakeViewMealDataAccessObject mealDao = new FakeViewMealDataAccessObject(List.of());
        final CapturingPresenter presenter = new CapturingPresenter();

        new SyncMealCalendarEventsInteractor(calendarDao, mealDao, presenter)
                .execute(new SyncMealCalendarEventsInputData("amir"));

        assertEquals(1, calendarDao.removedEventIds.size());
        assertEquals("google-event-4", calendarDao.removedEventIds.get(0));
        assertEquals(0, calendarDao.addedTitles.size());
    }

    @Test
    void staleEventWithNoMatchAmongDesiredMealsIsRemoved() {
        final Meal desiredMeal = new Meal("amir", LocalDate.of(2026, 8, 7), "Dinner");
        desiredMeal.setId(99);

        final FakeCalendarEventDataAccessObject calendarDao = new FakeCalendarEventDataAccessObject();
        calendarDao.events.add(new CalendarEvent("google-event-4", "amir", "Meal: Lunch",
                "GitBuff meal ID: 4", LocalDate.of(2026, 8, 6)));
        final FakeViewMealDataAccessObject mealDao = new FakeViewMealDataAccessObject(List.of(desiredMeal));
        final CapturingPresenter presenter = new CapturingPresenter();

        new SyncMealCalendarEventsInteractor(calendarDao, mealDao, presenter)
                .execute(new SyncMealCalendarEventsInputData("amir"));

        assertEquals(1, calendarDao.removedEventIds.size());
        assertEquals("google-event-4", calendarDao.removedEventIds.get(0));
        assertEquals(1, calendarDao.addedTitles.size());
        assertEquals("Meal: Dinner", calendarDao.addedTitles.get(0));
    }

    @Test
    void calendarFailureIsSentToFailureView() {
        final FakeCalendarEventDataAccessObject calendarDao = new FakeCalendarEventDataAccessObject();
        calendarDao.failureMessage = "Calendar unavailable";
        final FakeViewMealDataAccessObject mealDao = new FakeViewMealDataAccessObject(List.of());
        final CapturingPresenter presenter = new CapturingPresenter();

        new SyncMealCalendarEventsInteractor(calendarDao, mealDao, presenter)
                .execute(new SyncMealCalendarEventsInputData("amir"));

        assertEquals("Calendar unavailable", presenter.failureMessage);
    }

    private static final class FakeCalendarEventDataAccessObject implements CalendarEventDataAccessInterface {
        private final List<CalendarEvent> events = new ArrayList<>();
        private final List<String> addedTitles = new ArrayList<>();
        private final List<String> removedEventIds = new ArrayList<>();
        private int nextId = 1;
        private String failureMessage;

        @Override
        public void addCalendarEvent(final String userId, final String title, final String description,
                                     final LocalDate activityDate) {
            events.add(new CalendarEvent("generated-" + nextId++, userId, title, description, activityDate));
            addedTitles.add(title);
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

    private static final class FakeViewMealDataAccessObject implements ViewMealDataAccessInterface {
        private final List<Meal> meals;

        private FakeViewMealDataAccessObject(final List<Meal> meals) {
            this.meals = meals;
        }

        @Override
        public List<Meal> getMealsForUser(final String userId) {
            return meals;
        }

        @Override
        public List<FoodEntry> getFoodEntriesForMeal(final int mealId) {
            return List.of();
        }

        @Override
        public Meal getMealById(final int mealId) {
            return null;
        }
    }

    private static final class CapturingPresenter implements SyncMealCalendarEventsOutputBoundary {
        private String failureMessage;

        @Override
        public void prepareSuccessView(final SyncMealCalendarEventsOutputData outputData) {
            // tests assert on the DAO's captured calls instead
        }

        @Override
        public void prepareFailureView(final String errorMessage) {
            this.failureMessage = errorMessage;
        }
    }
}
