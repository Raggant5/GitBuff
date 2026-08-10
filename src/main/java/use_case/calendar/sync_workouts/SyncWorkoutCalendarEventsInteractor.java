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

    public SyncWorkoutCalendarEventsInteractor(final CalendarEventDataAccessInterface calendarDataAccessObject,
                                               final SyncWorkoutCalendarEventsOutputBoundary presenter) {
        this.calendarDataAccessObject = calendarDataAccessObject;
        this.presenter = presenter;
    }

    @Override
    public void execute(final SyncWorkoutCalendarEventsInputData inputData) {
        final String userId = inputData.getUserId();

        final List<ScheduledWorkout> desiredWorkouts = new ArrayList<>();
        if (inputData.getWorkoutPlans() != null) {
            for (final WorkoutPlanData plan : inputData.getWorkoutPlans()) {
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
        }

        final List<CalendarEvent> currentEvents = this.calendarDataAccessObject.getUserEvents(userId);
        for (final CalendarEvent event : currentEvents) {
            if (event.getDescription() != null && event.getDescription().contains(WORKOUT_REFERENCE)) {
                final ScheduledWorkout match = findMatchingWorkout(desiredWorkouts, event);
                if (match == null) {
                    this.calendarDataAccessObject.removeCalendarEvent(userId, event.getEventId());
                }
                else {
                    desiredWorkouts.remove(match);
                }
            }
        }

        for (final ScheduledWorkout workout : desiredWorkouts) {
            this.calendarDataAccessObject.addCalendarEvent(
                    userId, workout.title(), workout.description(), workout.date());
        }

        final List<CalendarEvent> finalEvents = this.calendarDataAccessObject.getUserEvents(userId);
        final List<CalendarEventData> finalEventsData = new ArrayList<>();
        for (final CalendarEvent event : finalEvents) {
            finalEventsData.add(CalendarEventData.from(event));
        }
        this.presenter.prepareSuccessView(new SyncWorkoutCalendarEventsOutputData(finalEventsData));
    }

    private ScheduledWorkout findMatchingWorkout(final List<ScheduledWorkout> workouts, final CalendarEvent event) {
        for (final ScheduledWorkout workout : workouts) {
            if (workout.title().equals(event.getTitle())
                    && workout.description().equals(event.getDescription())
                    && workout.date().equals(event.getActivityDate())) {
                return workout;
            }
        }
        return null;
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

    private record ScheduledWorkout(String title, String description, LocalDate date) {
    }
}
