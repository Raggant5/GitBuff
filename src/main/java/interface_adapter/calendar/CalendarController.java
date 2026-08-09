package interface_adapter.calendar;

import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import entity.Meal;
import entity.WorkoutPlan;
import interface_adapter.login.LoginViewModel;
import use_case.calendar.add_event.AddCalendarEventInputBoundary;
import use_case.calendar.add_event.AddCalendarEventInputData;
import use_case.calendar.load_events.LoadCalendarEventsInputBoundary;
import use_case.calendar.load_events.LoadCalendarEventsInputData;
import use_case.calendar.remove_event.RemoveCalendarEventInputBoundary;
import use_case.calendar.remove_event.RemoveCalendarEventInputData;

/**
 * Controller for the calendar feature: keeps the user's calendar in sync with meals and
 * generated workout plans, and dispatches add/remove/load requests.
 */
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

    /**
     * Constructs a CalendarController instance.
     *
     * @param addInteractor boundary for adding calendar events.
     * @param removeInteractor boundary for removing calendar events.
     * @param loadInteractor boundary for loading calendar events.
     * @param loginViewModel view model for obtaining logged in user session.
     * @param calendarViewModel view model managing calendar display state.
     */
    public CalendarController(
            final AddCalendarEventInputBoundary addInteractor,
            final RemoveCalendarEventInputBoundary removeInteractor,
            final LoadCalendarEventsInputBoundary loadInteractor,
            final LoginViewModel loginViewModel,
            final CalendarViewModel calendarViewModel) {
        this.addInteractor = addInteractor;
        this.removeInteractor = removeInteractor;
        this.loadInteractor = loadInteractor;
        this.loginViewModel = loginViewModel;
        this.calendarViewModel = calendarViewModel;
    }

    /**
     * Dispatches request to load calendar events for the active user.
     */
    public void loadCalendarEvents() {
        final String userId = getCurrentUserId();
        if (userId == null) {
            return;
        }

        final LoadCalendarEventsInputData inputData = new LoadCalendarEventsInputData(userId);
        this.loadInteractor.loadCalendarEvents(inputData);
    }

    /**
     * Adds a saved meal to the current user's Google Calendar.
     *
     * @param mealId id of the saved meal.
     * @param name meal name.
     * @param date meal date.
     */
    public void addMeal(final int mealId, final String name, final LocalDate date) {
        if (date == null) {
            return;
        }

        addEvent("Meal: " + name, MEAL_REFERENCE_PREFIX + mealId, date);
    }

    /**
     * Replaces a meal's calendar event with its current values.
     *
     * @param mealId id of the edited meal.
     * @param name updated meal name.
     * @param date updated meal date.
     */
    public void updateMeal(final int mealId, final String name, final LocalDate date) {
        removeMeal(mealId);
        addMeal(mealId, name, date);
    }

    /**
     * Removes the calendar event associated with a saved meal.
     *
     * @param mealId id of the meal being deleted.
     */
    public void removeMeal(final int mealId) {
        removeEventWithDescription(MEAL_REFERENCE_PREFIX + mealId);
    }

    /**
     * Makes the user's Google Calendar meal events match the meals saved in GitBuff.
     *
     * @param meals meals currently saved for the logged-in user.
     */
    public void synchronizeMeals(final List<Meal> meals) {
        if (this.calendarViewModel.getState().getErrorMessage() != null) {
            return;
        }

        final List<Meal> desiredMeals = new ArrayList<>();
        if (meals != null) {
            for (final Meal meal : meals) {
                if (meal != null && meal.getId() != null && meal.getDate() != null) {
                    desiredMeals.add(meal);
                }
            }
        }

        final List<CalendarEventDisplayData> currentEvents =
                List.copyOf(this.calendarViewModel.getState().getCalendarEvents());
        for (final CalendarEventDisplayData event : currentEvents) {
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

        for (final Meal meal : desiredMeals) {
            addMeal(meal.getId(), meal.getName(), meal.getDate());
        }
    }

    /**
     * Replaces the user's generated workout events with a new workout plan.
     *
     * @param workoutPlans generated workout plans.
     */
    public void replaceWorkoutPlans(final List<WorkoutPlan> workoutPlans) {
        if (this.calendarViewModel.getState().getErrorMessage() != null) {
            return;
        }

        final List<CalendarEventDisplayData> currentEvents =
                List.copyOf(this.calendarViewModel.getState().getCalendarEvents());
        final List<ScheduledWorkout> desiredWorkouts = new ArrayList<>();

        if (workoutPlans != null) {
            for (final WorkoutPlan plan : workoutPlans) {
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

        for (final CalendarEventDisplayData event : currentEvents) {
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

        for (final ScheduledWorkout workout : desiredWorkouts) {
            addEvent(workout.title(), workout.description(), workout.date());
        }
    }

    private ScheduledWorkout findMatchingWorkout(
            final List<ScheduledWorkout> workouts, final CalendarEventDisplayData event) {
        for (final ScheduledWorkout workout : workouts) {
            if (workout.title().equals(event.getTitle())
                    && workout.description().equals(event.getDescription())
                    && workout.date().equals(event.getActivityDate())) {
                return workout;
            }
        }
        return null;
    }

    private Meal findMatchingMeal(final List<Meal> meals, final CalendarEventDisplayData event) {
        for (final Meal meal : meals) {
            if (("Meal: " + meal.getName()).equals(event.getTitle())
                    && (MEAL_REFERENCE_PREFIX + meal.getId()).equals(event.getDescription())
                    && meal.getDate().equals(event.getActivityDate())) {
                return meal;
            }
        }
        return null;
    }

    private void addEvent(final String title, final String description, final LocalDate date) {
        final String userId = getCurrentUserId();
        if (userId == null || date == null) {
            return;
        }

        this.addInteractor.addCalendarEvent(new AddCalendarEventInputData(
                userId, title, description, date));
    }

    private void removeEventWithDescription(final String description) {
        CalendarEventDisplayData matchingEvent = findEventWithDescription(description);
        if (matchingEvent == null) {
            loadCalendarEvents();
            matchingEvent = findEventWithDescription(description);
        }

        if (matchingEvent != null) {
            removeEvent(matchingEvent);
        }
    }

    private CalendarEventDisplayData findEventWithDescription(final String description) {
        for (final CalendarEventDisplayData event : this.calendarViewModel.getState().getCalendarEvents()) {
            if (description.equals(event.getDescription())) {
                return event;
            }
        }
        return null;
    }

    private void removeEvent(final CalendarEventDisplayData event) {
        this.removeInteractor.removeCalendarEvent(new RemoveCalendarEventInputData(
                event.getUserId(), event.getEventId()));
    }

    private String getCurrentUserId() {
        final String userId = this.loginViewModel.getState().getUsername();
        if (userId == null || userId.isBlank()) {
            return null;
        }
        return userId;
    }

    private LocalDate parseWorkoutDate(final String dateText) {
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
        catch (final DateTimeParseException exception) {
            return null;
        }
    }

    private record ScheduledWorkout(
            String title, String description, LocalDate date) {
    }
}

