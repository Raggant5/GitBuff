package entity;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;

/**
 * The representation of a user in our domain model.
 */
public interface User {

    /**
     * Gets the username.
     *
     * @return username string
     */
    String getName();

    /**
     * Gets the user password.
     *
     * @return password string
     */
    String getPassword();

    /**
     * Sets user height in meters.
     *
     * @param height height in meters
     */
    void setHeight(float height);

    /**
     * Gets user height in meters.
     *
     * @return height float
     */
    float getHeight();

    /**
     * Sets user weight in kilograms.
     *
     * @param weight weight in kg
     */
    void setWeight(float weight);

    /**
     * Gets user weight in kilograms.
     *
     * @return weight float
     */
    float getWeight();

    /**
     * Calculates user Body Mass Index (BMI).
     *
     * @return calculated BMI
     */
    default double getBMI() {
        if (this.getWeight() != 0.0f && this.getHeight() != 0.0f) {
            return this.getWeight() / Math.pow(this.getHeight(), 2);
        }
        return 0.0d;
    }

    /**
     * Sets user activity level.
     *
     * @param activityLevel activity level
     */
    void setActivityLevel(ActivityLevel activityLevel);

    /**
     * Gets user activity level.
     *
     * @return activity level
     */
    ActivityLevel getActivityLevel();

    /**
     * Sets user fitness goal.
     *
     * @param goal fitness goal
     */
    void setGoal(FitnessGoal goal);

    /**
     * Gets user fitness goal.
     *
     * @return fitness goal
     */
    FitnessGoal getGoal();

    /**
     * Sets profile picture file path.
     *
     * @param profilePicturePath picture file path
     */
    void setProfilePicturePath(String profilePicturePath);

    /**
     * Gets profile picture file path.
     *
     * @return picture file path
     */
    String getProfilePicturePath();

    /**
     * Sets user date of birth.
     *
     * @param dateOfBirth date of birth
     */
    void setDateOfBirth(LocalDate dateOfBirth);

    /**
     * Gets user date of birth.
     *
     * @return date of birth
     */
    LocalDate getDateOfBirth();

    /**
     * Sets user gender.
     *
     * @param gender gender
     */
    void setGender(Gender gender);

    /**
     * Gets user gender.
     *
     * @return gender
     */
    Gender getGender();

    /**
     * Sets user profile bio.
     *
     * @param bio user bio
     */
    void setBio(String bio);

    /**
     * Gets user profile bio.
     *
     * @return user bio
     */
    String getBio();

    /**
     * Sets preferred unit measurement system.
     *
     * @param unitSystem preferred unit system
     */
    void setPreferredUnitSystem(UnitSystem unitSystem);

    /**
     * Gets preferred unit measurement system.
     *
     * @return preferred unit system
     */
    UnitSystem getPreferredUnitSystem();

    /**
     * Sets available equipment set.
     *
     * @param equipment set of available equipment
     */
    void setEquipment(Set<Equipment> equipment);

    /**
     * Gets available equipment set.
     *
     * @return set of equipment
     */
    Set<Equipment> getEquipment();

    /**
     * Sets dietary restrictions set.
     *
     * @param dietaryRestrictions set of dietary restrictions
     */
    void setDietaryRestrictions(Set<DietaryRestriction> dietaryRestrictions);

    /**
     * Gets dietary restrictions set.
     *
     * @return set of dietary restrictions
     */
    Set<DietaryRestriction> getDietaryRestrictions();

    /**
     * Sets preferred workout days set.
     *
     * @param preferredWorkoutDays set of preferred workout days
     */
    void setPreferredWorkoutDays(Set<DayOfWeek> preferredWorkoutDays);

    /**
     * Gets preferred workout days set.
     *
     * @return set of preferred workout days
     */
    Set<DayOfWeek> getPreferredWorkoutDays();

    /**
     * Sets preferred workout duration in minutes.
     *
     * @param minutes duration in minutes
     */
    void setPreferredWorkoutDurationMinutes(int minutes);

    /**
     * Gets preferred workout duration in minutes.
     *
     * @return duration in minutes
     */
    int getPreferredWorkoutDurationMinutes();

    /**
     * Sets privacy settings set.
     *
     * @param privacySettings set of privacy settings
     */
    void setPrivacySettings(Set<PrivacySetting> privacySettings);

    /**
     * Gets privacy settings set.
     *
     * @return set of privacy settings
     */
    Set<PrivacySetting> getPrivacySettings();
}
