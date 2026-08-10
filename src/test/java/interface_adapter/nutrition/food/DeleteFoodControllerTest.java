package interface_adapter.nutrition.food;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import use_case.nutrition.food.delete_food.DeleteFoodInputBoundary;
import use_case.nutrition.food.delete_food.DeleteFoodInputData;

/**
 * Unit tests for the Delete Food Controller.
 */
class DeleteFoodControllerTest {

    @Test
    void executeDelegatesToInteractorWithGivenId() {
        final FakeDeleteFoodInteractor interactor = new FakeDeleteFoodInteractor();
        final DeleteFoodController controller = new DeleteFoodController(interactor);

        controller.execute(7);

        assertTrue(interactor.executeCalled);
        assertEquals(7, interactor.receivedInputData.getId());
    }

    private static final class FakeDeleteFoodInteractor implements DeleteFoodInputBoundary {
        private boolean executeCalled;
        private DeleteFoodInputData receivedInputData;

        @Override
        public void execute(DeleteFoodInputData deleteFoodInputData) {
            this.executeCalled = true;
            this.receivedInputData = deleteFoodInputData;
        }
    }
}
