package interface_adapter.logout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import interface_adapter.ViewManagerModel;
import interface_adapter.login.LoginViewModel;
import interface_adapter.profile.ProfileViewModel;
import org.junit.jupiter.api.Test;
import use_case.logout.LogoutOutputData;

class LogoutPresenterTest {

    @Test
    void prepareSuccessViewClearsProfileAndLoginStateAndNavigatesToLogin() {
        final ViewManagerModel viewManagerModel = new ViewManagerModel();
        final ProfileViewModel profileViewModel = new ProfileViewModel();
        profileViewModel.getState().setUsername("amir");
        final LoginViewModel loginViewModel = new LoginViewModel();
        loginViewModel.getState().setUsername("amir");
        loginViewModel.getState().setPassword("password");
        final LogoutPresenter presenter = new LogoutPresenter(viewManagerModel, profileViewModel, loginViewModel);

        presenter.prepareSuccessView(new LogoutOutputData("amir", false));

        assertEquals("", profileViewModel.getState().getUsername());
        assertEquals("", loginViewModel.getState().getUsername());
        assertEquals("", loginViewModel.getState().getPassword());
        assertEquals(loginViewModel.getViewName(), viewManagerModel.getState());
    }

    @Test
    void prepareFailViewDoesNothing() {
        final ViewManagerModel viewManagerModel = new ViewManagerModel();
        final ProfileViewModel profileViewModel = new ProfileViewModel();
        final LoginViewModel loginViewModel = new LoginViewModel();
        final LogoutPresenter presenter = new LogoutPresenter(viewManagerModel, profileViewModel, loginViewModel);

        presenter.prepareFailView("unused");
    }
}
