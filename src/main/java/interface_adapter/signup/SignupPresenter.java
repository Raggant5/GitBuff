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

    public SignupPresenter(ViewManagerModel viewManagerModel,
                           SignupViewModel signupViewModel,
                           LoginViewModel loginViewModel,
                           ProfileViewModel profileViewModel,
                           MainViewManagerModel mainViewManagerModel) {
        this.viewManagerModel = viewManagerModel;
        this.signupViewModel = signupViewModel;
        this.loginViewModel = loginViewModel;
        this.profileViewModel = profileViewModel;
        this.mainViewManagerModel = mainViewManagerModel;
    }

    @Override
    public void prepareSuccessView(SignupOutputData response) {
        // On success, switch to the login view.
        final LoginState loginState = loginViewModel.getState();
        loginState.setUsername(response.getUsername());
        this.loginViewModel.setState(loginState);
        loginViewModel.firePropertyChanged();
        viewManagerModel.setState("app shell");
        viewManagerModel.firePropertyChanged();
        mainViewManagerModel.setState("profile");
        mainViewManagerModel.firePropertyChanged();
        ProfileState profileState = profileViewModel.getState();
        profileState.setUsername(response.getUsername());
        profileViewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String error) {
        final SignupState signupState = signupViewModel.getState();
        signupState.setUsernameError(error);
        signupViewModel.firePropertyChanged();
    }

    @Override
    public void switchToLoginView() {
        viewManagerModel.setState(loginViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }
}
