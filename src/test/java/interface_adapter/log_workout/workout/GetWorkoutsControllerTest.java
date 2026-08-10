package interface_adapter.log_workout.workout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import interface_adapter.login.LoginViewModel;
import use_case.log_workout.logged_workout.get_workouts.GetWorkoutsInputBoundary;
import use_case.log_workout.logged_workout.get_workouts.GetWorkoutsInputData;

/**
 * Unit tests for the Get Workouts Controller.
 */
class GetWorkoutsControllerTest {

    @Test
    void executeDelegatesToInteractorWhenUsernameIsPresent() {
        final FakeGetWorkoutsInteractor interactor = new FakeGetWorkoutsInteractor();
        final LoginViewModel loginViewModel = new LoginViewModel();
        loginViewModel.getState().setUsername("aahir");
        final GetWorkoutsController controller = new GetWorkoutsController(interactor, loginViewModel);

        controller.execute();

        assertTrue(interactor.executeCalled);
        assertEquals("aahir", interactor.receivedInputData.getUserId());
    }

    @Test
    void executeDoesNothingWhenUsernameIsNull() {
        final FakeGetWorkoutsInteractor interactor = new FakeGetWorkoutsInteractor();
        final LoginViewModel loginViewModel = new LoginViewModel();
        loginViewModel.getState().setUsername(null);
        final GetWorkoutsController controller = new GetWorkoutsController(interactor, loginViewModel);

        controller.execute();

        assertFalse(interactor.executeCalled);
    }

    @Test
    void executeDoesNothingWhenUsernameIsBlank() {
        final FakeGetWorkoutsInteractor interactor = new FakeGetWorkoutsInteractor();
        final LoginViewModel loginViewModel = new LoginViewModel();
        loginViewModel.getState().setUsername("   ");
        final GetWorkoutsController controller = new GetWorkoutsController(interactor, loginViewModel);

        controller.execute();

        assertFalse(interactor.executeCalled);
    }

    private static final class FakeGetWorkoutsInteractor implements GetWorkoutsInputBoundary {
        private boolean executeCalled;
        private GetWorkoutsInputData receivedInputData;

        @Override
        public void execute(GetWorkoutsInputData inputData) {
            this.executeCalled = true;
            this.receivedInputData = inputData;
        }
    }
}
