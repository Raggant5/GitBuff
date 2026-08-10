package use_case.recommendation;

import java.util.List;

/**
 * Output Data for the Recommend Workout Plan Use Case.
 */
public class RecommendWorkoutPlanOutputData {

    private final String workoutFocus;
    private final String activityLevelDescription;
    private final List<WorkoutPlanData> workoutPlans;

    public RecommendWorkoutPlanOutputData(final String workoutFocus, final String activityLevelDescription,
                                          final List<WorkoutPlanData> workoutPlans) {
        this.workoutFocus = workoutFocus;
        this.activityLevelDescription = activityLevelDescription;
        this.workoutPlans = workoutPlans;
    }

    public String getWorkoutFocus() {
        return this.workoutFocus;
    }

    public String getActivityLevelDescription() {
        return this.activityLevelDescription;
    }

    public List<WorkoutPlanData> getWorkoutPlans() {
        return this.workoutPlans;
    }
}
