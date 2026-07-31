package entity;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Standard implementation of the User domain entity.
 */
public class CommonUser implements User {

    private static final int DEFAULT_DURATION = 45;

    private final String name;
    private final String password;
    private float weight;
    private float height;
    private ActivityLevel activityLevel;
    private FitnessGoal goal;
    private String profilePicturePath;

    private LocalDate dateOfBirth;
    private Gender gender;
    private String bio;
    private UnitSystem preferredUnitSystem;
    private Set<Equipment> equipment;
    private Set<DietaryRestriction> dietaryRestrictions;
    private Set<DayOfWeek> preferredWorkoutDays;
    private int preferredWorkoutDurationMinutes;
    private Set<PrivacySetting> privacySettings;

    public CommonUser(final String name, final String password) {
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
        this.preferredWorkoutDurationMinutes = DEFAULT_DURATION;
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
    public void setWeight(final float weight) {
        this.weight = weight;
    }

    @Override
    public float getWeight() {
        return this.weight;
    }

    @Override
    public void setHeight(final float height) {
        this.height = height;
    }

    @Override
    public float getHeight() {
        return this.height;
    }

    @Override
    public void setActivityLevel(final ActivityLevel activityLevel) {
        this.activityLevel = activityLevel;
    }

    @Override
    public ActivityLevel getActivityLevel() {
        return this.activityLevel;
    }

    @Override
    public void setGoal(final FitnessGoal goal) {
        this.goal = goal;
    }

    @Override
    public FitnessGoal getGoal() {
        return this.goal;
    }

    @Override
    public void setProfilePicturePath(final String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }

    @Override
    public String getProfilePicturePath() {
        return this.profilePicturePath;
    }

    @Override
    public void setDateOfBirth(final LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public LocalDate getDateOfBirth() {
        return this.dateOfBirth;
    }

    @Override
    public void setGender(final Gender gender) {
        this.gender = gender;
    }

    @Override
    public Gender getGender() {
        return this.gender;
    }

    @Override
    public void setBio(final String bio) {
        this.bio = bio;
    }

    @Override
    public String getBio() {
        return this.bio;
    }

    @Override
    public void setPreferredUnitSystem(final UnitSystem preferredUnitSystem) {
        this.preferredUnitSystem = preferredUnitSystem;
    }

    @Override
    public UnitSystem getPreferredUnitSystem() {
        return this.preferredUnitSystem;
    }

    @Override
    public void setEquipment(final Set<Equipment> equipment) {
        this.equipment = equipment;
    }

    @Override
    public Set<Equipment> getEquipment() {
        return this.equipment;
    }

    @Override
    public void setDietaryRestrictions(final Set<DietaryRestriction> dietaryRestrictions) {
        this.dietaryRestrictions = dietaryRestrictions;
    }

    @Override
    public Set<DietaryRestriction> getDietaryRestrictions() {
        return this.dietaryRestrictions;
    }

    @Override
    public void setPreferredWorkoutDays(final Set<DayOfWeek> preferredWorkoutDays) {
        this.preferredWorkoutDays = preferredWorkoutDays;
    }

    @Override
    public Set<DayOfWeek> getPreferredWorkoutDays() {
        return this.preferredWorkoutDays;
    }

    @Override
    public void setPreferredWorkoutDurationMinutes(final int preferredWorkoutDurationMinutes) {
        this.preferredWorkoutDurationMinutes = preferredWorkoutDurationMinutes;
    }

    @Override
    public int getPreferredWorkoutDurationMinutes() {
        return this.preferredWorkoutDurationMinutes;
    }

    @Override
    public void setPrivacySettings(final Set<PrivacySetting> privacySettings) {
        this.privacySettings = privacySettings;
    }

    @Override
    public Set<PrivacySetting> getPrivacySettings() {
        return this.privacySettings;
    }
}
