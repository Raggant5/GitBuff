package interface_adapter.nutrition.food;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import entity.FoodUnit;
import use_case.nutrition.food.create_food.AddFoodEntryInputBoundary;
import use_case.nutrition.food.create_food.AddFoodEntryInputData;

/**
 * Unit tests for the Add Food Controller.
 */
class AddFoodControllerTest {

    @Test
    void executeDelegatesToInteractorWithConvertedInputData() {
        final FakeAddFoodEntryInteractor interactor = new FakeAddFoodEntryInteractor();
        final AddFoodController controller = new AddFoodController(interactor);

        final FoodNutritionDisplayData nutrition = new FoodNutritionDisplayData("100", "10", "20", "5");

        controller.execute("Banana", nutrition, "2", FoodUnitOption.GRAM, "150");

        assertTrue(interactor.executeCalled);
        final AddFoodEntryInputData received = interactor.receivedInputData;
        assertEquals("Banana", received.getFoodName());
        assertEquals("100", received.getFoodNutrition().getCalories());
        assertEquals("10", received.getFoodNutrition().getProtein());
        assertEquals("20", received.getFoodNutrition().getCarbs());
        assertEquals("5", received.getFoodNutrition().getFat());
        assertEquals("2", received.getQuantity());
        assertEquals(FoodUnit.GRAM, received.getUnit());
        assertEquals("150", received.getGrams());
    }

    private static final class FakeAddFoodEntryInteractor implements AddFoodEntryInputBoundary {
        private boolean executeCalled;
        private AddFoodEntryInputData receivedInputData;

        @Override
        public void execute(AddFoodEntryInputData addFoodEntryInputData) {
            this.executeCalled = true;
            this.receivedInputData = addFoodEntryInputData;
        }
    }
}
