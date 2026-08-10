package use_case.nutrition.meal.delete_meal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import use_case.DataAccessException;

public class DeleteMealInteractorTest {

    @Test
    public void executeDeletesMealAndNotifiesPresenter() {
        final List<Integer> deletedIds = new ArrayList<>();
        final DeleteMealDataAccessInterface dataAccess = deletedIds::add;
        final DeleteMealOutputData[] captured = new DeleteMealOutputData[1];
        final DeleteMealOutputBoundary presenter = new DeleteMealOutputBoundary() {
            @Override
            public void prepareSuccessView(DeleteMealOutputData outputData) {
                captured[0] = outputData;
            }

            @Override
            public void prepareFailView(String errorMessage) {
                throw new AssertionError("Expected success view, got failure: " + errorMessage);
            }
        };

        new DeleteMealInteractor(presenter, dataAccess).execute(new DeleteMealInputData(6));

        assertTrue(deletedIds.contains(6));
        assertEquals(6, captured[0].getMealId());
    }

    @Test
    public void executeWhenDataAccessThrowsPreparesFailView() {
        final DeleteMealDataAccessInterface failingDataAccess = mealId -> {
            throw new DataAccessException("db unavailable");
        };
        final boolean[] failed = {false};
        final DeleteMealOutputBoundary presenter = new DeleteMealOutputBoundary() {
            @Override
            public void prepareSuccessView(DeleteMealOutputData outputData) {
                throw new AssertionError("Expected failure view");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                failed[0] = true;
                assertFalse(errorMessage.isEmpty());
            }
        };

        new DeleteMealInteractor(presenter, failingDataAccess).execute(new DeleteMealInputData(6));

        assertTrue(failed[0]);
    }
}
