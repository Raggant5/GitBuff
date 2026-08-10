package interface_adapter.calendar;

import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import interface_adapter.login.LoginViewModel;
import interface_adapter.nutrition.meal.MealDisplayData;
import interface_adapter.workouts.WorkoutPlanDisplayData;
import use_case.calendar.add_event.AddCalendarEventInputBoundary;
import use_case.calendar.add_event.AddCalendarEventInputData;
import use_case.calendar.load_events.LoadCalendarEventsInputBoundary;
import use_case.calendar.load_events.LoadCalendarEventsInputData;
import use_case.calendar.remove_event.RemoveCalendarEventInputBoundary;
import use_case.calendar.remove_event.RemoveCalendarEventInputData;

/**
 * Controller for the calendar feature: keeps the user's calendar in sync with meals and
 * generated workout plans, and dispatches add/remove/load requests.
 *
 * <p>Reads and compares against {@link CalendarEventDisplayData} (the state's display DTO)
 * rather than the {@code entity.CalendarEvent} entity, and accepts
 * {@link MealDisplayData}/{@link WorkoutPlanDisplayData} - not {@code entity.Meal}/
 * {@code entity.WorkoutPlan} - from the presenters that call
 * {@link #synchronizeMeals(List)}/{@link #replaceWorkoutPlans(List)}, keeping this
 * interface_adapter-layer class entirely free of entity-layer dependencies.
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
     * @param mealId id of the saved meal
     * @param name meal name
     * @param date meal date
     */
    public void addMeal(int mealId, String name, LocalDate date) {
        if (date == null) {
            return;
        }

        addEvent("Meal: " + name, MEAL_REFERENCE_PREFIX + mealId, date);
    }

    /**
     * Replaces a meal's calendar event with its current values.
     *
     * @param mealId id of the edited meal
     * @param name updated meal name
     * @param date updated meal date
     */
    public void updateMeal(int mealId, String name, LocalDate date) {
        removeMeal(mealId);
        addMeal(mealId, name, date);
    }

    /**
     * Removes the calendar event associated with a saved meal.
     *
     * @param mealId id of the meal being deleted
     */
    public void removeMeal(int mealId) {
        removeEventWithDescription(MEAL_REFERENCE_PREFIX + mealId);
    }

    /**
     * Makes the user's Google Calendar meal events match the meals saved in GitBuff.
     * This also backfills meals that existed before calendar integration was enabled.
     *
     * @param meals meals currently saved for the logged-in user
     */
    public void synchronizeMeals(List<MealDisplayData> meals) {
        if (calendarViewModel.getState().getErrorMessage() != null) {
            return;
        }

        final List<MealDisplayData> desiredMeals = new ArrayList<>();
        if (meals != null) {
            for (MealDisplayData meal : meals) {
                if (meal != null && meal.getDate() != null) {
                    desiredMeals.add(meal);
                }
            }
        }

        final List<CalendarEventDisplayData> currentEvents =
                List.copyOf(calendarViewModel.getState().getCalendarEvents());
        for (CalendarEventDisplayData event : currentEvents) {
            if (event.getDescription() != null
                    && event.getDescription().startsWith(MEAL_REFERENCE_PREFIX)) {
                final MealDisplayData matchingMeal = findMatchingMeal(desiredMeals, event);
                if (matchingMeal == null) {
                    removeEvent(event);
                }
                else {
                    desiredMeals.remove(matchingMeal);
                }
            }
        }

        for (MealDisplayData meal : desiredMeals) {
            addMeal(meal.getId(), meal.getName(), meal.getDate());
        }
    }

    /**
     * Replaces the user's generated workout events with a new workout plan.
     * Rest days are intentionally omitted from the calendar.
     *
     * @param workoutPlans generated workout plans
     */
    public void replaceWorkoutPlans(List<WorkoutPlanDisplayData> workoutPlans) {
        if (calendarViewModel.getState().getErrorMessage() != null) {
            return;
        }

        final List<CalendarEventDisplayData> currentEvents =
                List.copyOf(calendarViewModel.getState().getCalendarEvents());
        final List<ScheduledWorkout> desiredWorkouts = new ArrayList<>();

        if (workoutPlans != null) {
            for (WorkoutPlanDisplayData plan : workoutPlans) {
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

        for (CalendarEventDisplayData event : currentEvents) {
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
            List<ScheduledWorkout> workouts, CalendarEventDisplayData event) {
        for (ScheduledWorkout workout : workouts) {
            if (workout.title().equals(event.getTitle())
                    && workout.description().equals(event.getDescription())
                    && workout.date().equals(event.getActivityDate())) {
                return workout;
            }
        }
        return null;
    }

    private MealDisplayData findMatchingMeal(List<MealDisplayData> meals, CalendarEventDisplayData event) {
        for (MealDisplayData meal : meals) {
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
        CalendarEventDisplayData matchingEvent = findEventWithDescription(description);
        if (matchingEvent == null) {
            loadCalendarEvents();
            matchingEvent = findEventWithDescription(description);
        }

        if (matchingEvent != null) {
            removeEvent(matchingEvent);
        }
    }

    private CalendarEventDisplayData findEventWithDescription(String description) {
        for (CalendarEventDisplayData event : calendarViewModel.getState().getCalendarEvents()) {
            if (description.equals(event.getDescription())) {
                return event;
            }
        }
        return null;
    }

    private void removeEvent(CalendarEventDisplayData event) {
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


