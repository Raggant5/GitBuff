package interface_adapter.signup;

import static org.junit.jupiter.api.Assertions.assertEquals;

import interface_adapter.MainViewManagerModel;
import interface_adapter.ViewManagerModel;
import interface_adapter.login.LoginViewModel;
import interface_adapter.profile.ProfileViewModel;
import org.junit.jupiter.api.Test;
import use_case.signup.SignupOutputData;

class SignupPresenterTest {

    private SignupPresenter newPresenter(final ViewManagerModel viewManagerModel,
                                          final SignupViewModel signupViewModel,
                                          final LoginViewModel loginViewModel,
                                          final ProfileViewModel profileViewModel,
                                          final MainViewManagerModel mainViewManagerModel) {
        return new SignupPresenter(viewManagerModel, signupViewModel, loginViewModel, profileViewModel,
                mainViewManagerModel);
    }

    @Test
    void prepareSuccessViewUpdatesLoginProfileAndNavigation() {
        final ViewManagerModel viewManagerModel = new ViewManagerModel();
        final SignupViewModel signupViewModel = new SignupViewModel();
        final LoginViewModel loginViewModel = new LoginViewModel();
        final ProfileViewModel profileViewModel = new ProfileViewModel();
        final MainViewManagerModel mainViewManagerModel = new MainViewManagerModel();
        final SignupPresenter presenter = newPresenter(
                viewManagerModel, signupViewModel, loginViewModel, profileViewModel, mainViewManagerModel);

        presenter.prepareSuccessView(new SignupOutputData("amir", false));

        assertEquals("amir", loginViewModel.getState().getUsername());
        assertEquals("app shell", viewManagerModel.getState());
        assertEquals("profile", mainViewManagerModel.getState());
        assertEquals("amir", profileViewModel.getState().getUsername());
    }

    @Test
    void prepareFailViewSetsUsernameError() {
        final ViewManagerModel viewManagerModel = new ViewManagerModel();
        final SignupViewModel signupViewModel = new SignupViewModel();
        final LoginViewModel loginViewModel = new LoginViewModel();
        final ProfileViewModel profileViewModel = new ProfileViewModel();
        final MainViewManagerModel mainViewManagerModel = new MainViewManagerModel();
        final SignupPresenter presenter = newPresenter(
                viewManagerModel, signupViewModel, loginViewModel, profileViewModel, mainViewManagerModel);

        presenter.prepareFailView("User already exists.");

        assertEquals("User already exists.", signupViewModel.getState().getUsernameError());
    }

    @Test
    void switchToLoginViewWithCredentialsPopulatesLoginState() {
        final ViewManagerModel viewManagerModel = new ViewManagerModel();
        final SignupViewModel signupViewModel = new SignupViewModel();
        final LoginViewModel loginViewModel = new LoginViewModel();
        final ProfileViewModel profileViewModel = new ProfileViewModel();
        final MainViewManagerModel mainViewManagerModel = new MainViewManagerModel();
        final SignupPresenter presenter = newPresenter(
                viewManagerModel, signupViewModel, loginViewModel, profileViewModel, mainViewManagerModel);

        presenter.switchToLoginView("amir", "password");

        assertEquals("amir", loginViewModel.getState().getUsername());
        assertEquals("password", loginViewModel.getState().getPassword());
        assertEquals("", loginViewModel.getState().getLoginError());
        assertEquals(loginViewModel.getViewName(), viewManagerModel.getState());
    }

    @Test
    void switchToLoginViewWithNullCredentialsDefaultsToEmptyStrings() {
        final ViewManagerModel viewManagerModel = new ViewManagerModel();
        final SignupViewModel signupViewModel = new SignupViewModel();
        final LoginViewModel loginViewModel = new LoginViewModel();
        final ProfileViewModel profileViewModel = new ProfileViewModel();
        final MainViewManagerModel mainViewManagerModel = new MainViewManagerModel();
        final SignupPresenter presenter = newPresenter(
                viewManagerModel, signupViewModel, loginViewModel, profileViewModel, mainViewManagerModel);

        presenter.switchToLoginView(null, null);

        assertEquals("", loginViewModel.getState().getUsername());
        assertEquals("", loginViewModel.getState().getPassword());
    }
}
