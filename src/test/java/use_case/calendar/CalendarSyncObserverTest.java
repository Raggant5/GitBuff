package use_case.calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import use_case.calendar.sync_meal_event.UpdateMealCalendarEventInputBoundary;
import use_case.calendar.sync_meal_event.UpdateMealCalendarEventInputData;
import use_case.calendar.sync_meals.SyncMealCalendarEventsInputBoundary;
import use_case.calendar.sync_meals.SyncMealCalendarEventsInputData;
import use_case.calendar.sync_workouts.SyncWorkoutCalendarEventsInputBoundary;
import use_case.calendar.sync_workouts.SyncWorkoutCalendarEventsInputData;
import use_case.login.LoginOutputData;
import use_case.nutrition.meal.MealCalendarSyncObserver;
import use_case.nutrition.meal.MealChangeType;
import use_case.nutrition.meal.MealChangedEvent;
import use_case.recommendation.WorkoutCalendarSyncObserver;
import use_case.recommendation.WorkoutPlanData;
import use_case.recommendation.WorkoutPlanGeneratedEvent;
import use_case.session.CalendarSyncObserver;
import use_case.session.UserLoggedInEvent;

/**
 * Tests the adapters that translate application events into calendar use-case input data.
 */
class CalendarSyncObserverTest {

    @Test
    void mealObserverForwardsEveryChangedMealField() {
        final CapturingMealBoundary boundary = new CapturingMealBoundary();
        final MealCalendarSyncObserver observer = new MealCalendarSyncObserver(boundary);
        final LocalDate date = LocalDate.of(2026, 8, 10);

        observer.onMealChanged(new MealChangedEvent("amir", 42, "Lunch", date, MealChangeType.EDITED));

        assertEquals("amir", boundary.inputData.getUserId());
        assertEquals(42, boundary.inputData.getMealId());
        assertEquals("Lunch", boundary.inputData.getName());
        assertEquals(date, boundary.inputData.getDate());
        assertEquals(MealChangeType.EDITED, boundary.inputData.getChangeType());
    }

    @Test
    void workoutObserverForwardsUserAndGeneratedPlans() {
        final CapturingWorkoutBoundary boundary = new CapturingWorkoutBoundary();
        final WorkoutCalendarSyncObserver observer = new WorkoutCalendarSyncObserver(boundary);
        final List<WorkoutPlanData> plans = List.of();

        observer.onWorkoutPlanGenerated(new WorkoutPlanGeneratedEvent("amir", plans));

        assertEquals("amir", boundary.inputData.getUserId());
        assertSame(plans, boundary.inputData.getWorkoutPlans());
    }

    @Test
    void loginObserverUsesLoggedInUsernameForMealSynchronization() {
        final CapturingMealSyncBoundary boundary = new CapturingMealSyncBoundary();
        final CalendarSyncObserver observer = new CalendarSyncObserver(boundary);
        final LoginOutputData loginData = new LoginOutputData(
                "amir", 0, 0, null, null, null, null, null, null, null,
                Set.of(), Set.of(), Set.of(), 0, Set.of(), false);

        observer.onUserLoggedIn(new UserLoggedInEvent(loginData));

        assertEquals("amir", boundary.inputData.getUserId());
    }

    private static final class CapturingMealBoundary implements UpdateMealCalendarEventInputBoundary {
        private UpdateMealCalendarEventInputData inputData;

        @Override
        public void execute(final UpdateMealCalendarEventInputData data) {
            this.inputData = data;
        }
    }

    private static final class CapturingWorkoutBoundary implements SyncWorkoutCalendarEventsInputBoundary {
        private SyncWorkoutCalendarEventsInputData inputData;

        @Override
        public void execute(final SyncWorkoutCalendarEventsInputData data) {
            this.inputData = data;
        }
    }

    private static final class CapturingMealSyncBoundary implements SyncMealCalendarEventsInputBoundary {
        private SyncMealCalendarEventsInputData inputData;

        @Override
        public void execute(final SyncMealCalendarEventsInputData data) {
            this.inputData = data;
        }
    }
}
