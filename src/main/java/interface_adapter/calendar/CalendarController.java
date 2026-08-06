package interface_adapter.calendar;

import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import entity.CalendarEvent;
import entity.Meal;
import entity.WorkoutPlan;
import interface_adapter.login.LoginViewModel;
import use_case.calendar.add_event.AddCalendarEventInputBoundary;
import use_case.calendar.add_event.AddCalendarEventInputData;
import use_case.calendar.load_events.LoadCalendarEventsInputBoundary;
import use_case.calendar.load_events.LoadCalendarEventsInputData;
import use_case.calendar.remove_event.RemoveCalendarEventInputBoundary;
import use_case.calendar.remove_event.RemoveCalendarEventInputData;

public class CalendarController {
    private static final String MEAL_REFERENCE_PREFIX = "GitBuff meal ID: ";
    private static final String WORKOUT_REFERENCE = "GitBuff workout schedule";
    private static final DateTimeFormatter WORKOUT_DATE_FORMAT =
            DateTimeFormatter.ofPattern("EEEE, MMM d, uuuu", Locale.ENGLISH);

    private final AddCalendarEventInputBoundary addInteractor;
    private final RemoveCalendarEventInputBoundary removeInteractor;
    private final LoadCalendarEventsInputBoundary loadInteractor;
    private final LoginViewModel loginViewModel;
    private final CalendarViewModel calendarViewModel;

    public CalendarController(
            AddCalendarEventInputBoundary addInteractor,
            RemoveCalendarEventInputBoundary removeInteractor,
            LoadCalendarEventsInputBoundary loadInteractor,
            LoginViewModel loginViewModel,
            CalendarViewModel calendarViewModel) {
        this.addInteractor = addInteractor;
        this.removeInteractor = removeInteractor;
        this.loadInteractor = loadInteractor;
        this.loginViewModel = loginViewModel;
        this.calendarViewModel = calendarViewModel;
    }

    public void loadCalendarEvents() {
        final String userId = getCurrentUserId();
        if (userId == null) {
            return;
        }

        final LoadCalendarEventsInputData inputData = new LoadCalendarEventsInputData(userId);
        loadInteractor.loadCalendarEvents(inputData);
    }

    /**
     * Adds a saved meal to the current user's Google Calendar.
     *
     * @param meal saved meal
     */
    public void addMeal(Meal meal) {
        if (meal == null || meal.getId() == null) {
            return;
        }

        addEvent(
                "Meal: " + meal.getName(),
                MEAL_REFERENCE_PREFIX + meal.getId(),
                meal.getDate());
    }

    /**
     * Replaces a meal's calendar event with its current values.
     *
     * @param meal edited meal
     */
    public void updateMeal(Meal meal) {
        removeMeal(meal);
        addMeal(meal);
    }

    /**
     * Removes the calendar event associated with a saved meal.
     *
     * @param meal meal being deleted
     */
    public void removeMeal(Meal meal) {
        if (meal != null && meal.getId() != null) {
            removeEventWithDescription(MEAL_REFERENCE_PREFIX + meal.getId());
        }
    }

    /**
     * Makes the user's Google Calendar meal events match the meals saved in GitBuff.
     * This also backfills meals that existed before calendar integration was enabled.
     *
     * @param meals meals currently saved for the logged-in user
     */
    public void synchronizeMeals(List<Meal> meals) {
        if (calendarViewModel.getState().getErrorMessage() != null) {
            return;
        }

        final List<Meal> desiredMeals = new ArrayList<>();
        if (meals != null) {
            for (Meal meal : meals) {
                if (meal != null && meal.getId() != null && meal.getDate() != null) {
                    desiredMeals.add(meal);
                }
            }
        }

        final List<CalendarEvent> currentEvents =
                List.copyOf(calendarViewModel.getState().getCalendarEvents());
        for (CalendarEvent event : currentEvents) {
            if (event.getDescription() != null
                    && event.getDescription().startsWith(MEAL_REFERENCE_PREFIX)) {
                final Meal matchingMeal = findMatchingMeal(desiredMeals, event);
                if (matchingMeal == null) {
                    removeEvent(event);
                }
                else {
                    desiredMeals.remove(matchingMeal);
                }
            }
        }

        for (Meal meal : desiredMeals) {
            addMeal(meal);
        }
    }

