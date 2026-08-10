package interface_adapter.nutrition.meal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import interface_adapter.login.LoginViewModel;
import use_case.nutrition.meal.get_meals.GetMealsInputBoundary;
import use_case.nutrition.meal.get_meals.GetMealsInputData;

/**
 * Unit tests for the Get Meals Controller.
 */
class GetMealsControllerTest {

    @Test
    void executeCallsInteractorWhenUsernamePresent() {
        final LoginViewModel loginViewModel = new LoginViewModel();
        loginViewModel.getState().setUsername("aahir");
        final FakeGetMealsInputBoundary interactor = new FakeGetMealsInputBoundary();
        final GetMealsController controller = new GetMealsController(interactor, loginViewModel);

        controller.execute();

        assertTrue(interactor.executeCalled);
        assertEquals("aahir", interactor.receivedInputData.getUserId());
    }

    @Test
    void executeDoesNothingWhenUsernameIsNull() {
        final LoginViewModel loginViewModel = new LoginViewModel();
        loginViewModel.getState().setUsername(null);
        final FakeGetMealsInputBoundary interactor = new FakeGetMealsInputBoundary();
        final GetMealsController controller = new GetMealsController(interactor, loginViewModel);

        controller.execute();

        assertFalse(interactor.executeCalled);
    }

    @Test
    void executeDoesNothingWhenUsernameIsBlank() {
        final LoginViewModel loginViewModel = new LoginViewModel();
        loginViewModel.getState().setUsername("   ");
        final FakeGetMealsInputBoundary interactor = new FakeGetMealsInputBoundary();
        final GetMealsController controller = new GetMealsController(interactor, loginViewModel);

        controller.execute();

        assertFalse(interactor.executeCalled);
    }

    private static final class FakeGetMealsInputBoundary implements GetMealsInputBoundary {
        private boolean executeCalled;
        private GetMealsInputData receivedInputData;

        @Override
        public void execute(final GetMealsInputData inputData) {
            this.executeCalled = true;
            this.receivedInputData = inputData;
        }
    }
}
