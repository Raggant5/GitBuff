package interface_adapter.profile;

import entity.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;

import use_case.profile.EditProfileInputBoundary;
import use_case.profile.EditProfileInputData;

/**
 * The controller for the Edit Profile Use Case.
 */
public class ProfileController {

    private final EditProfileInputBoundary editProfileUseCaseInteractor;

    public ProfileController(EditProfileInputBoundary editProfileUseCaseInteractor) {
        this.editProfileUseCaseInteractor = editProfileUseCaseInteractor;
    }

    public void execute(float height, float weight, ActivityLevel activityLevel,
                        FitnessGoal goal, String profilePicturePath,
                        LocalDate dateOfBirth, Gender gender, String bio,
                        UnitSystem preferredUnitSystem, Set<Equipment> equipment,
                        Set<DietaryRestriction> dietaryRestrictions,
                        Set<DayOfWeek> preferredWorkoutDays,
                        int preferredWorkoutDurationMinutes,
                        Set<PrivacySetting> privacySettings) {
        final EditProfileInputData inputData = new EditProfileInputData(
                height, weight, activityLevel, goal, profilePicturePath,
                dateOfBirth, gender, bio, preferredUnitSystem, equipment,
                dietaryRestrictions, preferredWorkoutDays,
                preferredWorkoutDurationMinutes, privacySettings);
        editProfileUseCaseInteractor.execute(inputData);
    }
}
