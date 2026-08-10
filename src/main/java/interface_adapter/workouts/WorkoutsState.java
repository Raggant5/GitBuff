package interface_adapter.workouts;

import java.util.ArrayList;
import java.util.List;

/**
 * The state for Workouts View Model.
 */
public class WorkoutsState {

    private String workoutFocus = "";
    private String activityLevelDescription = "";
    private List<WorkoutPlanDisplayData> workoutPlans = new ArrayList<>();
    private String message = "";
    private boolean isLoading;
    private int preferredDurationMinutes;

    public String getWorkoutFocus() {
        return this.workoutFocus;
    }

    public void setWorkoutFocus(final String workoutFocus) {
        this.workoutFocus = workoutFocus;
    }

    public String getActivityLevelDescription() {
        return this.activityLevelDescription;
    }

    public void setActivityLevelDescription(final String activityLevelDescription) {
        this.activityLevelDescription = activityLevelDescription;
    }

    public List<WorkoutPlanDisplayData> getWorkoutPlans() {
        return this.workoutPlans;
    }

    public void setWorkoutPlans(final List<WorkoutPlanDisplayData> workoutPlans) {
        this.workoutPlans = workoutPlans;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public boolean isLoading() {
        return this.isLoading;
    }

    public void setLoading(final boolean newIsLoading) {
        this.isLoading = newIsLoading;
    }

    public int getPreferredDurationMinutes() {
        return this.preferredDurationMinutes;
    }

    public void setPreferredDurationMinutes(final int preferredDurationMinutes) {
        this.preferredDurationMinutes = preferredDurationMinutes;
    }
}
