package interface_adapter.calendar;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import interface_adapter.login.LoginViewModel;
import interface_adapter.nutrition.meal.MealDisplayData;
import interface_adapter.workouts.RecommendedExerciseDisplayData;
import interface_adapter.workouts.WorkoutPlanDisplayData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.calendar.add_event.AddCalendarEventInputBoundary;
import use_case.calendar.add_event.AddCalendarEventInputData;
import use_case.calendar.load_events.LoadCalendarEventsInputBoundary;
import use_case.calendar.remove_event.RemoveCalendarEventInputBoundary;
import use_case.calendar.remove_event.RemoveCalendarEventInputData;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalendarControllerTest {
    private final List<AddCalendarEventInputData> addedEvents =
            new ArrayList<>();
    private final List<RemoveCalendarEventInputData> removedEvents =
            new ArrayList<>();
    private CalendarViewModel calendarViewModel;
    private CalendarController controller;

    @BeforeEach
    void setUp() {
        final LoginViewModel loginViewModel = new LoginViewModel();
        loginViewModel.getState().setUsername("amir");
        calendarViewModel = new CalendarViewModel();

        final AddCalendarEventInputBoundary addBoundary = addedEvents::add;
        final RemoveCalendarEventInputBoundary removeBoundary = removedEvents::add;
        final LoadCalendarEventsInputBoundary loadBoundary = inputData -> { };

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
        assertEquals("google-event-4", removedEvents.get(0).getEventID());
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
}

