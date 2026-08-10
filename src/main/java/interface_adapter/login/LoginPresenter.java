package interface_adapter.login;

import interface_adapter.ViewManagerModel;
import interface_adapter.signup.SignupViewModel;
import use_case.login.LoginOutputBoundary;
import use_case.login.LoginOutputData;

/**
 * The Presenter for the Login Use Case.
 */
public class LoginPresenter implements LoginOutputBoundary {

    private final LoginViewModel loginViewModel;
    private final ViewManagerModel viewManagerModel;
    private final SignupViewModel signupViewModel;

    /**
     * Constructs a LoginPresenter instance.
     *
     * @param viewManagerModel manager model for top-level view navigation
     * @param loginViewModel view model for login state
     * @param signupViewModel view model for signup state
     */
    public LoginPresenter(
            final ViewManagerModel viewManagerModel,
            final LoginViewModel loginViewModel,
            final SignupViewModel signupViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.loginViewModel = loginViewModel;
        this.signupViewModel = signupViewModel;
    }

    @Override
    public void prepareSuccessView(final LoginOutputData response) {
        updateLoginState(response);
        navigateToAppShell();
    }

    private void updateLoginState(final LoginOutputData response) {
        final LoginState loginState = this.loginViewModel.getState();
        loginState.setUsername(response.getUsername());
        loginState.setLoginError(null);
        this.loginViewModel.firePropertyChanged();
    }

    private void navigateToAppShell() {
        this.viewManagerModel.setState("app shell");
        this.viewManagerModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(final String error) {
        final LoginState loginState = this.loginViewModel.getState();
        loginState.setLoginError(error);
        this.loginViewModel.firePropertyChanged();
    }

    @Override
    public void switchToSignupView() {
        this.viewManagerModel.setState(this.signupViewModel.getViewName());
        this.viewManagerModel.firePropertyChanged();
    }
}
