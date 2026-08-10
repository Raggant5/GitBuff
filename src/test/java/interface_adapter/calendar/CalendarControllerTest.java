package interface_adapter.calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import interface_adapter.login.LoginViewModel;
import interface_adapter.nutrition.meal.MealDisplayData;
import interface_adapter.workouts.RecommendedExerciseDisplayData;
import interface_adapter.workouts.WorkoutPlanDisplayData;
import use_case.calendar.add_event.AddCalendarEventInputBoundary;
import use_case.calendar.add_event.AddCalendarEventInputData;
import use_case.calendar.load_events.LoadCalendarEventsInputBoundary;
import use_case.calendar.load_events.LoadCalendarEventsInputData;
import use_case.calendar.remove_event.RemoveCalendarEventInputBoundary;
import use_case.calendar.remove_event.RemoveCalendarEventInputData;

class CalendarControllerTest {
    private final List<AddCalendarEventInputData> addedEvents =
            new ArrayList<>();
    private final List<RemoveCalendarEventInputData> removedEvents =
            new ArrayList<>();
    private final List<LoadCalendarEventsInputData> loadRequests =
            new ArrayList<>();
    private LoginViewModel loginViewModel;
    private CalendarViewModel calendarViewModel;
    private CalendarController controller;

    @BeforeEach
    void setUp() {
        loginViewModel = new LoginViewModel();
        loginViewModel.getState().setUsername("amir");
        calendarViewModel = new CalendarViewModel();

        final AddCalendarEventInputBoundary addBoundary = addedEvents::add;
        final RemoveCalendarEventInputBoundary removeBoundary = removedEvents::add;
        final LoadCalendarEventsInputBoundary loadBoundary = loadRequests::add;

        controller = new CalendarController(
                addBoundary,
                removeBoundary,
                loadBoundary,
                loginViewModel,
                calendarViewModel);
    }

    @Test
    void savedMealCreatesCalendarEventWithStableReference() {
        final MealDisplayData meal = new MealDisplayData(
                12, LocalDate.of(2026, 8, 6), "Lunch", List.of());

        controller.addMeal(meal.getId(), meal.getName(), meal.getDate());

        assertEquals(1, addedEvents.size());
        assertEquals("Meal: Lunch", addedEvents.get(0).getTitle());
        assertEquals("GitBuff meal ID: 12",
                addedEvents.get(0).getDescription());
    }

    @Test
    void deletingMealRemovesMatchingGoogleEvent() {
        final int mealId = 4;
        calendarViewModel.getState().setCalendarEvents(List.of(
                new CalendarEventDisplayData(
                        "google-event-4",
                        "amir",
                        "Meal: Dinner",
                        "GitBuff meal ID: 4",
                        LocalDate.now())));

        controller.removeMeal(mealId);

        assertEquals(1, removedEvents.size());
        assertEquals("google-event-4", removedEvents.get(0).getEventId());
    }

    @Test
    void activeWorkoutIsAddedAndRestDayIsSkipped() {
        final String today = LocalDate.now().format(
                DateTimeFormatter.ofPattern("EEEE, MMM d", Locale.ENGLISH));
        final RecommendedExerciseDisplayData exercise = new RecommendedExerciseDisplayData(
                "Squat", 3, 10, 5, "Legs", "Barbell", "Lower with control", "");
        final WorkoutPlanDisplayData workout = new WorkoutPlanDisplayData(
                today, "Strength", "Leg session",
                30, 300, 10, 30, List.of(exercise));
        final WorkoutPlanDisplayData rest = new WorkoutPlanDisplayData(
                today, "Rest & Recovery", "Rest",
                0, 0, 0, 0, List.of());

        controller.replaceWorkoutPlans(List.of(workout, rest));

        assertEquals(1, addedEvents.size());
        assertEquals("Workout: Strength", addedEvents.get(0).getTitle());
        assertEquals(LocalDate.now(), addedEvents.get(0).getActivityDate());
    }

    @Test
    void savedMealsAreBackfilledWithoutDuplicatingExistingEvents() {
        final MealDisplayData existingMeal = new MealDisplayData(
                12, LocalDate.of(2026, 8, 6), "Lunch", List.of());
        final MealDisplayData missingMeal = new MealDisplayData(
                13, LocalDate.of(2026, 8, 7), "Dinner", List.of());

        calendarViewModel.getState().setCalendarEvents(List.of(
                new CalendarEventDisplayData(
                        "google-event-12",
                        "amir",
                        "Meal: Lunch",
                        "GitBuff meal ID: 12",
                        existingMeal.getDate())));

        controller.synchronizeMeals(List.of(existingMeal, missingMeal));

        assertEquals(1, addedEvents.size());
        assertEquals("Meal: Dinner", addedEvents.get(0).getTitle());
        assertEquals(0, removedEvents.size());
    }

