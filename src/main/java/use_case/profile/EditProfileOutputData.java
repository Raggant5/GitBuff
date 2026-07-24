package use_case.profile;

import entity.ActivityLevel;
import entity.FitnessGoal;

/**
 * Output Data for the Edit Profile Use Case.
 */
public class EditProfileOutputData {

    private final String username;
    private final float height;
    private final float weight;
    private final ActivityLevel activityLevel;
    private final FitnessGoal goal;
    private final String profilePicturePath;

    public EditProfileOutputData(String username, float height, float weight, ActivityLevel activityLevel,
                                  FitnessGoal goal, String profilePicturePath) {
        this.username = username;
        this.height = height;
        this.weight = weight;
        this.activityLevel = activityLevel;
        this.goal = goal;
        this.profilePicturePath = profilePicturePath;
    }

    public String getUsername() {
        return username;
    }

    public float getHeight() {
        return height;
    }

    public float getWeight() {
        return weight;
    }

    public ActivityLevel getActivityLevel() {
        return activityLevel;
    }

    public FitnessGoal getGoal() {
        return goal;
    }

    public String getProfilePicturePath() {
        return profilePicturePath;
    }
}
