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

    public String getUsername() {
        return this.username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getHeightText() {
        return this.heightText;
    }

    public void setHeightText(final String heightText) {
        this.heightText = heightText;
    }

    public String getWeightText() {
        return this.weightText;
    }

    public void setWeightText(final String weightText) {
        this.weightText = weightText;
    }

    public ActivityLevel getActivityLevel() {
        return this.activityLevel;
    }

    public void setActivityLevel(final ActivityLevel activityLevel) {
        this.activityLevel = activityLevel;
    }

    public FitnessGoal getGoal() {
        return this.goal;
    }

    public void setGoal(final FitnessGoal goal) {
        this.goal = goal;
    }

    public String getProfilePicturePath() {
        return this.profilePicturePath;
    }

    public void setProfilePicturePath(final String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }

    public LocalDate getDateOfBirth() {
        return this.dateOfBirth;
    }

    public void setDateOfBirth(final LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Gender getGender() {
        return this.gender;
    }

    public void setGender(final Gender gender) {
        this.gender = gender;
    }

    public String getBio() {
        return this.bio;
    }

    public void setBio(final String bio) {
        this.bio = bio;
    }

    public UnitSystem getPreferredUnitSystem() {
        return this.preferredUnitSystem;
    }

    public void setPreferredUnitSystem(final UnitSystem preferredUnitSystem) {
        this.preferredUnitSystem = preferredUnitSystem;
    }

    public Set<Equipment> getEquipment() {
        return this.equipment;
    }

    public void setEquipment(final Set<Equipment> equipment) {
        this.equipment = equipment;
    }

    public Set<DietaryRestriction> getDietaryRestrictions() {
        return this.dietaryRestrictions;
    }

    public void setDietaryRestrictions(final Set<DietaryRestriction> dietaryRestrictions) {
        this.dietaryRestrictions = dietaryRestrictions;
    }

    public Set<DayOfWeek> getPreferredWorkoutDays() {
        return this.preferredWorkoutDays;
    }

    public void setPreferredWorkoutDays(final Set<DayOfWeek> preferredWorkoutDays) {
        this.preferredWorkoutDays = preferredWorkoutDays;
    }

    public int getPreferredWorkoutDurationMinutes() {
        return this.preferredWorkoutDurationMinutes;
    }

    public void setPreferredWorkoutDurationMinutes(final int preferredWorkoutDurationMinutes) {
        this.preferredWorkoutDurationMinutes = preferredWorkoutDurationMinutes;
    }

    public Set<PrivacySetting> getPrivacySettings() {
        return this.privacySettings;
    }

    public void setPrivacySettings(final Set<PrivacySetting> privacySettings) {
        this.privacySettings = privacySettings;
    }

    public String getProfileError() {
        return this.profileError;
    }

    public void setProfileError(final String profileError) {
        this.profileError = profileError;
    }

    public String getSaveConfirmation() {
        return this.saveConfirmation;
    }

    public void setSaveConfirmation(final String saveConfirmation) {
        this.saveConfirmation = saveConfirmation;
    }
}
