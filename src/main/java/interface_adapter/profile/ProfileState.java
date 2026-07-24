package interface_adapter.profile;

import entity.ActivityLevel;
import entity.FitnessGoal;

/**
 * The state for the Profile View Model.
 */
public class ProfileState {
    private String username = "";
    private String heightText = "";
    private String weightText = "";
    private ActivityLevel activityLevel = ActivityLevel.MODERATELY_ACTIVE;
    private FitnessGoal goal = FitnessGoal.MAINTAIN;
    private String profilePicturePath;
    private String profileError;
    private String saveConfirmation;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHeightText() {
        return heightText;
    }

    public void setHeightText(String heightText) {
        this.heightText = heightText;
    }

    public String getWeightText() {
        return weightText;
    }

    public void setWeightText(String weightText) {
        this.weightText = weightText;
    }

    public ActivityLevel getActivityLevel() {
        return activityLevel;
    }

    public void setActivityLevel(ActivityLevel activityLevel) {
        this.activityLevel = activityLevel;
    }

    public FitnessGoal getGoal() {
        return goal;
    }

    public void setGoal(FitnessGoal goal) {
        this.goal = goal;
    }

    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }

    public String getProfileError() {
        return profileError;
    }

    public void setProfileError(String profileError) {
        this.profileError = profileError;
    }

    public String getSaveConfirmation() {
        return saveConfirmation;
    }

    public void setSaveConfirmation(String saveConfirmation) {
        this.saveConfirmation = saveConfirmation;
    }
}
