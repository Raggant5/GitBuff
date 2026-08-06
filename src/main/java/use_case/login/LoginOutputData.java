package use_case.login;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import entity.ActivityLevel;
import entity.DietaryRestriction;
import entity.Equipment;
import entity.FitnessGoal;
import entity.Gender;
import entity.LoggedWorkout;
import entity.Meal;
import entity.PrivacySetting;
import entity.UnitSystem;

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

    private final LocalDate dateOfBirth;
    private final Gender gender;
    private final String bio;
    private final UnitSystem preferredUnitSystem;
    private final Set<Equipment> equipment;
    private final Set<DietaryRestriction> dietaryRestrictions;
    private final Set<DayOfWeek> preferredWorkoutDays;
    private final int preferredWorkoutDurationMinutes;
    private final Set<PrivacySetting> privacySettings;

    private final List<Meal> meals;
    private final List<LoggedWorkout> workouts;
    private final boolean useCaseFailed;

    public LoginOutputData(
            String username,
            float height,
            float weight,
            ActivityLevel activityLevel,
            FitnessGoal goal,
            String profilePicturePath,
            LocalDate dateOfBirth,
            Gender gender,
            String bio,
            UnitSystem preferredUnitSystem,
            Set<Equipment> equipment,
            Set<DietaryRestriction> dietaryRestrictions,
            Set<DayOfWeek> preferredWorkoutDays,
            int preferredWorkoutDurationMinutes,
            Set<PrivacySetting> privacySettings,
            List<Meal> meals,
            List<LoggedWorkout> workouts,
            boolean useCaseFailed) {

        this.username = username;
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
        this.preferredWorkoutDurationMinutes =
                preferredWorkoutDurationMinutes;
        this.privacySettings = privacySettings;

        this.meals = meals;
        this.workouts = workouts;
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

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public Gender getGender() {
        return gender;
    }

    public String getBio() {
        return bio;
    }

    public UnitSystem getPreferredUnitSystem() {
        return preferredUnitSystem;
    }

    public Set<Equipment> getEquipment() {
        return equipment;
    }

    public Set<DietaryRestriction> getDietaryRestrictions() {
        return dietaryRestrictions;
    }

    public Set<DayOfWeek> getPreferredWorkoutDays() {
        return preferredWorkoutDays;
    }

    public int getPreferredWorkoutDurationMinutes() {
        return preferredWorkoutDurationMinutes;
    }

    public Set<PrivacySetting> getPrivacySettings() {
        return privacySettings;
    }

    public List<Meal> getMeals() {
        return this.meals;
    }

    public List<LoggedWorkout> getWorkouts() {
        return this.workouts;
    }

    public boolean isUseCaseFailed() {
        return this.useCaseFailed;
    }
}