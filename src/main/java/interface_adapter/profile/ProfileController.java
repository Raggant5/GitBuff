package interface_adapter.profile;

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
import use_case.profile.EditProfileInputBoundary;
import use_case.profile.EditProfileInputData;

/**
 * The controller for the Edit Profile Use Case.
 */
public class ProfileController {

    private final EditProfileInputBoundary editProfileUseCaseInteractor;

    /**
     * Constructs a ProfileController instance.
     *
     * @param editProfileUseCaseInteractor interactor boundary for profile modifications
     */
    public ProfileController(final EditProfileInputBoundary editProfileUseCaseInteractor) {
        this.editProfileUseCaseInteractor = editProfileUseCaseInteractor;
    }

    /**
     * Executes the Edit Profile Use Case.
     *
     * @param height height in meters
     * @param weight weight in kg
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
    public void execute(final float height, final float weight, final ActivityLevel activityLevel,
                        final FitnessGoal goal, final String profilePicturePath,
                        final LocalDate dateOfBirth, final Gender gender, final String bio,
                        final UnitSystem preferredUnitSystem, final Set<Equipment> equipment,
                        final Set<DietaryRestriction> dietaryRestrictions,
                        final Set<DayOfWeek> preferredWorkoutDays,
                        final int preferredWorkoutDurationMinutes,
                        final Set<PrivacySetting> privacySettings) {
        final EditProfileInputData inputData = new EditProfileInputData(
                height, weight, activityLevel, goal, profilePicturePath,
                dateOfBirth, gender, bio, preferredUnitSystem, equipment,
                dietaryRestrictions, preferredWorkoutDays,
                preferredWorkoutDurationMinutes, privacySettings);
        this.editProfileUseCaseInteractor.execute(inputData);
    }
}
