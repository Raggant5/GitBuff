package use_case.log_workout.logged_workout.get_workouts;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;

import entity.ExercisePerformed;
import entity.LoggedWorkout;
import entity.LoggedWorkoutFactory;
import org.junit.jupiter.api.Test;
import use_case.DataAccessException;

class GetWorkoutsInteractorTest {

    @Test
    void executeReturnsWorkoutsForUser() {
        final LoggedWorkout workout = new LoggedWorkoutFactory().create("amir", LocalDate.of(2026, 8, 6));
        workout.setId(1);
        final FakeDataAccessObject dataAccessObject = new FakeDataAccessObject(List.of(workout));
        final CapturingPresenter presenter = new CapturingPresenter();

        new GetWorkoutsInteractor(presenter, dataAccessObject).execute(new GetWorkoutsInputData("amir"));

        assertEquals(1, presenter.successData.getWorkouts().size());
        assertEquals(LocalDate.of(2026, 8, 6), presenter.successData.getWorkouts().get(0).getDate());
    }

    @Test
    void executeWhenDataAccessThrowsFails() {
        final FakeDataAccessObject dataAccessObject = new FakeDataAccessObject(List.of()) {
            @Override
            public List<LoggedWorkout> getWorkoutsForUser(final String userId) {
                throw new DataAccessException("Database unavailable");
            }
        };
        final CapturingPresenter presenter = new CapturingPresenter();

        new GetWorkoutsInteractor(presenter, dataAccessObject).execute(new GetWorkoutsInputData("amir"));

        assertEquals("Unable to load workout history. Please try again.", presenter.failMessage);
    }

    private static class FakeDataAccessObject implements ViewWorkoutDataAccessInterface {
        private final List<LoggedWorkout> workouts;

        private FakeDataAccessObject(final List<LoggedWorkout> workouts) {
            this.workouts = workouts;
        }

        @Override
        public List<LoggedWorkout> getWorkoutsForUser(final String userId) {
            return workouts;
        }

        @Override
        public List<ExercisePerformed> getExercisesForWorkout(final int workoutId) {
            return List.of();
        }

        @Override
        public LoggedWorkout getWorkoutById(final int workoutId) {
            return workouts.isEmpty() ? null : workouts.get(0);
        }
    }

    private static final class CapturingPresenter implements GetWorkoutsOutputBoundary {
        private GetWorkoutsOutputData successData;
        private String failMessage;

        @Override
        public void prepareSuccessView(final GetWorkoutsOutputData outputData) {
            this.successData = outputData;
        }

        @Override
        public void prepareFailView(final String errorMessage) {
            this.failMessage = errorMessage;
        }
    }
}
