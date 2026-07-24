package interface_adapter.workouts;

/**
 * The state for the Workouts View Model.
 */
public class WorkoutsState {
    private String workoutFocus = "";
    private String activityLevelDescription = "";
    private String message = "Visit your profile and save your details to see a personalized "
            + "workout recommendation.";

    public String getWorkoutFocus() {
        return workoutFocus;
    }

    public void setWorkoutFocus(String workoutFocus) {
        this.workoutFocus = workoutFocus;
    }

    public String getActivityLevelDescription() {
        return activityLevelDescription;
    }

    public void setActivityLevelDescription(String activityLevelDescription) {
        this.activityLevelDescription = activityLevelDescription;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
