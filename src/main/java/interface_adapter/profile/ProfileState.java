package interface_adapter.profile;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import entity.ActivityLevel;
import entity.DietaryRestriction;
import entity.Equipment;
import entity.FitnessGoal;
import entity.Gender;
import entity.PrivacySetting;
import entity.UnitSystem;

/**
 * The state for the Profile View Model.
 */
public class ProfileState {

    private static final int DEFAULT_DURATION = 45;

    private String username = "";
    private String heightText = "";
    private String weightText = "";
    private ActivityLevel activityLevel = ActivityLevel.MODERATELY_ACTIVE;
    private FitnessGoal goal = FitnessGoal.MAINTAIN_GENERAL_FITNESS;
    private String profilePicturePath;

    private LocalDate dateOfBirth;
    private Gender gender = Gender.PREFER_NOT_TO_SAY;
    private String bio = "";
    private UnitSystem preferredUnitSystem = UnitSystem.METRIC;
    private Set<Equipment> equipment = new HashSet<>();
    private Set<DietaryRestriction> dietaryRestrictions = new HashSet<>();
    private Set<DayOfWeek> preferredWorkoutDays = new HashSet<>();
    private int preferredWorkoutDurationMinutes = DEFAULT_DURATION;
    private Set<PrivacySetting> privacySettings = new HashSet<>();

    private String profileError;
    private String saveConfirmation;

    /**
     * Gets the username.
     *
     * @return username string.
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Sets the username.
     *
     * @param username username string.
     */
    public void setUsername(final String username) {
        this.username = username;
    }

    /**
     * Gets the height text.
     *
     * @return height string.
     */
    public String getHeightText() {
        return this.heightText;
    }

    /**
     * Sets the height text.
     *
     * @param heightText height string.
     */
    public void setHeightText(final String heightText) {
        this.heightText = heightText;
    }

    /**
     * Gets the weight text.
     *
     * @return weight string.
     */
    public String getWeightText() {
        return this.weightText;
    }

    /**
     * Sets the weight text.
     *
     * @param weightText weight string.
     */
    public void setWeightText(final String weightText) {
        this.weightText = weightText;
    }

    /**
     * Gets the activity level.
     *
     * @return activity level.
     */
    public ActivityLevel getActivityLevel() {
        return this.activityLevel;
    }

    /**
     * Sets the activity level.
     *
     * @param activityLevel activity level.
     */
    public void setActivityLevel(final ActivityLevel activityLevel) {
        this.activityLevel = activityLevel;
    }

    /**
     * Gets the fitness goal.
     *
     * @return fitness goal.
     */
    public FitnessGoal getGoal() {
        return this.goal;
    }

    /**
     * Sets the fitness goal.
     *
     * @param goal fitness goal.
     */
    public void setGoal(final FitnessGoal goal) {
        this.goal = goal;
    }

    /**
     * Gets profile picture path.
     *
     * @return file path string.
     */
    public String getProfilePicturePath() {
        return this.profilePicturePath;
    }

    /**
     * Sets profile picture path.
     *
     * @param profilePicturePath file path string.
     */
    public void setProfilePicturePath(final String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }

    /**
     * Gets date of birth.
     *
     * @return date of birth.
     */
    public LocalDate getDateOfBirth() {
        return this.dateOfBirth;
    }

    /**
     * Sets date of birth.
     *
     * @param dateOfBirth date of birth.
     */
    public void setDateOfBirth(final LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Gets gender.
     *
     * @return gender.
     */
    public Gender getGender() {
        return this.gender;
    }

    /**
     * Sets gender.
     *
     * @param gender gender.
     */
    public void setGender(final Gender gender) {
        this.gender = gender;
    }

    /**
     * Gets bio text.
     *
     * @return bio string.
     */
    public String getBio() {
        return this.bio;
    }

    /**
     * Sets bio text.
     *
     * @param bio bio string.
     */
    public void setBio(final String bio) {
        this.bio = bio;
    }

    /**
     * Gets preferred unit system.
     *
     * @return preferred unit system.
     */
    public UnitSystem getPreferredUnitSystem() {
        return this.preferredUnitSystem;
    }

    /**
     * Sets preferred unit system.
     *
     * @param preferredUnitSystem preferred unit system.
     */
    public void setPreferredUnitSystem(final UnitSystem preferredUnitSystem) {
        this.preferredUnitSystem = preferredUnitSystem;
    }

    /**
     * Gets available equipment set.
     *
     * @return equipment set.
     */
    public Set<Equipment> getEquipment() {
        return this.equipment;
    }

    /**
     * Sets available equipment set.
     *
     * @param equipment equipment set.
     */
    public void setEquipment(final Set<Equipment> equipment) {
        this.equipment = equipment;
    }

    /**
     * Gets dietary restrictions set.
     *
     * @return dietary restrictions set.
     */
    public Set<DietaryRestriction> getDietaryRestrictions() {
        return this.dietaryRestrictions;
    }

    /**
     * Sets dietary restrictions set.
     *
     * @param dietaryRestrictions dietary restrictions set.
     */
    public void setDietaryRestrictions(final Set<DietaryRestriction> dietaryRestrictions) {
        this.dietaryRestrictions = dietaryRestrictions;
    }

    /**
     * Gets preferred workout days.
     *
     * @return workout days set.
     */
    public Set<DayOfWeek> getPreferredWorkoutDays() {
        return this.preferredWorkoutDays;
    }

    /**
     * Sets preferred workout days.
     *
     * @param preferredWorkoutDays workout days set.
     */
    public void setPreferredWorkoutDays(final Set<DayOfWeek> preferredWorkoutDays) {
        this.preferredWorkoutDays = preferredWorkoutDays;
    }

    /**
     * Gets preferred workout duration in minutes.
     *
     * @return duration in minutes.
     */
    public int getPreferredWorkoutDurationMinutes() {
        return this.preferredWorkoutDurationMinutes;
    }

    /**
     * Sets preferred workout duration in minutes.
     *
     * @param preferredWorkoutDurationMinutes duration in minutes.
     */
    public void setPreferredWorkoutDurationMinutes(final int preferredWorkoutDurationMinutes) {
        this.preferredWorkoutDurationMinutes = preferredWorkoutDurationMinutes;
    }

    /**
     * Gets privacy settings set.
     *
     * @return privacy settings set.
     */
    public Set<PrivacySetting> getPrivacySettings() {
        return this.privacySettings;
    }

    /**
     * Sets privacy settings set.
     *
     * @param privacySettings privacy settings set.
     */
    public void setPrivacySettings(final Set<PrivacySetting> privacySettings) {
        this.privacySettings = privacySettings;
    }

    /**
     * Gets profile error.
     *
     * @return profile error string.
     */
    public String getProfileError() {
        return this.profileError;
    }

    /**
     * Sets profile error.
     *
     * @param profileError profile error string.
     */
    public void setProfileError(final String profileError) {
        this.profileError = profileError;
    }

    /**
     * Gets save confirmation message.
     *
     * @return save confirmation string.
     */
    public String getSaveConfirmation() {
        return this.saveConfirmation;
    }

    /**
     * Sets save confirmation message.
     *
     * @param saveConfirmation save confirmation string.
     */
    public void setSaveConfirmation(final String saveConfirmation) {
        this.saveConfirmation = saveConfirmation;
    }
}
