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

    /**
     * Gets workout focus description.
     *
     * @return workout focus string.
     */
    public String getWorkoutFocus() {
        return this.workoutFocus;
    }

    /**
     * Sets workout focus description.
     *
     * @param workoutFocus workout focus string.
     */
    public void setWorkoutFocus(final String workoutFocus) {
        this.workoutFocus = workoutFocus;
    }

    /**
     * Gets activity level description.
     *
     * @return activity level description string.
     */
    public String getActivityLevelDescription() {
        return this.activityLevelDescription;
    }

    /**
     * Sets activity level description.
     *
     * @param activityLevelDescription activity level description string.
     */
    public void setActivityLevelDescription(final String activityLevelDescription) {
        this.activityLevelDescription = activityLevelDescription;
    }

    /**
     * Gets workout plans list.
     *
     * @return list of workout plan display data.
     */
    public List<WorkoutPlanDisplayData> getWorkoutPlans() {
        return this.workoutPlans;
    }

    /**
     * Sets workout plans list.
     *
     * @param workoutPlans list of workout plan display data.
     */
    public void setWorkoutPlans(final List<WorkoutPlanDisplayData> workoutPlans) {
        this.workoutPlans = workoutPlans;
    }

    /**
     * Gets status/error message.
     *
     * @return message string.
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Sets status/error message.
     *
     * @param message message string.
     */
    public void setMessage(final String message) {
        this.message = message;
    }

    /**
     * Gets loading flag.
     *
     * @return true if loading, false otherwise.
     */
    public boolean isLoading() {
        return this.isLoading;
    }

    /**
     * Sets loading flag.
     *
     * @param isLoading loading boolean flag.
     */
    public void setLoading(final boolean isLoading) {
        this.isLoading = isLoading;
    }
}

