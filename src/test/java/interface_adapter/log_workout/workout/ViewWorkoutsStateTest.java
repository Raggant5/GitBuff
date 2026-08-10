package interface_adapter.log_workout.workout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the View Workouts State.
 */
class ViewWorkoutsStateTest {

    @Test
    void addWorkoutAppendsToWorkoutsList() {
        final ViewWorkoutsState state = new ViewWorkoutsState();

        state.addWorkout(new LoggedWorkoutDisplayData(1, LocalDate.now(), List.of()));

        assertEquals(1, state.getWorkouts().size());
        assertEquals(1, state.getWorkouts().get(0).getId());
    }

    @Test
    void replaceWorkoutReplacesMatchingId() {
        final ViewWorkoutsState state = new ViewWorkoutsState();
        state.addWorkout(new LoggedWorkoutDisplayData(1, LocalDate.of(2024, 1, 1), List.of()));
        state.addWorkout(new LoggedWorkoutDisplayData(2, LocalDate.of(2024, 2, 1), List.of()));

        state.replaceWorkout(new LoggedWorkoutDisplayData(1, LocalDate.of(2024, 5, 5), List.of()));

        assertEquals(2, state.getWorkouts().size());
        assertEquals(LocalDate.of(2024, 5, 5), state.getWorkouts().get(0).getDate());
        assertEquals(LocalDate.of(2024, 2, 1), state.getWorkouts().get(1).getDate());
    }

    @Test
    void replaceWorkoutWithUnknownIdLeavesListUnchanged() {
        final ViewWorkoutsState state = new ViewWorkoutsState();
        state.addWorkout(new LoggedWorkoutDisplayData(1, LocalDate.of(2024, 1, 1), List.of()));

        state.replaceWorkout(new LoggedWorkoutDisplayData(99, LocalDate.of(2024, 5, 5), List.of()));

        assertEquals(1, state.getWorkouts().size());
        assertEquals(LocalDate.of(2024, 1, 1), state.getWorkouts().get(0).getDate());
    }

    @Test
    void removeWorkoutRemovesMatchingId() {
        final ViewWorkoutsState state = new ViewWorkoutsState();
        state.addWorkout(new LoggedWorkoutDisplayData(1, LocalDate.now(), List.of()));
        state.addWorkout(new LoggedWorkoutDisplayData(2, LocalDate.now(), List.of()));

        state.removeWorkout(1);

        assertEquals(1, state.getWorkouts().size());
        assertEquals(2, state.getWorkouts().get(0).getId());
    }

    @Test
    void errorDefaultsToEmptyStringAndIsSettable() {
        final ViewWorkoutsState state = new ViewWorkoutsState();

        assertTrue(state.getError().isEmpty());

        state.setError("boom");

        assertEquals("boom", state.getError());
    }
}
