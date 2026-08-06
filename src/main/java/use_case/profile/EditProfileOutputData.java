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
import entity.User;

/**
 * Output Data for the Edit Profile Use Case.
 */
public class EditProfileOutputData {

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

    /**
     * Constructs an EditProfileOutputData instance from a saved user.
     *
     * @param user the user whose saved profile fields populate this output data
     */
    public EditProfileOutputData(final User user) {
        this.username = user.getName();
        this.height = user.getHeight();
        this.weight = user.getWeight();
        this.activityLevel = user.getActivityLevel();
        this.goal = user.getGoal();
        this.profilePicturePath = user.getProfilePicturePath();
        this.dateOfBirth = user.getDateOfBirth();
        this.gender = user.getGender();
        this.bio = user.getBio();
        this.preferredUnitSystem = user.getPreferredUnitSystem();
        this.equipment = user.getEquipment();
        this.dietaryRestrictions = user.getDietaryRestrictions();
        this.preferredWorkoutDays = user.getPreferredWorkoutDays();
        this.preferredWorkoutDurationMinutes = user.getPreferredWorkoutDurationMinutes();
        this.privacySettings = user.getPrivacySettings();
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
