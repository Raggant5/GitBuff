package interface_adapter.profile;

import use_case.login.LoginOutputData;
import use_case.session.UserLoggedInEvent;
import use_case.session.UserSessionObserver;

/**
 * Populates ProfileState from the login use case's output data.
 */
public class ProfileSessionObserver implements UserSessionObserver {

    private final ProfileViewModel profileViewModel;

    /**
     * Constructs a ProfileSessionObserver instance.
     *
     * @param profileViewModel view model for the user's profile
     */
    public ProfileSessionObserver(final ProfileViewModel profileViewModel) {
        this.profileViewModel = profileViewModel;
    }

    @Override
    public void onUserLoggedIn(final UserLoggedInEvent event) {
        final LoginOutputData response = event.getData();
        final ProfileState profileState = this.profileViewModel.getState();

        profileState.setUsername(response.getUsername());
        profileState.setHeightText(String.valueOf(response.getHeight()));
        profileState.setWeightText(String.valueOf(response.getWeight()));
        profileState.setActivityLevel(ProfileEnumMapper.toOption(response.getActivityLevel()));
        profileState.setGoal(ProfileEnumMapper.toOption(response.getGoal()));
        profileState.setProfilePicturePath(response.getProfilePicturePath());
        profileState.setDateOfBirth(response.getDateOfBirth());
        profileState.setGender(ProfileEnumMapper.toOption(response.getGender()));
        profileState.setBio(response.getBio());
        profileState.setPreferredUnitSystem(ProfileEnumMapper.toOption(response.getPreferredUnitSystem()));
        profileState.setEquipment(ProfileEnumMapper.toEquipmentOptions(response.getEquipment()));
        profileState.setDietaryRestrictions(
                ProfileEnumMapper.toDietaryOptions(response.getDietaryRestrictions()));
        profileState.setPreferredWorkoutDays(response.getPreferredWorkoutDays());
        profileState.setPreferredWorkoutDurationMinutes(response.getPreferredWorkoutDurationMinutes());
        profileState.setPrivacySettings(ProfileEnumMapper.toPrivacyOptions(response.getPrivacySettings()));
        profileState.setProfileError(null);
        profileState.setSaveConfirmation(null);

        this.profileViewModel.firePropertyChanged();
    }
}
