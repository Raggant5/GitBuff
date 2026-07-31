package interface_adapter.workouts;

/**
 * The state for the Workouts View Model.
 */
public class WorkoutsState {

    private String workoutFocus = "";
    private String activityLevelDescription = "";
    private String aiWorkoutPlan = "";
    private String message = "Visit your profile and save your details to see a personalized "
            + "workout recommendation.";

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

    public String getAiWorkoutPlan() {
        return this.aiWorkoutPlan;
    }

    public void setAiWorkoutPlan(final String aiWorkoutPlan) {
        this.aiWorkoutPlan = aiWorkoutPlan;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }
}
