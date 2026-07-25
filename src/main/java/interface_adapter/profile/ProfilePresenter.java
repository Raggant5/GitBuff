package interface_adapter.profile;

import use_case.profile.EditProfileOutputBoundary;
import use_case.profile.EditProfileOutputData;

/**
 * The Presenter for the Edit Profile Use Case.
 */
public class ProfilePresenter implements EditProfileOutputBoundary {

    private final ProfileViewModel profileViewModel;

    public ProfilePresenter(ProfileViewModel profileViewModel) {
        this.profileViewModel = profileViewModel;
    }

    @Override
    public void prepareSuccessView(EditProfileOutputData outputData) {
        final ProfileState profileState = profileViewModel.getState();
        profileState.setUsername(outputData.getUsername());
        profileState.setHeightText(String.valueOf(outputData.getHeight()));
        profileState.setWeightText(String.valueOf(outputData.getWeight()));
        profileState.setActivityLevel(outputData.getActivityLevel());
        profileState.setGoal(outputData.getGoal());
        profileState.setProfilePicturePath(outputData.getProfilePicturePath());
        profileState.setDateOfBirth(outputData.getDateOfBirth());
        profileState.setGender(outputData.getGender());
        profileState.setBio(outputData.getBio());
        profileState.setPreferredUnitSystem(outputData.getPreferredUnitSystem());
        profileState.setEquipment(outputData.getEquipment());
        profileState.setDietaryRestrictions(outputData.getDietaryRestrictions());
        profileState.setPreferredWorkoutDays(outputData.getPreferredWorkoutDays());
        profileState.setPreferredWorkoutDurationMinutes(outputData.getPreferredWorkoutDurationMinutes());
        profileState.setPrivacySettings(outputData.getPrivacySettings());
        profileState.setProfileError(null);
        profileState.setSaveConfirmation("Profile saved.");
        profileViewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final ProfileState profileState = profileViewModel.getState();
        profileState.setSaveConfirmation(null);
        profileState.setProfileError(errorMessage);
        profileViewModel.firePropertyChanged();
    }
}
