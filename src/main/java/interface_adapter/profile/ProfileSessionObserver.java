package interface_adapter.profile;

import interface_adapter.session.UserLoggedInEvent;
import interface_adapter.session.UserSessionObserver;
import use_case.login.LoginOutputData;

/**
 * Populates {@link ProfileState} from the login use case's output data.
 *
 * <p>Extracted from {@code interface_adapter.login.LoginPresenter}, which previously did this
 * inline as one of several unrelated responsibilities crammed into one method. Translates the
 * entity-layer enums carried by {@link LoginOutputData} to the interface_adapter-layer
 * {@code *Option} enums via {@link ProfileEnumMapper}, exactly as
 * {@link ProfilePresenter} does after a profile edit - login and edit-profile now share the
 * same translation boundary logic instead of each re-implementing it.
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
        final LoginOutputData response = event.data();
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


