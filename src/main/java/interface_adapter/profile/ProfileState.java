package interface_adapter.profile;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * The state for the Profile View Model.
 *
 * <p>Holds the interface_adapter-layer {@code *Option} enums ({@link ActivityLevelOption},
 * {@link FitnessGoalOption}, {@link GenderOption}, {@link UnitSystemOption},
 * {@link EquipmentOption}, {@link DietaryRestrictionOption}, {@link PrivacySettingOption})
 * rather than the {@code entity.*} enums they mirror, so this interface_adapter-layer class -
 * and {@code view.ProfileView}, which reads it - does not depend on the entity layer.
 * {@link ProfileController} and {@code interface_adapter.profile.ProfilePresenter} are
 * responsible for translating to/from the entity enums (via {@link ProfileEnumMapper}) at the
 * use case boundary.
 */
public class ProfileState {

    private static final int DEFAULT_DURATION = 45;

    private String username = "";
    private String heightText = "";
    private String weightText = "";
    private ActivityLevelOption activityLevel = ActivityLevelOption.MODERATELY_ACTIVE;
    private FitnessGoalOption goal = FitnessGoalOption.MAINTAIN_GENERAL_FITNESS;
    private String profilePicturePath;

    private LocalDate dateOfBirth;
    private GenderOption gender = GenderOption.PREFER_NOT_TO_SAY;
    private String bio = "";
    private UnitSystemOption preferredUnitSystem = UnitSystemOption.METRIC;
    private Set<EquipmentOption> equipment = new HashSet<>();
    private Set<DietaryRestrictionOption> dietaryRestrictions = new HashSet<>();
    private Set<DayOfWeek> preferredWorkoutDays = new HashSet<>();
    private int preferredWorkoutDurationMinutes = DEFAULT_DURATION;
    private Set<PrivacySettingOption> privacySettings = new HashSet<>();

    private String profileError;
    private String saveConfirmation;

    public String getUsername() {
        return this.username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getHeightText() {
        return this.heightText;
    }

    public void setHeightText(final String heightText) {
        this.heightText = heightText;
    }

    public String getWeightText() {
        return this.weightText;
    }

    public void setWeightText(final String weightText) {
        this.weightText = weightText;
    }

    public ActivityLevelOption getActivityLevel() {
        return this.activityLevel;
    }

    public void setActivityLevel(final ActivityLevelOption activityLevel) {
        this.activityLevel = activityLevel;
    }

    public FitnessGoalOption getGoal() {
        return this.goal;
    }

    public void setGoal(final FitnessGoalOption goal) {
        this.goal = goal;
    }

    public String getProfilePicturePath() {
        return this.profilePicturePath;
    }

    public void setProfilePicturePath(final String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }

    public LocalDate getDateOfBirth() {
        return this.dateOfBirth;
    }

    public void setDateOfBirth(final LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public GenderOption getGender() {
        return this.gender;
    }

    public void setGender(final GenderOption gender) {
        this.gender = gender;
    }

    public String getBio() {
        return this.bio;
    }

    public void setBio(final String bio) {
        this.bio = bio;
    }

    public UnitSystemOption getPreferredUnitSystem() {
        return this.preferredUnitSystem;
    }

    public void setPreferredUnitSystem(final UnitSystemOption preferredUnitSystem) {
        this.preferredUnitSystem = preferredUnitSystem;
    }

    public Set<EquipmentOption> getEquipment() {
        return this.equipment;
    }

    public void setEquipment(final Set<EquipmentOption> equipment) {
        this.equipment = equipment;
    }

    public Set<DietaryRestrictionOption> getDietaryRestrictions() {
        return this.dietaryRestrictions;
    }

    public void setDietaryRestrictions(final Set<DietaryRestrictionOption> dietaryRestrictions) {
        this.dietaryRestrictions = dietaryRestrictions;
    }

    public Set<DayOfWeek> getPreferredWorkoutDays() {
        return this.preferredWorkoutDays;
    }

    public void setPreferredWorkoutDays(final Set<DayOfWeek> preferredWorkoutDays) {
        this.preferredWorkoutDays = preferredWorkoutDays;
    }

    public int getPreferredWorkoutDurationMinutes() {
        return this.preferredWorkoutDurationMinutes;
    }

    public void setPreferredWorkoutDurationMinutes(final int preferredWorkoutDurationMinutes) {
        this.preferredWorkoutDurationMinutes = preferredWorkoutDurationMinutes;
    }

    public Set<PrivacySettingOption> getPrivacySettings() {
        return this.privacySettings;
    }

    public void setPrivacySettings(final Set<PrivacySettingOption> privacySettings) {
        this.privacySettings = privacySettings;
    }

    public String getProfileError() {
        return this.profileError;
    }

    public void setProfileError(final String profileError) {
        this.profileError = profileError;
    }

    public String getSaveConfirmation() {
        return this.saveConfirmation;
    }

    public void setSaveConfirmation(final String saveConfirmation) {
        this.saveConfirmation = saveConfirmation;
    }
}


