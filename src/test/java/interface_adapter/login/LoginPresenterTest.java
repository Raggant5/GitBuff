package interface_adapter.login;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Set;

import entity.ActivityLevel;
import entity.FitnessGoal;
import entity.Gender;
import entity.UnitSystem;
import interface_adapter.ViewManagerModel;
import interface_adapter.signup.SignupViewModel;
import org.junit.jupiter.api.Test;
import use_case.login.LoginOutputData;

class LoginPresenterTest {

    private static LoginOutputData buildOutputData() {
        return new LoginOutputData(
                "amir", 1.8f, 80f, ActivityLevel.VERY_ACTIVE, FitnessGoal.MUSCLE_AND_STRENGTH_GAIN,
                "/tmp/pic.png", null, Gender.MALE, "bio", UnitSystem.METRIC,
                Set.of(), Set.of(), Set.of(), 45, Set.of(), false);
    }

    @Test
    void prepareSuccessViewUpdatesLoginStateAndNavigatesToAppShell() {
        final ViewManagerModel viewManagerModel = new ViewManagerModel();
        final LoginViewModel loginViewModel = new LoginViewModel();
        final SignupViewModel signupViewModel = new SignupViewModel();
        final LoginPresenter presenter = new LoginPresenter(viewManagerModel, loginViewModel, signupViewModel);

        presenter.prepareSuccessView(buildOutputData());

        assertEquals("amir", loginViewModel.getState().getUsername());
        assertNull(loginViewModel.getState().getLoginError());
        assertEquals("app shell", viewManagerModel.getState());
    }

    @Test
    void prepareFailViewSetsLoginError() {
        final ViewManagerModel viewManagerModel = new ViewManagerModel();
        final LoginViewModel loginViewModel = new LoginViewModel();
        final SignupViewModel signupViewModel = new SignupViewModel();
        final LoginPresenter presenter = new LoginPresenter(viewManagerModel, loginViewModel, signupViewModel);

        presenter.prepareFailView("Incorrect password.");

        assertEquals("Incorrect password.", loginViewModel.getState().getLoginError());
    }

    @Test
    void switchToSignupViewNavigatesToSignupViewName() {
        final ViewManagerModel viewManagerModel = new ViewManagerModel();
        final LoginViewModel loginViewModel = new LoginViewModel();
        final SignupViewModel signupViewModel = new SignupViewModel();
        final LoginPresenter presenter = new LoginPresenter(viewManagerModel, loginViewModel, signupViewModel);

        presenter.switchToSignupView();

        assertEquals(signupViewModel.getViewName(), viewManagerModel.getState());
    }
}
