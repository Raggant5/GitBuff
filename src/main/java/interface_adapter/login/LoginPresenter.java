package interface_adapter.login;

import interface_adapter.ViewManagerModel;
import interface_adapter.session.UserLoggedInEvent;
import interface_adapter.session.UserSessionEventBus;
import interface_adapter.signup.SignupViewModel;
import use_case.login.LoginOutputBoundary;
import use_case.login.LoginOutputData;

/**
 * The Presenter for the Login Use Case.
 *
 * <p>Previously, this class also seeded the profile, meals, and workout-history views, marked
 * the workout schedule as loading, synchronized the calendar, triggered a recommendation
 * refresh, and refreshed the dashboard - reaching directly into six unrelated collaborators
 * from one ~170-line method, a Single Responsibility Principle violation. It now does only what
 * a Login presenter should: update {@link LoginState} and decide where to navigate. Every other
 * reaction to a successful login is now a {@link interface_adapter.session.UserSessionObserver}
 * (Observer design pattern) subscribed to a {@link UserSessionEventBus} - see
 * {@code interface_adapter.profile.session.ProfileSessionObserver} and its siblings in
 * {@code interface_adapter.session}, wired up in {@code app.AppBuilder#addLoginUseCase()}. This
 * presenter no longer needs to change when any of those features change how they react to
 * login, and new features can react to login without this class changing at all (Open/Closed
 * Principle).
 */
public class LoginPresenter implements LoginOutputBoundary {

    private final LoginViewModel loginViewModel;
    private final ViewManagerModel viewManagerModel;
    private final SignupViewModel signupViewModel;
    private final UserSessionEventBus userSessionEventBus;

    /**
     * Constructs a LoginPresenter instance.
     *
     * @param viewManagerModel manager model for top-level view navigation
     * @param loginViewModel view model for login state
     * @param signupViewModel view model for signup state
     * @param userSessionEventBus event bus notified once login succeeds; every feature that
     *        needs to react to login subscribes to this instead of being passed into this
     *        presenter directly
     */
    public LoginPresenter(
            final ViewManagerModel viewManagerModel,
            final LoginViewModel loginViewModel,
            final SignupViewModel signupViewModel,
            final UserSessionEventBus userSessionEventBus) {
        this.viewManagerModel = viewManagerModel;
        this.loginViewModel = loginViewModel;
        this.signupViewModel = signupViewModel;
        this.userSessionEventBus = userSessionEventBus;
    }

    @Override
    public void prepareSuccessView(final LoginOutputData response) {
        updateLoginState(response);
        this.userSessionEventBus.publishUserLoggedIn(new UserLoggedInEvent(response));
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
