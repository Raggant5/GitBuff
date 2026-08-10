package use_case.calendar.sync_workouts;

import java.util.List;

import use_case.recommendation.WorkoutPlanData;

/**
 * Input Data for the Sync Workout Calendar Events Use Case.
 */
public class SyncWorkoutCalendarEventsInputData {

    private final String userId;
    private final List<WorkoutPlanData> workoutPlans;

    public SyncWorkoutCalendarEventsInputData(final String userId, final List<WorkoutPlanData> workoutPlans) {
        this.userId = userId;
        this.workoutPlans = workoutPlans;
    }

    public String getUserId() {
        return this.userId;
    }

    public List<WorkoutPlanData> getWorkoutPlans() {
        return this.workoutPlans;
    }
}
