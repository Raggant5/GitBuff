package use_case.nutrition.food.delete_food;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class DeleteFoodInteractorTest {

    @Test
    public void executePassesIdThroughToPresenter() {
        final DeleteFoodOutputData[] captured = new DeleteFoodOutputData[1];
        final DeleteFoodOutputBoundary presenter = outputData -> captured[0] = outputData;

        new DeleteFoodInteractor(presenter).execute(new DeleteFoodInputData(4));

        assertEquals(4, captured[0].getId());
    }

    @Test
    public void executeWithTempNegativeIdPassesThroughUnchanged() {
        final DeleteFoodOutputData[] captured = new DeleteFoodOutputData[1];
        final DeleteFoodOutputBoundary presenter = outputData -> captured[0] = outputData;

        new DeleteFoodInteractor(presenter).execute(new DeleteFoodInputData(-3));

        assertEquals(-3, captured[0].getId());
    }
}
