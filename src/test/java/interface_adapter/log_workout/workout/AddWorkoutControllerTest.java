package interface_adapter.log_workout.workout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import interface_adapter.log_workout.exercise.ExercisePerformedDisplayData;
import interface_adapter.log_workout.exercise.StrengthDetailsDisplayData;
import interface_adapter.login.LoginViewModel;
import use_case.log_workout.logged_workout.add_workout.AddWorkoutInputBoundary;
import use_case.log_workout.logged_workout.add_workout.AddWorkoutInputData;

/**
 * Unit tests for the Add Workout Controller.
 */
class AddWorkoutControllerTest {

    @Test
    void executeMapsExercisesAndDelegatesToInteractor() {
        final FakeAddWorkoutInteractor interactor = new FakeAddWorkoutInteractor();
        final LoginViewModel loginViewModel = new LoginViewModel();
        loginViewModel.getState().setUsername("aahir");
        final AddWorkoutController controller = new AddWorkoutController(interactor, loginViewModel);

        final ExercisePerformedDisplayData strength = new ExercisePerformedDisplayData(1, "Bench Press",
                new StrengthDetailsDisplayData("3", "10", "60"), 0, null, false);
        final ExercisePerformedDisplayData cardio = new ExercisePerformedDisplayData(2, "Running",
                new StrengthDetailsDisplayData("", "", ""), 30, 5.0, true);

        controller.execute(Arrays.asList(strength, cardio));

        assertTrue(interactor.executeCalled);
        final AddWorkoutInputData receivedData = interactor.receivedInputData;
        assertEquals("aahir", receivedData.getUserId());
        assertEquals(LocalDate.now(), receivedData.getDate());
        assertEquals(2, receivedData.getExercises().size());
        assertEquals("Bench Press", receivedData.getExercises().get(0).getExerciseName());
        assertEquals("3", receivedData.getExercises().get(0).getSets());
        assertEquals("Running", receivedData.getExercises().get(1).getExerciseName());
        assertTrue(receivedData.getExercises().get(1).getIsCardio());
        assertEquals(5.0, receivedData.getExercises().get(1).getDistanceKm());
    }

    @Test
    void executeWithEmptyListDelegatesWithNoExercises() {
        final FakeAddWorkoutInteractor interactor = new FakeAddWorkoutInteractor();
        final LoginViewModel loginViewModel = new LoginViewModel();
        loginViewModel.getState().setUsername("aahir");
        final AddWorkoutController controller = new AddWorkoutController(interactor, loginViewModel);

        controller.execute(List.of());

        assertTrue(interactor.executeCalled);
        assertTrue(interactor.receivedInputData.getExercises().isEmpty());
    }

    private static final class FakeAddWorkoutInteractor implements AddWorkoutInputBoundary {
        private boolean executeCalled;
        private AddWorkoutInputData receivedInputData;

        @Override
        public void execute(AddWorkoutInputData addWorkoutInputData) {
            this.executeCalled = true;
            this.receivedInputData = addWorkoutInputData;
        }
    }
}
