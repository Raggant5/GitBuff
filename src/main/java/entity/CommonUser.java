package entity;

/**
 * A simple implementation of the User interface.
 */
public class CommonUser implements User {

    private final String name;
    private final String password;
    private float weight;
    private float height;
    private ActivityLevel activityLevel;
    private FitnessGoal goal;
    private String profilePicturePath;

    public CommonUser(String name, String password) {
        this.name = name;
        this.password = password;
        this.height = 0.0f;
        this.weight = 0.0f;
        this.activityLevel = ActivityLevel.MODERATELY_ACTIVE;
        this.goal = FitnessGoal.MAINTAIN;
        this.profilePicturePath = null;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public void setWeight(float weight) { this.weight = weight; }

    @Override
    public float getWeight() { return this.weight; }

    @Override
    public void setHeight(float height) { this.height = height; }

    @Override
    public float getHeight() { return this.height; }

    @Override
    public void setActivityLevel(ActivityLevel activityLevel) { this.activityLevel = activityLevel; }

    @Override
    public ActivityLevel getActivityLevel() { return this.activityLevel; }

    @Override
    public void setGoal(FitnessGoal goal) { this.goal = goal; }

    @Override
    public FitnessGoal getGoal() { return this.goal; }

    @Override
    public void setProfilePicturePath(String profilePicturePath) { this.profilePicturePath = profilePicturePath; }

    @Override
    public String getProfilePicturePath() { return this.profilePicturePath; }

}
