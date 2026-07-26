package interface_adapter.profile;

import entity.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * The state for the Profile View Model.
 */
public class ProfileState {
    private String username = "";
    private String heightText = "";
    private String weightText = "";
    private ActivityLevel activityLevel = ActivityLevel.MODERATELY_ACTIVE;
    private FitnessGoal goal = FitnessGoal.MAINTAIN_GENERAL_FITNESS;
    private String profilePicturePath;

    // --- Profile Expansion Fields ---
    private LocalDate dateOfBirth;
    private Gender gender = Gender.PREFER_NOT_TO_SAY;
    private String bio = "";
    private UnitSystem preferredUnitSystem = UnitSystem.METRIC;
    private Set<Equipment> equipment = new HashSet<>();
    private Set<DietaryRestriction> dietaryRestrictions = new HashSet<>();
    private Set<DayOfWeek> preferredWorkoutDays = new HashSet<>();
    private int preferredWorkoutDurationMinutes = 45;
    private Set<PrivacySetting> privacySettings = new HashSet<>();

    private String profileError;
    private String saveConfirmation;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getHeightText() { return heightText; }
    public void setHeightText(String heightText) { this.heightText = heightText; }

    public String getWeightText() { return weightText; }
    public void setWeightText(String weightText) { this.weightText = weightText; }

    public ActivityLevel getActivityLevel() { return activityLevel; }
    public void setActivityLevel(ActivityLevel activityLevel) { this.activityLevel = activityLevel; }

    public FitnessGoal getGoal() { return goal; }
    public void setGoal(FitnessGoal goal) { this.goal = goal; }

    public String getProfilePicturePath() { return profilePicturePath; }
    public void setProfilePicturePath(String profilePicturePath) { this.profilePicturePath = profilePicturePath; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public Gender getGender() { return gender; }
    public void setGender(Gender gender) { this.gender = gender; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public UnitSystem getPreferredUnitSystem() { return preferredUnitSystem; }
    public void setPreferredUnitSystem(UnitSystem preferredUnitSystem) { this.preferredUnitSystem = preferredUnitSystem; }

    public Set<Equipment> getEquipment() { return equipment; }
    public void setEquipment(Set<Equipment> equipment) { this.equipment = equipment; }

    public Set<DietaryRestriction> getDietaryRestrictions() { return dietaryRestrictions; }
    public void setDietaryRestrictions(Set<DietaryRestriction> dietaryRestrictions) { this.dietaryRestrictions = dietaryRestrictions; }

    public Set<DayOfWeek> getPreferredWorkoutDays() { return preferredWorkoutDays; }
    public void setPreferredWorkoutDays(Set<DayOfWeek> preferredWorkoutDays) { this.preferredWorkoutDays = preferredWorkoutDays; }

    public int getPreferredWorkoutDurationMinutes() { return preferredWorkoutDurationMinutes; }
    public void setPreferredWorkoutDurationMinutes(int preferredWorkoutDurationMinutes) { this.preferredWorkoutDurationMinutes = preferredWorkoutDurationMinutes; }

    public Set<PrivacySetting> getPrivacySettings() { return privacySettings; }
    public void setPrivacySettings(Set<PrivacySetting> privacySettings) { this.privacySettings = privacySettings; }

    public String getProfileError() { return profileError; }
    public void setProfileError(String profileError) { this.profileError = profileError; }

    public String getSaveConfirmation() { return saveConfirmation; }
    public void setSaveConfirmation(String saveConfirmation) { this.saveConfirmation = saveConfirmation; }
}