    @Test
    void loadingCalendarUsesLoggedInUsernameAndSkipsBlankUser() {
        controller.loadCalendarEvents();

        assertEquals(1, loadRequests.size());
        assertEquals("amir", loadRequests.get(0).getUserId());

        loginViewModel.getState().setUsername("  ");
        controller.loadCalendarEvents();

        assertEquals(1, loadRequests.size());
    }

    @Test
    void nullMealDateAndBlankUserDoNotCreateEvents() {
        controller.addMeal(1, "Lunch", null);
        loginViewModel.getState().setUsername("");
        controller.addMeal(2, "Dinner", LocalDate.now());

        assertTrue(addedEvents.isEmpty());
    }

    @Test
    void updatingMealRemovesOldEventAndAddsUpdatedEvent() {
        calendarViewModel.getState().setCalendarEvents(List.of(
                new CalendarEventDisplayData(
                        "meal-9", "amir", "Meal: Old", "GitBuff meal ID: 9",
                        LocalDate.of(2026, 8, 1))));

        controller.updateMeal(9, "New", LocalDate.of(2026, 8, 2));

        assertEquals(1, removedEvents.size());
        assertEquals("meal-9", removedEvents.get(0).getEventId());
        assertEquals(1, addedEvents.size());
        assertEquals("Meal: New", addedEvents.get(0).getTitle());
    }

    @Test
    void removingMissingMealLoadsCalendarBeforeGivingUp() {
        controller.removeMeal(99);

        assertEquals(1, loadRequests.size());
        assertTrue(removedEvents.isEmpty());
    }

    @Test
    void synchronizationRemovesStaleMealsAndIgnoresInvalidMeals() {
        calendarViewModel.getState().setCalendarEvents(List.of(
                new CalendarEventDisplayData(
                        "stale", "amir", "Meal: Old", "GitBuff meal ID: 3",
                        LocalDate.of(2026, 8, 1)),
                new CalendarEventDisplayData(
                        "other", "amir", "Personal", null,
                        LocalDate.of(2026, 8, 1))));
        final MealDisplayData noDate = new MealDisplayData(4, null, "Invalid", List.of());
        final List<MealDisplayData> invalidMeals = new ArrayList<>();
        invalidMeals.add(null);
        invalidMeals.add(noDate);

        controller.synchronizeMeals(invalidMeals);

        assertEquals(1, removedEvents.size());
        assertEquals("stale", removedEvents.get(0).getEventId());
        assertTrue(addedEvents.isEmpty());
    }

    @Test
    void synchronizationStopsWhenCalendarHasError() {
        calendarViewModel.getState().setErrorMessage("unavailable");

        controller.synchronizeMeals(List.of(new MealDisplayData(
                1, LocalDate.now(), "Lunch", List.of())));
        controller.replaceWorkoutPlans(List.of(activeWorkout(validWorkoutDate())));

        assertTrue(addedEvents.isEmpty());
        assertTrue(removedEvents.isEmpty());
    }

    @Test
    void replacingWorkoutsKeepsMatchesAndRemovesStaleEvents() {
        final WorkoutPlanDisplayData desiredPlan = activeWorkout(validWorkoutDate());
        final LocalDate desiredDate = LocalDate.parse(
                desiredPlan.getDate() + ", " + LocalDate.now().getYear(),
                DateTimeFormatter.ofPattern("EEEE, MMM d, uuuu", Locale.ENGLISH));
        calendarViewModel.getState().setCalendarEvents(List.of(
                new CalendarEventDisplayData(
                        "matching", "amir", "Workout: Strength",
                        "GitBuff workout schedule\nLeg session", desiredDate),
                new CalendarEventDisplayData(
                        "stale", "amir", "Workout: Old",
                        "GitBuff workout schedule\nOld plan", desiredDate)));

        controller.replaceWorkoutPlans(List.of(desiredPlan));

        assertEquals(1, removedEvents.size());
        assertEquals("stale", removedEvents.get(0).getEventId());
        assertTrue(addedEvents.isEmpty());
    }

    @Test
    void invalidWorkoutDatesAndNullPlansAreIgnored() {
        final WorkoutPlanDisplayData invalidDate = activeWorkout("not a date");

        controller.replaceWorkoutPlans(List.of(invalidDate));
        controller.replaceWorkoutPlans(null);

        assertTrue(addedEvents.isEmpty());
    }

    private WorkoutPlanDisplayData activeWorkout(String date) {
        final RecommendedExerciseDisplayData exercise = new RecommendedExerciseDisplayData(
                "Squat", 3, 10, 5, "Legs", "Barbell", "Lower with control", "");
        return new WorkoutPlanDisplayData(
                date, "Strength", "Leg session", 30, 300, 10, 30, List.of(exercise));
    }

    private String validWorkoutDate() {
        return LocalDate.now().plusDays(2).format(
                DateTimeFormatter.ofPattern("EEEE, MMM d", Locale.ENGLISH));
    }
}
