package use_case.log_workout.logged_workout.delete_workout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import use_case.DataAccessException;

public class DeleteWorkoutInteractorTest {

    @Test
    public void executeDeletesWorkoutAndNotifiesPresenter() {
        final List<Integer> deletedIds = new ArrayList<>();
        final DeleteWorkoutDataAccessInterface dataAccess = deletedIds::add;
        final DeleteWorkoutOutputData[] captured = new DeleteWorkoutOutputData[1];
        final DeleteWorkoutOutputBoundary presenter = new DeleteWorkoutOutputBoundary() {
            @Override
            public void prepareSuccessView(DeleteWorkoutOutputData outputData) {
                captured[0] = outputData;
            }

            @Override
            public void prepareFailView(String errorMessage) {
                throw new AssertionError("Expected success view, got failure: " + errorMessage);
            }
        };

        new DeleteWorkoutInteractor(presenter, dataAccess).execute(new DeleteWorkoutInputData(8));

        assertTrue(deletedIds.contains(8));
        assertEquals(8, captured[0].getWorkoutId());
    }

    @Test
    public void executeWhenDataAccessThrowsPreparesFailView() {
        final DeleteWorkoutDataAccessInterface failingDataAccess = workoutId -> {
            throw new DataAccessException("db unavailable");
        };
        final boolean[] failed = {false};
        final DeleteWorkoutOutputBoundary presenter = new DeleteWorkoutOutputBoundary() {
            @Override
            public void prepareSuccessView(DeleteWorkoutOutputData outputData) {
                throw new AssertionError("Expected failure view");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                failed[0] = true;
                assertFalse(errorMessage.isEmpty());
            }
        };

        new DeleteWorkoutInteractor(presenter, failingDataAccess).execute(new DeleteWorkoutInputData(8));

        assertTrue(failed[0]);
    }
}
