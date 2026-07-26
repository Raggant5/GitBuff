package entity;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;

/**
 * The representation of a user in our program.
 */
public interface User {

    String getName();

    String getPassword();

    void setHeight(float height);

    float getHeight();

    void setWeight(float weight);

    float getWeight();

    default double getBMI() {
        if (this.getWeight() != 0.0f && this.getHeight() != 0.0f) {
            return this.getWeight() / Math.pow(this.getHeight(), 2);
        }
        return 0.0d;
    }

    void setActivityLevel(ActivityLevel activityLevel);

    ActivityLevel getActivityLevel();

    void setGoal(FitnessGoal goal);

    FitnessGoal getGoal();

    void setProfilePicturePath(String profilePicturePath);

    String getProfilePicturePath();

    // --- Profile Expansion Fields ---

    void setDateOfBirth(LocalDate dateOfBirth);

    LocalDate getDateOfBirth();

    void setGender(Gender gender);

    Gender getGender();

    void setBio(String bio);

    String getBio();

    void setPreferredUnitSystem(UnitSystem unitSystem);

    UnitSystem getPreferredUnitSystem();

    void setEquipment(Set<Equipment> equipment);

    Set<Equipment> getEquipment();

    void setDietaryRestrictions(Set<DietaryRestriction> dietaryRestrictions);

    Set<DietaryRestriction> getDietaryRestrictions();

    void setPreferredWorkoutDays(Set<DayOfWeek> preferredWorkoutDays);

    Set<DayOfWeek> getPreferredWorkoutDays();

    void setPreferredWorkoutDurationMinutes(int minutes);

    int getPreferredWorkoutDurationMinutes();

    void setPrivacySettings(Set<PrivacySetting> privacySettings);

    Set<PrivacySetting> getPrivacySettings();
}