    /**
     * Replaces the user's generated workout events with a new workout plan.
     * Rest days are intentionally omitted from the calendar.
     *
     * @param workoutPlans generated workout plans
     */
    public void replaceWorkoutPlans(List<WorkoutPlan> workoutPlans) {
        if (calendarViewModel.getState().getErrorMessage() != null) {
            return;
        }

        final List<CalendarEvent> currentEvents =
                List.copyOf(calendarViewModel.getState().getCalendarEvents());
        final List<ScheduledWorkout> desiredWorkouts = new ArrayList<>();

        if (workoutPlans != null) {
            for (WorkoutPlan plan : workoutPlans) {
                if (plan != null && plan.getExercises() != null
                        && !plan.getExercises().isEmpty()) {
                    final LocalDate date = parseWorkoutDate(plan.getDate());
                    if (date != null) {
                        desiredWorkouts.add(new ScheduledWorkout(
                                "Workout: " + plan.getTitle(),
                                WORKOUT_REFERENCE + "\n" + plan.getDescription(),
                                date));
                    }
                }
            }
        }

        for (CalendarEvent event : currentEvents) {
            if (event.getDescription() != null
                    && event.getDescription().contains(WORKOUT_REFERENCE)) {
                final ScheduledWorkout match = findMatchingWorkout(
                        desiredWorkouts, event);
                if (match == null) {
                    removeEvent(event);
                }
                else {
                    desiredWorkouts.remove(match);
                }
            }
        }

        for (ScheduledWorkout workout : desiredWorkouts) {
            addEvent(workout.title(), workout.description(), workout.date());
        }
    }

    private ScheduledWorkout findMatchingWorkout(
            List<ScheduledWorkout> workouts, CalendarEvent event) {
        for (ScheduledWorkout workout : workouts) {
            if (workout.title().equals(event.getTitle())
                    && workout.description().equals(event.getDescription())
                    && workout.date().equals(event.getActivityDate())) {
                return workout;
            }
        }
        return null;
    }

    private Meal findMatchingMeal(List<Meal> meals, CalendarEvent event) {
        for (Meal meal : meals) {
            if (("Meal: " + meal.getName()).equals(event.getTitle())
                    && (MEAL_REFERENCE_PREFIX + meal.getId()).equals(event.getDescription())
                    && meal.getDate().equals(event.getActivityDate())) {
                return meal;
            }
        }
        return null;
    }

    private void addEvent(String title, String description, LocalDate date) {
        final String userId = getCurrentUserId();
        if (userId == null || date == null) {
            return;
        }

        addInteractor.addCalendarEvent(new AddCalendarEventInputData(
                userId, title, description, date));
    }

    private void removeEventWithDescription(String description) {
        CalendarEvent matchingEvent = findEventWithDescription(description);
        if (matchingEvent == null) {
            loadCalendarEvents();
            matchingEvent = findEventWithDescription(description);
        }

        if (matchingEvent != null) {
            removeEvent(matchingEvent);
        }
    }

    private CalendarEvent findEventWithDescription(String description) {
        for (CalendarEvent event : calendarViewModel.getState().getCalendarEvents()) {
            if (description.equals(event.getDescription())) {
                return event;
            }
        }
        return null;
    }

    private void removeEvent(CalendarEvent event) {
        removeInteractor.removeCalendarEvent(new RemoveCalendarEventInputData(
                event.getUserId(), event.getEventId()));
    }

    private String getCurrentUserId() {
        final String userId = loginViewModel.getState().getUsername();
        if (userId == null || userId.isBlank()) {
            return null;
        }
        return userId;
    }

    private LocalDate parseWorkoutDate(String dateText) {
        if (dateText == null || dateText.isBlank()) {
            return null;
        }

        final LocalDate today = LocalDate.now();
        try {
            LocalDate parsed = LocalDate.parse(
                    dateText + ", " + Year.now().getValue(),
                    WORKOUT_DATE_FORMAT);

            if (parsed.isBefore(today.minusDays(1))) {
                parsed = parsed.plusYears(1);
            }
            return parsed;
        }
        catch (DateTimeParseException exception) {
            return null;
        }
    }

    private record ScheduledWorkout(
            String title, String description, LocalDate date) {
    }
}
