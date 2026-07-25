package entity;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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

    // --- Profile Expansion Fields ---
    private LocalDate dateOfBirth;
    private Gender gender;
    private String bio;
    private UnitSystem preferredUnitSystem;
    private Set<Equipment> equipment;
    private Set<DietaryRestriction> dietaryRestrictions;
    private Set<DayOfWeek> preferredWorkoutDays;
    private int preferredWorkoutDurationMinutes;
    private Set<PrivacySetting> privacySettings;

    public CommonUser(String name, String password) {
        this.name = name;
        this.password = password;
        this.height = 0.0f;
        this.weight = 0.0f;
        this.activityLevel = ActivityLevel.MODERATELY_ACTIVE;
        this.goal = FitnessGoal.MAINTAIN_GENERAL_FITNESS;
        this.profilePicturePath = null;

        this.dateOfBirth = null;
        this.gender = Gender.PREFER_NOT_TO_SAY;
        this.bio = "";
        this.preferredUnitSystem = UnitSystem.METRIC;
        this.equipment = new HashSet<>();
        this.dietaryRestrictions = new HashSet<>();
        this.preferredWorkoutDays = new HashSet<>();
        this.preferredWorkoutDurationMinutes = 45;
        this.privacySettings = new HashSet<>();
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

    @Override
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    @Override
    public LocalDate getDateOfBirth() { return this.dateOfBirth; }

    @Override
    public void setGender(Gender gender) { this.gender = gender; }

    @Override
    public Gender getGender() { return this.gender; }

    @Override
    public void setBio(String bio) { this.bio = bio; }

    @Override
    public String getBio() { return this.bio; }

    @Override
    public void setPreferredUnitSystem(UnitSystem preferredUnitSystem) { this.preferredUnitSystem = preferredUnitSystem; }

    @Override
    public UnitSystem getPreferredUnitSystem() { return this.preferredUnitSystem; }

    @Override
    public void setEquipment(Set<Equipment> equipment) { this.equipment = equipment; }

    @Override
    public Set<Equipment> getEquipment() { return this.equipment; }

    @Override
    public void setDietaryRestrictions(Set<DietaryRestriction> dietaryRestrictions) { this.dietaryRestrictions = dietaryRestrictions; }

    @Override
    public Set<DietaryRestriction> getDietaryRestrictions() { return this.dietaryRestrictions; }

    @Override
    public void setPreferredWorkoutDays(Set<DayOfWeek> preferredWorkoutDays) { this.preferredWorkoutDays = preferredWorkoutDays; }

    @Override
    public Set<DayOfWeek> getPreferredWorkoutDays() { return this.preferredWorkoutDays; }

    @Override
    public void setPreferredWorkoutDurationMinutes(int preferredWorkoutDurationMinutes) { this.preferredWorkoutDurationMinutes = preferredWorkoutDurationMinutes; }

    @Override
    public int getPreferredWorkoutDurationMinutes() { return this.preferredWorkoutDurationMinutes; }

    @Override
    public void setPrivacySettings(Set<PrivacySetting> privacySettings) { this.privacySettings = privacySettings; }

    @Override
    public Set<PrivacySetting> getPrivacySettings() { return this.privacySettings; }
}
