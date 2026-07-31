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

    /**
     * Constructs a LoginOutputData instance.
     *
     * @param username user username
     * @param height user height in meters
     * @param weight user weight in kilograms
     * @param activityLevel user activity level
     * @param goal user fitness goal
     * @param profilePicturePath path to user profile image
     * @param useCaseFailed execution failure flag
     */
    public LoginOutputData(final String username,
                           final float height,
                           final float weight,
                           final ActivityLevel activityLevel,
                           final FitnessGoal goal,
                           final String profilePicturePath,
                           final boolean useCaseFailed) {
        this.username = username;
        this.height = height;
        this.weight = weight;
        this.activityLevel = activityLevel;
        this.goal = goal;
        this.profilePicturePath = profilePicturePath;
        this.useCaseFailed = useCaseFailed;
    }

    public String getUsername() {
        return this.username;
    }

    public float getHeight() {
        return this.height;
    }

    public float getWeight() {
        return this.weight;
    }

    public ActivityLevel getActivityLevel() {
        return this.activityLevel;
    }

    public FitnessGoal getGoal() {
        return this.goal;
    }

    public String getProfilePicturePath() {
        return this.profilePicturePath;
    }

    public boolean isUseCaseFailed() {
        return this.useCaseFailed;
    }
}