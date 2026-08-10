package interface_adapter.signup;

import interface_adapter.MainViewManagerModel;
import interface_adapter.ViewManagerModel;
import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;
import interface_adapter.profile.ProfileState;
import interface_adapter.profile.ProfileViewModel;
import use_case.signup.SignupOutputBoundary;
import use_case.signup.SignupOutputData;

/**
 * The Presenter for the Signup Use Case.
 */
public class SignupPresenter implements SignupOutputBoundary {

    private final SignupViewModel signupViewModel;
    private final LoginViewModel loginViewModel;
    private final ProfileViewModel profileViewModel;
    private final ViewManagerModel viewManagerModel;
    private final MainViewManagerModel mainViewManagerModel;

    /**
     * Constructs a SignupPresenter instance.
     *
     * @param viewManagerModel manager model for top-level view navigation.
     * @param signupViewModel view model for signup state.
     * @param loginViewModel view model for login state.
     * @param profileViewModel view model for user profile state.
     * @param mainViewManagerModel manager model for main tab navigation.
     */
    public SignupPresenter(final ViewManagerModel viewManagerModel,
                           final SignupViewModel signupViewModel,
                           final LoginViewModel loginViewModel,
                           final ProfileViewModel profileViewModel,
                           final MainViewManagerModel mainViewManagerModel) {
        this.viewManagerModel = viewManagerModel;
        this.signupViewModel = signupViewModel;
        this.loginViewModel = loginViewModel;
        this.profileViewModel = profileViewModel;
        this.mainViewManagerModel = mainViewManagerModel;
    }

    @Override
    public void prepareSuccessView(final SignupOutputData response) {
        final LoginState loginState = this.loginViewModel.getState();
        loginState.setUsername(response.getUsername());
        this.loginViewModel.setState(loginState);
        this.loginViewModel.firePropertyChanged();

        this.viewManagerModel.setState("app shell");
        this.viewManagerModel.firePropertyChanged();

        this.mainViewManagerModel.setState("profile");
        this.mainViewManagerModel.firePropertyChanged();

        final ProfileState profileState = this.profileViewModel.getState();
        profileState.setUsername(response.getUsername());
        this.profileViewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(final String error) {
        final SignupState signupState = this.signupViewModel.getState();
        signupState.setUsernameError(error);
        this.signupViewModel.firePropertyChanged();
    }

    @Override
    public void switchToLoginView(final String username, final String password) {
        final LoginState loginState = this.loginViewModel.getState();
        final String safeUsername;
        if (username == null) {
            safeUsername = "";
        }
        else {
            safeUsername = username;
        }
        final String safePassword;
        if (password == null) {
            safePassword = "";
        }
        else {
            safePassword = password;
        }
        loginState.setUsername(safeUsername);
        loginState.setPassword(safePassword);
        loginState.setLoginError("");
        this.loginViewModel.setState(loginState);
        this.loginViewModel.firePropertyChanged();

        this.viewManagerModel.setState(this.loginViewModel.getViewName());
        this.viewManagerModel.firePropertyChanged();
    }
}
