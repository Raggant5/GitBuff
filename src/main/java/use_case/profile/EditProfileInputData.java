package use_case.profile;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;

import entity.ActivityLevel;
import entity.DietaryRestriction;
import entity.Equipment;
import entity.FitnessGoal;
import entity.Gender;
import entity.PrivacySetting;
import entity.UnitSystem;

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

    /**
     * Constructs an EditProfileInputData instance.
     *
     * @param height height in meters
     * @param weight weight in kilograms
     * @param activityLevel activity level selection
     * @param goal fitness goal selection
     * @param profilePicturePath profile image file path
     * @param dateOfBirth date of birth
     * @param gender gender selection
     * @param bio user bio description
     * @param preferredUnitSystem preferred measurement units
     * @param equipment set of available equipment
     * @param dietaryRestrictions set of dietary restrictions
     * @param preferredWorkoutDays set of preferred workout days
     * @param preferredWorkoutDurationMinutes target workout duration in minutes
     * @param privacySettings set of enabled privacy settings
     */
    public EditProfileInputData(final float height, final float weight, final ActivityLevel activityLevel,
                                final FitnessGoal goal, final String profilePicturePath,
                                final LocalDate dateOfBirth, final Gender gender, final String bio,
                                final UnitSystem preferredUnitSystem, final Set<Equipment> equipment,
                                final Set<DietaryRestriction> dietaryRestrictions,
                                final Set<DayOfWeek> preferredWorkoutDays,
                                final int preferredWorkoutDurationMinutes,
                                final Set<PrivacySetting> privacySettings) {
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
        return this.dateOfBirth;
    }

    public Gender getGender() {
        return this.gender;
    }

    public String getBio() {
        return this.bio;
    }

    public UnitSystem getPreferredUnitSystem() {
        return this.preferredUnitSystem;
    }

    public Set<Equipment> getEquipment() {
        return this.equipment;
    }

    public Set<DietaryRestriction> getDietaryRestrictions() {
        return this.dietaryRestrictions;
    }

    public Set<DayOfWeek> getPreferredWorkoutDays() {
        return this.preferredWorkoutDays;
    }

    public int getPreferredWorkoutDurationMinutes() {
        return this.preferredWorkoutDurationMinutes;
    }

    public Set<PrivacySetting> getPrivacySettings() {
        return this.privacySettings;
    }
}


