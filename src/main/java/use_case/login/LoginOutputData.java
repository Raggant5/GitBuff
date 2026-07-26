package use_case.login;

import entity.ActivityLevel;
import entity.FitnessGoal;

/**
 * Output Data for the Login Use Case.
 */
public class LoginOutputData {

    private final String username;
    private final float height;
    private final float weight;
    private final ActivityLevel activityLevel;
    private final FitnessGoal goal;
    private final String profilePicturePath;
    private final boolean useCaseFailed;

    public LoginOutputData(
            String username,
            float height,
            float weight,
            ActivityLevel activityLevel,
            FitnessGoal goal,
            String profilePicturePath,
            boolean useCaseFailed) {

        this.username = username;
        this.height = height;
        this.weight = weight;
        this.activityLevel = activityLevel;
        this.goal = goal;
        this.profilePicturePath = profilePicturePath;
        this.useCaseFailed = useCaseFailed;
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

    public boolean isUseCaseFailed() {
        return useCaseFailed;
    }
}