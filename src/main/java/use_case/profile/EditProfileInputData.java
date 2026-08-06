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
public final class EditProfileInputData {

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

    private EditProfileInputData(final Builder builder) {
        this.height = builder.height;
        this.weight = builder.weight;
        this.activityLevel = builder.activityLevel;
        this.goal = builder.goal;
        this.profilePicturePath = builder.profilePicturePath;
        this.dateOfBirth = builder.dateOfBirth;
        this.gender = builder.gender;
        this.bio = builder.bio;
        this.preferredUnitSystem = builder.preferredUnitSystem;
        this.equipment = builder.equipment;
        this.dietaryRestrictions = builder.dietaryRestrictions;
        this.preferredWorkoutDays = builder.preferredWorkoutDays;
        this.preferredWorkoutDurationMinutes = builder.preferredWorkoutDurationMinutes;
        this.privacySettings = builder.privacySettings;
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

    /**
     * Builder for EditProfileInputData, keeping the constructor parameter count within limits.
     */
    public static final class Builder {
        private float height;
        private float weight;
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

        /**
         * Sets the height.
         *
         * @param heightValue height in meters
         * @return this builder
         */
        public Builder height(final float heightValue) {
            this.height = heightValue;
            return this;
        }

        /**
         * Sets the weight.
         *
         * @param weightValue weight in kilograms
         * @return this builder
         */
        public Builder weight(final float weightValue) {
            this.weight = weightValue;
            return this;
        }

        /**
         * Sets the activity level.
         *
         * @param activityLevelValue activity level selection
         * @return this builder
         */
        public Builder activityLevel(final ActivityLevel activityLevelValue) {
            this.activityLevel = activityLevelValue;
            return this;
        }

        /**
         * Sets the fitness goal.
         *
         * @param goalValue fitness goal selection
         * @return this builder
         */
        public Builder goal(final FitnessGoal goalValue) {
            this.goal = goalValue;
            return this;
        }

        /**
         * Sets the profile picture path.
         *
         * @param profilePicturePathValue profile image file path
         * @return this builder
         */
        public Builder profilePicturePath(final String profilePicturePathValue) {
            this.profilePicturePath = profilePicturePathValue;
            return this;
        }

        /**
         * Sets the date of birth.
         *
         * @param dateOfBirthValue date of birth
         * @return this builder
         */
        public Builder dateOfBirth(final LocalDate dateOfBirthValue) {
            this.dateOfBirth = dateOfBirthValue;
            return this;
        }

        /**
         * Sets the gender.
         *
         * @param genderValue gender selection
         * @return this builder
         */
        public Builder gender(final Gender genderValue) {
            this.gender = genderValue;
            return this;
        }

        /**
         * Sets the bio.
         *
         * @param bioValue user bio description
         * @return this builder
         */
        public Builder bio(final String bioValue) {
            this.bio = bioValue;
            return this;
        }

        /**
         * Sets the preferred unit system.
         *
         * @param preferredUnitSystemValue preferred measurement units
         * @return this builder
         */
        public Builder preferredUnitSystem(final UnitSystem preferredUnitSystemValue) {
            this.preferredUnitSystem = preferredUnitSystemValue;
            return this;
        }

        /**
         * Sets the available equipment.
         *
         * @param equipmentValue set of available equipment
         * @return this builder
         */
        public Builder equipment(final Set<Equipment> equipmentValue) {
            this.equipment = equipmentValue;
            return this;
        }

        /**
         * Sets the dietary restrictions.
         *
         * @param dietaryRestrictionsValue set of dietary restrictions
         * @return this builder
         */
        public Builder dietaryRestrictions(final Set<DietaryRestriction> dietaryRestrictionsValue) {
            this.dietaryRestrictions = dietaryRestrictionsValue;
            return this;
        }

        /**
         * Sets the preferred workout days.
         *
         * @param preferredWorkoutDaysValue set of preferred workout days
         * @return this builder
         */
        public Builder preferredWorkoutDays(final Set<DayOfWeek> preferredWorkoutDaysValue) {
            this.preferredWorkoutDays = preferredWorkoutDaysValue;
            return this;
        }

        /**
         * Sets the preferred workout duration.
         *
         * @param preferredWorkoutDurationMinutesValue target workout duration in minutes
         * @return this builder
         */
        public Builder preferredWorkoutDurationMinutes(final int preferredWorkoutDurationMinutesValue) {
            this.preferredWorkoutDurationMinutes = preferredWorkoutDurationMinutesValue;
            return this;
        }

        /**
         * Sets the privacy settings.
         *
         * @param privacySettingsValue set of enabled privacy settings
         * @return this builder
         */
        public Builder privacySettings(final Set<PrivacySetting> privacySettingsValue) {
            this.privacySettings = privacySettingsValue;
            return this;
        }

        /**
         * Builds the EditProfileInputData instance.
         *
         * @return a new EditProfileInputData populated from this builder
         */
        public EditProfileInputData build() {
            return new EditProfileInputData(this);
        }
    }
}
