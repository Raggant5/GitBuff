package interface_adapter.profile;

import interface_adapter.ViewModel;

/**
 * The View Model for the Profile View.
 */
public class ProfileViewModel extends ViewModel<ProfileState> {

    public ProfileViewModel() {
        super("profile");
        setState(new ProfileState());
    }
}
