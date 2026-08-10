package use_case.calendar.sync_workouts;

import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import entity.CalendarEvent;
import use_case.calendar.CalendarEventData;
import use_case.calendar.CalendarEventDataAccessInterface;
import use_case.recommendation.WorkoutPlanData;

/**
 * Reconciles the user's calendar so its workout events match a freshly generated workout plan.
 * Rest days are intentionally omitted from the calendar.
 */
public class SyncWorkoutCalendarEventsInteractor implements SyncWorkoutCalendarEventsInputBoundary {

    private static final String WORKOUT_REFERENCE = "GitBuff workout schedule";
    private static final DateTimeFormatter WORKOUT_DATE_FORMAT =
            DateTimeFormatter.ofPattern("EEEE, MMM d, uuuu", Locale.ENGLISH);

    private final CalendarEventDataAccessInterface calendarDataAccessObject;
    private final SyncWorkoutCalendarEventsOutputBoundary presenter;

    /**
     * Creates an interactor that reconciles generated workouts with calendar events.
     *
     * @param calendarDataAccessObject gateway for calendar operations
     * @param presenter output boundary receiving the synchronized calendar
     */
    public SyncWorkoutCalendarEventsInteractor(final CalendarEventDataAccessInterface calendarDataAccessObject,
                                               final SyncWorkoutCalendarEventsOutputBoundary presenter) {
        this.calendarDataAccessObject = calendarDataAccessObject;
        this.presenter = presenter;
    }

    /**
     * Synchronizes all scheduled workouts belonging to the requested user.
     *
     * @param inputData identifies the user and the generated workout plans
     */
    @Override
    public void execute(final SyncWorkoutCalendarEventsInputData inputData) {
        try {
            synchronizeWorkouts(inputData);
        }
        catch (final IllegalStateException exception) {
            this.presenter.prepareFailureView(exception.getMessage());
        }
    }

    /**
     * Performs the reconciliation of generated workouts and calendar events.
     *
     * @param inputData identifies the user and the generated workout plans
     */
    private void synchronizeWorkouts(final SyncWorkoutCalendarEventsInputData inputData) {
        final String userId = inputData.getUserId();
        final List<ScheduledWorkout> desiredWorkouts = getDesiredWorkouts(inputData.getWorkoutPlans());

        reconcileExistingEvents(userId, desiredWorkouts);
        addMissingEvents(userId, desiredWorkouts);

        final List<CalendarEvent> finalEvents = this.calendarDataAccessObject.getUserEvents(userId);
        final List<CalendarEventData> finalEventsData = new ArrayList<>();
        for (final CalendarEvent event : finalEvents) {
            finalEventsData.add(CalendarEventData.from(event));
        }
        this.presenter.prepareSuccessView(new SyncWorkoutCalendarEventsOutputData(finalEventsData));
    }

    /**
     * Converts workout plan data into the dated workouts that belong on the calendar.
     *
     * @param workoutPlans generated workout plans
     * @return scheduled, non-rest workouts with valid dates
     */
    private List<ScheduledWorkout> getDesiredWorkouts(final List<WorkoutPlanData> workoutPlans) {
        final List<ScheduledWorkout> desiredWorkouts = new ArrayList<>();
        if (workoutPlans != null) {
            for (final WorkoutPlanData plan : workoutPlans) {
                addScheduledWorkout(plan, desiredWorkouts);
            }
        }
        return desiredWorkouts;
    }

    /**
     * Adds one plan to the desired schedule when it represents a dated workout.
     *
     * @param plan generated workout plan
     * @param desiredWorkouts destination for valid scheduled workouts
     */
    private void addScheduledWorkout(final WorkoutPlanData plan,
                                     final List<ScheduledWorkout> desiredWorkouts) {
        if (plan.getExercises() != null && !plan.getExercises().isEmpty()) {
            final LocalDate date = parseWorkoutDate(plan.getDate());
            if (date != null) {
                desiredWorkouts.add(new ScheduledWorkout(
                        "Workout: " + plan.getTitle(),
                        WORKOUT_REFERENCE + "\n" + plan.getDescription(),
                        date));
            }
        }
    }

