package use_case.calendar.sync_workouts;

import java.util.List;

import use_case.recommendation.WorkoutPlanData;

/**
 * Input Data for the Sync Workout Calendar Events Use Case.
 */
public class SyncWorkoutCalendarEventsInputData {

    private final String userId;
    private final List<WorkoutPlanData> workoutPlans;

    /**
     * Creates input data for synchronizing generated workout plans.
     *
     * @param userId the GitBuff user identifier
     * @param workoutPlans generated workout plans to synchronize
     */
    public SyncWorkoutCalendarEventsInputData(final String userId, final List<WorkoutPlanData> workoutPlans) {
        this.userId = userId;
        this.workoutPlans = workoutPlans;
    }

    /**
     * Returns the GitBuff user identifier.
     *
     * @return the user identifier
     */
    public String getUserId() {
        return this.userId;
    }

    /**
     * Returns the generated workout plans.
     *
     * @return the workout plans, or {@code null} when no plans were generated
     */
    public List<WorkoutPlanData> getWorkoutPlans() {
        return this.workoutPlans;
    }
}
