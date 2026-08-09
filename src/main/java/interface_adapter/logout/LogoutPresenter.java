package interface_adapter.logout;

import interface_adapter.ViewManagerModel;
import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;
import interface_adapter.profile.ProfileState;
import interface_adapter.profile.ProfileViewModel;
import use_case.logout.LogoutOutputBoundary;
import use_case.logout.LogoutOutputData;

/**
 * The Presenter for the Logout Use Case.
 */
public class LogoutPresenter implements LogoutOutputBoundary {

    private final ProfileViewModel profileViewModel;
    private final ViewManagerModel viewManagerModel;
    private final LoginViewModel loginViewModel;

    /**
     * Constructs a LogoutPresenter instance.
     *
     * @param viewManagerModel manager model for top-level view navigation.
     * @param profileViewModel view model for logged-in profile state.
     * @param loginViewModel view model for login state.
     */
    public LogoutPresenter(final ViewManagerModel viewManagerModel,
                           final ProfileViewModel profileViewModel,
                           final LoginViewModel loginViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.profileViewModel = profileViewModel;
        this.loginViewModel = loginViewModel;
    }

    @Override
    public void prepareSuccessView(final LogoutOutputData response) {
        final ProfileState profileState = this.profileViewModel.getState();
        profileState.setUsername("");
        this.profileViewModel.setState(profileState);

        final LoginState loginState = this.loginViewModel.getState();
        loginState.setUsername("");
        loginState.setPassword("");
        this.loginViewModel.setState(loginState);
        this.loginViewModel.firePropertyChanged();

        this.viewManagerModel.setState(this.loginViewModel.getViewName());
        this.viewManagerModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(final String error) {
        // Unused as logout operations do not produce functional failure states
    }
}
