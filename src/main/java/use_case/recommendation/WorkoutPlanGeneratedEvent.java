package use_case.recommendation;

import java.util.List;

/**
 * Event published whenever a fresh workout plan has been generated for a user - whether
 * triggered by login, a manual refresh, or a profile edit.
 */
public class WorkoutPlanGeneratedEvent {

    private final String userId;
    private final List<WorkoutPlanData> workoutPlans;

    public WorkoutPlanGeneratedEvent(final String userId, final List<WorkoutPlanData> workoutPlans) {
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
