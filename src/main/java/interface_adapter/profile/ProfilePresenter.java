package interface_adapter.profile;

import use_case.profile.EditProfileOutputBoundary;
import use_case.profile.EditProfileOutputData;

/**
 * The Presenter for the Edit Profile Use Case.
 *
 * <p>Converts the entity-layer enums carried by {@link EditProfileOutputData} into the
 * interface_adapter-layer {@code *Option} enums, via {@link ProfileEnumMapper}, before storing
 * them in {@link ProfileState} - the use case layer is allowed to depend on entities, but this
 * interface_adapter layer is not.
 */
public class ProfilePresenter implements EditProfileOutputBoundary {

    private final ProfileViewModel profileViewModel;

    /**
     * Constructs a ProfilePresenter instance.
     *
     * @param profileViewModel view model for managing profile state
     */
    public ProfilePresenter(final ProfileViewModel profileViewModel) {
        this.profileViewModel = profileViewModel;
    }

    @Override
    public void prepareSuccessView(final EditProfileOutputData outputData) {
        final ProfileState profileState = this.profileViewModel.getState();
        profileState.setUsername(outputData.getUsername());
        profileState.setHeightText(String.valueOf(outputData.getHeight()));
        profileState.setWeightText(String.valueOf(outputData.getWeight()));
        profileState.setActivityLevel(ProfileEnumMapper.toOption(outputData.getActivityLevel()));
        profileState.setGoal(ProfileEnumMapper.toOption(outputData.getGoal()));
        profileState.setProfilePicturePath(outputData.getProfilePicturePath());
        profileState.setDateOfBirth(outputData.getDateOfBirth());
        profileState.setGender(ProfileEnumMapper.toOption(outputData.getGender()));
        profileState.setBio(outputData.getBio());
        profileState.setPreferredUnitSystem(ProfileEnumMapper.toOption(outputData.getPreferredUnitSystem()));
        profileState.setEquipment(ProfileEnumMapper.toEquipmentOptions(outputData.getEquipment()));
        profileState.setDietaryRestrictions(
                ProfileEnumMapper.toDietaryOptions(outputData.getDietaryRestrictions()));
        profileState.setPreferredWorkoutDays(outputData.getPreferredWorkoutDays());
        profileState.setPreferredWorkoutDurationMinutes(outputData.getPreferredWorkoutDurationMinutes());
        profileState.setPrivacySettings(ProfileEnumMapper.toPrivacyOptions(outputData.getPrivacySettings()));
        profileState.setProfileError(null);
        profileState.setSaveConfirmation("Profile saved.");
        this.profileViewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(final String errorMessage) {
        final ProfileState profileState = this.profileViewModel.getState();
        profileState.setSaveConfirmation(null);
        profileState.setProfileError(errorMessage);
        this.profileViewModel.firePropertyChanged();
    }
}

