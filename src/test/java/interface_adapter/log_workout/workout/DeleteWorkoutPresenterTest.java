package interface_adapter.log_workout.workout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import use_case.log_workout.logged_workout.delete_workout.DeleteWorkoutOutputData;

/**
 * Unit tests for the Delete Workout Presenter.
 */
class DeleteWorkoutPresenterTest {

    @Test
    void prepareSuccessViewRemovesWorkoutAndClearsError() {
        final ViewWorkoutsViewModel viewWorkoutsViewModel = new ViewWorkoutsViewModel();
        viewWorkoutsViewModel.getState().setError("stale error");
        viewWorkoutsViewModel.getState().addWorkout(new LoggedWorkoutDisplayData(1, LocalDate.now(), List.of()));
        viewWorkoutsViewModel.getState().addWorkout(new LoggedWorkoutDisplayData(2, LocalDate.now(), List.of()));
        final DeleteWorkoutPresenter presenter = new DeleteWorkoutPresenter(viewWorkoutsViewModel);

        presenter.prepareSuccessView(new DeleteWorkoutOutputData(1));

        final ViewWorkoutsState state = viewWorkoutsViewModel.getState();
        assertEquals("", state.getError());
        assertEquals(1, state.getWorkouts().size());
        assertEquals(2, state.getWorkouts().get(0).getId());
    }

    @Test
    void prepareFailViewSetsError() {
        final ViewWorkoutsViewModel viewWorkoutsViewModel = new ViewWorkoutsViewModel();
        final DeleteWorkoutPresenter presenter = new DeleteWorkoutPresenter(viewWorkoutsViewModel);

        presenter.prepareFailView("Could not delete workout.");

        assertEquals("Could not delete workout.", viewWorkoutsViewModel.getState().getError());
        assertTrue(viewWorkoutsViewModel.getState().getWorkouts().isEmpty());
    }
}
