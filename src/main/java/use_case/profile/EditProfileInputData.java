package use_case.profile;

import entity.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;

/**
 * The Input Data for the Edit Profile Use Case.
 */
public class EditProfileInputData {

    private final float height;
    private final float weight;
    private final ActivityLevel activityLevel;
    private final FitnessGoal goal;
    private final String profilePicturePath;
    private final LocalDate dateOfBirth;
    private final Gender gender;
    private final String bio;
    private final UnitSystem preferredUnitSystem;
    private final Set<Equipment> equipment;
    private final Set<DietaryRestriction> dietaryRestrictions;
    private final Set<DayOfWeek> preferredWorkoutDays;
    private final int preferredWorkoutDurationMinutes;
    private final Set<PrivacySetting> privacySettings;

    public EditProfileInputData(float height, float weight, ActivityLevel activityLevel,
                                FitnessGoal goal, String profilePicturePath,
                                LocalDate dateOfBirth, Gender gender, String bio,
                                UnitSystem preferredUnitSystem, Set<Equipment> equipment,
                                Set<DietaryRestriction> dietaryRestrictions,
                                Set<DayOfWeek> preferredWorkoutDays,
                                int preferredWorkoutDurationMinutes,
                                Set<PrivacySetting> privacySettings) {
        this.height = height;
        this.weight = weight;
        this.activityLevel = activityLevel;
        this.goal = goal;
        this.profilePicturePath = profilePicturePath;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.bio = bio;
        this.preferredUnitSystem = preferredUnitSystem;
        this.equipment = equipment;
        this.dietaryRestrictions = dietaryRestrictions;
        this.preferredWorkoutDays = preferredWorkoutDays;
        this.preferredWorkoutDurationMinutes = preferredWorkoutDurationMinutes;
        this.privacySettings = privacySettings;
    }

    public float getHeight() { return height; }
    public float getWeight() { return weight; }
    public ActivityLevel getActivityLevel() { return activityLevel; }
    public FitnessGoal getGoal() { return goal; }
    public String getProfilePicturePath() { return profilePicturePath; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public Gender getGender() { return gender; }
    public String getBio() { return bio; }
    public UnitSystem getPreferredUnitSystem() { return preferredUnitSystem; }
    public Set<Equipment> getEquipment() { return equipment; }
    public Set<DietaryRestriction> getDietaryRestrictions() { return dietaryRestrictions; }
    public Set<DayOfWeek> getPreferredWorkoutDays() { return preferredWorkoutDays; }
    public int getPreferredWorkoutDurationMinutes() { return preferredWorkoutDurationMinutes; }
    public Set<PrivacySetting> getPrivacySettings() { return privacySettings; }
}


