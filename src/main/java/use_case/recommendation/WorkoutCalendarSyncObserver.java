package use_case.recommendation;

import use_case.calendar.sync_workouts.SyncWorkoutCalendarEventsInputBoundary;
import use_case.calendar.sync_workouts.SyncWorkoutCalendarEventsInputData;

/**
 * Reconciles the calendar's workout events whenever a fresh workout plan is generated, no
 * matter which of RecommendWorkoutInteractor's three callers (login cascade, manual
 * refresh, profile edit) triggered the generation.
 */
public class WorkoutCalendarSyncObserver implements WorkoutPlanObserver {

    private final SyncWorkoutCalendarEventsInputBoundary syncWorkoutCalendarEventsInteractor;

    public WorkoutCalendarSyncObserver(final SyncWorkoutCalendarEventsInputBoundary
                                               syncWorkoutCalendarEventsInteractor) {
        this.syncWorkoutCalendarEventsInteractor = syncWorkoutCalendarEventsInteractor;
    }

    @Override
    public void onWorkoutPlanGenerated(final WorkoutPlanGeneratedEvent event) {
        this.syncWorkoutCalendarEventsInteractor.execute(new SyncWorkoutCalendarEventsInputData(event.getUserId(),
                event.getWorkoutPlans()));
    }
}
