package use_case.profile;

import entity.ActivityLevel;
import entity.FitnessGoal;

/**
 * The Input Data for the Edit Profile Use Case.
 */
public class EditProfileInputData {

    private final float height;
    private final float weight;
    private final ActivityLevel activityLevel;
    private final FitnessGoal goal;
    private final String profilePicturePath;

    public EditProfileInputData(float height, float weight, ActivityLevel activityLevel,
                                 FitnessGoal goal, String profilePicturePath) {
        this.height = height;
        this.weight = weight;
        this.activityLevel = activityLevel;
        this.goal = goal;
        this.profilePicturePath = profilePicturePath;
    }

    float getHeight() {
        return height;
    }

    float getWeight() {
        return weight;
    }

    ActivityLevel getActivityLevel() {
        return activityLevel;
    }

    FitnessGoal getGoal() {
        return goal;
    }

    String getProfilePicturePath() {
        return profilePicturePath;
    }
}