    /**
     * Removes obsolete workout events and consumes desired workouts already represented.
     *
     * @param userId the GitBuff user identifier
     * @param desiredWorkouts mutable list of desired scheduled workouts
     */
    private void reconcileExistingEvents(final String userId,
                                         final List<ScheduledWorkout> desiredWorkouts) {
        final List<CalendarEvent> currentEvents = this.calendarDataAccessObject.getUserEvents(userId);
        for (final CalendarEvent event : currentEvents) {
            if (isGitBuffWorkout(event)) {
                reconcileEvent(userId, desiredWorkouts, event);
            }
        }
    }

    /**
     * Determines whether an event was created by GitBuff's workout synchronization.
     *
     * @param event calendar event to inspect
     * @return {@code true} when this is a GitBuff workout event
     */
    private boolean isGitBuffWorkout(final CalendarEvent event) {
        return event.getDescription() != null && event.getDescription().contains(WORKOUT_REFERENCE);
    }

    /**
     * Reconciles one existing workout event with the desired schedule.
     *
     * @param userId the GitBuff user identifier
     * @param desiredWorkouts mutable list of desired scheduled workouts
     * @param event existing calendar event
     */
    private void reconcileEvent(final String userId,
                                final List<ScheduledWorkout> desiredWorkouts,
                                final CalendarEvent event) {
        final ScheduledWorkout match = findMatchingWorkout(desiredWorkouts, event);
        if (match == null) {
            this.calendarDataAccessObject.removeCalendarEvent(userId, event.getEventId());
        }
        else {
            desiredWorkouts.remove(match);
        }
    }

    /**
     * Creates calendar events for desired workouts that were not already present.
     *
     * @param userId the GitBuff user identifier
     * @param desiredWorkouts workouts still missing from the calendar
     */
    private void addMissingEvents(final String userId,
                                  final List<ScheduledWorkout> desiredWorkouts) {
        for (final ScheduledWorkout workout : desiredWorkouts) {
            this.calendarDataAccessObject.addCalendarEvent(
                    userId, workout.title(), workout.description(), workout.date());
        }
    }

    /**
     * Finds the scheduled workout represented by an existing calendar event.
     *
     * @param workouts candidate scheduled workouts
     * @param event existing calendar event
     * @return the matching workout, or {@code null} when none exists
     */
    private ScheduledWorkout findMatchingWorkout(final List<ScheduledWorkout> workouts, final CalendarEvent event) {
        ScheduledWorkout result = null;
        boolean found = false;
        for (final ScheduledWorkout workout : workouts) {
            if (!found && workout.title().equals(event.getTitle())
                    && workout.description().equals(event.getDescription())
                    && workout.date().equals(event.getActivityDate())) {
                result = workout;
                found = true;
            }
        }
        return result;
    }

    /**
     * Parses the display date on a generated workout, rolling past dates into next year.
     *
     * @param dateText workout date without a year
     * @return the resolved date, or {@code null} for blank or malformed text
     */
    private LocalDate parseWorkoutDate(final String dateText) {
        LocalDate result = null;
        if (dateText != null && !dateText.isBlank()) {
            final LocalDate today = LocalDate.now();
            try {
                LocalDate parsed = LocalDate.parse(
                        dateText + ", " + Year.now().getValue(),
                        WORKOUT_DATE_FORMAT);

                if (parsed.isBefore(today.minusDays(1))) {
                    parsed = parsed.plusYears(1);
                }
                result = parsed;
            }
            catch (final DateTimeParseException exception) {
                result = null;
            }
        }
        return result;
    }

    private record ScheduledWorkout(String title, String description, LocalDate date) {
    }
}
