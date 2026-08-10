package interface_adapter.calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import interface_adapter.login.LoginViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.calendar.load_events.LoadCalendarEventsInputBoundary;
import use_case.calendar.load_events.LoadCalendarEventsInputData;

/**
 * Unit tests for the Calendar Controller, which now only loads the calendar for redisplay
 * (e.g. on Dashboard nav) - single-meal sync and bulk reconciliation moved to
 * {@code use_case.calendar.sync_meal_event.UpdateMealCalendarEventInteractor}/
 * {@code use_case.calendar.sync_meals.SyncMealCalendarEventsInteractor}/
 * {@code use_case.calendar.sync_workouts.SyncWorkoutCalendarEventsInteractor}, see their own
 * tests.
 */
class CalendarControllerTest {

    private final List<LoadCalendarEventsInputData> loadCalls = new ArrayList<>();
    private LoginViewModel loginViewModel;
    private CalendarController controller;

    @BeforeEach
    void setUp() {
        loginViewModel = new LoginViewModel();
        final LoadCalendarEventsInputBoundary loadBoundary = loadCalls::add;
        controller = new CalendarController(loadBoundary, loginViewModel);
    }

    @Test
    void loadCalendarEventsDelegatesToInteractorWithCurrentUser() {
        loginViewModel.getState().setUsername("amir");

        controller.loadCalendarEvents();

        assertEquals(1, loadCalls.size());
        assertEquals("amir", loadCalls.get(0).getUserID());
    }

    @Test
    void loadCalendarEventsDoesNothingWithoutLoggedInUser() {
        controller.loadCalendarEvents();

        assertTrue(loadCalls.isEmpty());
    }
}
