package use_case.calendar.sync_workouts;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.Test;

import entity.CalendarEvent;
import use_case.calendar.CalendarEventDataAccessInterface;
import use_case.recommendation.ExerciseData;
import use_case.recommendation.WorkoutPlanData;

/**
 * Unit tests for the Sync Workout Calendar Events Interactor - the bulk reconciliation moved
 * out of {@code interface_adapter.calendar.CalendarController#replaceWorkoutPlans}.
 */
class SyncWorkoutCalendarEventsInteractorTest {

    @Test
    void activeWorkoutIsAddedAndRestDayIsSkipped() {
        final String today = LocalDate.now().format(
                DateTimeFormatter.ofPattern("EEEE, MMM d", Locale.ENGLISH));
        final ExerciseData exercise = new ExerciseData(
                "Squat", 3, 10, 5, "Legs", "Barbell", "Lower with control", "");
        final WorkoutPlanData workout = new WorkoutPlanData(
                today, "Strength", "Leg session", 30, 300, 10, 30, List.of(exercise));
        final WorkoutPlanData rest = new WorkoutPlanData(
                today, "Rest & Recovery", "Rest", 0, 0, 0, 0, List.of());

        final FakeCalendarEventDataAccessObject calendarDao = new FakeCalendarEventDataAccessObject();
        final CapturingPresenter presenter = new CapturingPresenter();

        new SyncWorkoutCalendarEventsInteractor(calendarDao, presenter)
                .execute(new SyncWorkoutCalendarEventsInputData("amir", List.of(workout, rest)));

        assertEquals(1, calendarDao.addedTitles.size());
        assertEquals("Workout: Strength", calendarDao.addedTitles.get(0));
        assertEquals(LocalDate.now(), calendarDao.addedDates.get(0));
    }

    @Test
    void matchingExistingWorkoutIsKeptAndStaleWorkoutIsRemoved() {
        final String today = LocalDate.now().format(
                DateTimeFormatter.ofPattern("EEEE, MMM d", Locale.ENGLISH));
        final ExerciseData exercise = new ExerciseData(
                "Squat", 3, 10, 5, "Legs", "Barbell", "Lower with control", "");
        final WorkoutPlanData workout = new WorkoutPlanData(
                today, "Strength", "Leg session", 30, 300, 10, 30, List.of(exercise));

        final FakeCalendarEventDataAccessObject calendarDao = new FakeCalendarEventDataAccessObject();
        calendarDao.events.add(new CalendarEvent("stale-event", "amir", "Workout: Old Plan",
                "GitBuff workout schedule\nNo longer scheduled", LocalDate.now().minusDays(3)));
        calendarDao.events.add(new CalendarEvent("kept-event", "amir", "Workout: Strength",
                "GitBuff workout schedule\nLeg session", LocalDate.now()));
        final CapturingPresenter presenter = new CapturingPresenter();

        new SyncWorkoutCalendarEventsInteractor(calendarDao, presenter)
                .execute(new SyncWorkoutCalendarEventsInputData("amir", List.of(workout)));

        assertEquals(0, calendarDao.addedTitles.size());
        assertEquals(1, calendarDao.removedEventIds.size());
        assertEquals("stale-event", calendarDao.removedEventIds.get(0));
    }

    @Test
    void blankAndMalformedAndPastWorkoutDatesAreHandled() {
        final ExerciseData exercise = new ExerciseData(
                "Squat", 3, 10, 5, "Legs", "Barbell", "Lower with control", "");
        final WorkoutPlanData blankDatePlan = new WorkoutPlanData(
                "", "Blank Date", "skipped", 30, 300, 10, 30, List.of(exercise));
        final WorkoutPlanData malformedDatePlan = new WorkoutPlanData(
                "Not A Date", "Malformed Date", "skipped", 30, 300, 10, 30, List.of(exercise));

        final LocalDate pastAnchor = LocalDate.of(LocalDate.now().getYear(), 1, 1);
        final String pastDateText = pastAnchor.format(DateTimeFormatter.ofPattern("EEEE, MMM d", Locale.ENGLISH));
        final WorkoutPlanData pastYearPlan = new WorkoutPlanData(
                pastDateText, "Rolled Over", "rolls to next year", 30, 300, 10, 30, List.of(exercise));

        final FakeCalendarEventDataAccessObject calendarDao = new FakeCalendarEventDataAccessObject();
        final CapturingPresenter presenter = new CapturingPresenter();

        new SyncWorkoutCalendarEventsInteractor(calendarDao, presenter)
                .execute(new SyncWorkoutCalendarEventsInputData(
                        "amir", List.of(blankDatePlan, malformedDatePlan, pastYearPlan)));

        assertEquals(1, calendarDao.addedTitles.size());
        assertEquals("Workout: Rolled Over", calendarDao.addedTitles.get(0));
        assertEquals(pastAnchor.plusYears(1), calendarDao.addedDates.get(0));
    }

    @Test
    void calendarFailureIsSentToFailureView() {
        final FakeCalendarEventDataAccessObject calendarDao = new FakeCalendarEventDataAccessObject();
        calendarDao.failureMessage = "Calendar unavailable";
        final CapturingPresenter presenter = new CapturingPresenter();

        new SyncWorkoutCalendarEventsInteractor(calendarDao, presenter)
                .execute(new SyncWorkoutCalendarEventsInputData("amir", List.of()));

        assertEquals("Calendar unavailable", presenter.failureMessage);
    }

    private static final class FakeCalendarEventDataAccessObject implements CalendarEventDataAccessInterface {
        private final List<CalendarEvent> events = new ArrayList<>();
        private final List<String> addedTitles = new ArrayList<>();
        private final List<LocalDate> addedDates = new ArrayList<>();
        private final List<String> removedEventIds = new ArrayList<>();
        private int nextId = 1;
        private String failureMessage;

        @Override
        public void addCalendarEvent(final String userId, final String title, final String description,
                                     final LocalDate activityDate) {
            events.add(new CalendarEvent("generated-" + nextId++, userId, title, description, activityDate));
            addedTitles.add(title);
            addedDates.add(activityDate);
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

    private static final class CapturingPresenter implements SyncWorkoutCalendarEventsOutputBoundary {
        private String failureMessage;

        @Override
        public void prepareSuccessView(final SyncWorkoutCalendarEventsOutputData outputData) {
            // tests assert on the DAO's captured calls instead
        }

        @Override
        public void prepareFailureView(final String errorMessage) {
            this.failureMessage = errorMessage;
        }
    }
}
