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
     * @param user the user whose saved profile fields populate this output data.
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

    /**
     * Gets username string.
     *
     * @return username string.
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Gets height float.
     *
     * @return height float.
     */
    public float getHeight() {
        return this.height;
    }

    /**
     * Gets weight float.
     *
     * @return weight float.
     */
    public float getWeight() {
        return this.weight;
    }

    /**
     * Gets activity level.
     *
     * @return activity level.
     */
    public ActivityLevel getActivityLevel() {
        return this.activityLevel;
    }

    /**
     * Gets fitness goal.
     *
     * @return fitness goal.
     */
    public FitnessGoal getGoal() {
        return this.goal;
    }

    /**
     * Gets profile picture path.
     *
     * @return profile picture path string.
     */
    public String getProfilePicturePath() {
        return this.profilePicturePath;
    }

    /**
     * Gets date of birth.
     *
     * @return date of birth.
     */
    public LocalDate getDateOfBirth() {
        return this.dateOfBirth;
    }

    /**
     * Gets gender.
     *
     * @return gender.
     */
    public Gender getGender() {
        return this.gender;
    }

    /**
     * Gets bio text.
     *
     * @return bio string.
     */
    public String getBio() {
        return this.bio;
    }

    /**
     * Gets preferred unit system.
     *
     * @return preferred unit system.
     */
    public UnitSystem getPreferredUnitSystem() {
        return this.preferredUnitSystem;
    }

    /**
     * Gets available equipment set.
     *
     * @return equipment set.
     */
    public Set<Equipment> getEquipment() {
        return this.equipment;
    }

    /**
     * Gets dietary restrictions set.
     *
     * @return dietary restrictions set.
     */
    public Set<DietaryRestriction> getDietaryRestrictions() {
        return this.dietaryRestrictions;
    }

    /**
     * Gets preferred workout days.
     *
     * @return workout days set.
     */
    public Set<DayOfWeek> getPreferredWorkoutDays() {
        return this.preferredWorkoutDays;
    }

    /**
     * Gets preferred workout duration in minutes.
     *
     * @return duration in minutes.
     */
    public int getPreferredWorkoutDurationMinutes() {
        return this.preferredWorkoutDurationMinutes;
    }

    /**
     * Gets privacy settings set.
     *
     * @return privacy settings set.
     */
    public Set<PrivacySetting> getPrivacySettings() {
        return this.privacySettings;
    }
}
