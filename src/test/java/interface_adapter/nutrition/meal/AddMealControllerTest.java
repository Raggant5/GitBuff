package interface_adapter.nutrition.meal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import entity.FoodUnit;
import interface_adapter.login.LoginViewModel;
import interface_adapter.nutrition.food.FoodEntryDisplayData;
import interface_adapter.nutrition.food.FoodNutritionDisplayData;
import interface_adapter.nutrition.food.FoodUnitOption;
import use_case.nutrition.meal.add_meal.AddMealInputBoundary;
import use_case.nutrition.meal.add_meal.AddMealInputData;

/**
 * Unit tests for the Add Meal Controller.
 */
class AddMealControllerTest {

    private static final double QUANTITY = 2.0;
    private static final double GRAMS = 200.0;
    private static final double DELTA = 0.0001;

    @Test
    void executeBuildsInputDataFromLoggedInUserAndFoodEntries() {
        final LoginViewModel loginViewModel = new LoginViewModel();
        loginViewModel.getState().setUsername("aahir");
        final FakeAddMealInputBoundary interactor = new FakeAddMealInputBoundary();
        final AddMealController controller = new AddMealController(interactor, loginViewModel);

        final FoodNutritionDisplayData nutrition = new FoodNutritionDisplayData("100", "10", "20", "5");
        final FoodEntryDisplayData food = new FoodEntryDisplayData(1, "Apple", nutrition, QUANTITY,
                FoodUnitOption.GRAM, GRAMS);

        controller.execute("Breakfast", List.of(food));

        assertTrue(interactor.executeCalled);
        final AddMealInputData inputData = interactor.receivedInputData;
        assertEquals("Breakfast", inputData.getName());
        assertEquals("aahir", inputData.getUserId());
        assertEquals(LocalDate.now(), inputData.getDate());
        assertEquals(1, inputData.getFoodEntries().size());
        assertEquals(1, inputData.getFoodEntries().get(0).getId());
        assertEquals("Apple", inputData.getFoodEntries().get(0).getFoodName());
        assertEquals("100", inputData.getFoodEntries().get(0).getCalories());
        assertEquals(QUANTITY, inputData.getFoodEntries().get(0).getQuantity(), DELTA);
        assertEquals(FoodUnit.GRAM, inputData.getFoodEntries().get(0).getUnit());
        assertEquals(GRAMS, inputData.getFoodEntries().get(0).getGrams(), DELTA);
    }

    @Test
    void executeWithEmptyFoodListProducesEmptyFoodEntries() {
        final LoginViewModel loginViewModel = new LoginViewModel();
        loginViewModel.getState().setUsername("aahir");
        final FakeAddMealInputBoundary interactor = new FakeAddMealInputBoundary();
        final AddMealController controller = new AddMealController(interactor, loginViewModel);

        controller.execute("Empty", Collections.emptyList());

        assertTrue(interactor.executeCalled);
        assertTrue(interactor.receivedInputData.getFoodEntries().isEmpty());
    }

    private static final class FakeAddMealInputBoundary implements AddMealInputBoundary {
        private boolean executeCalled;
        private AddMealInputData receivedInputData;

        @Override
        public void execute(final AddMealInputData addMealInputData) {
            this.executeCalled = true;
            this.receivedInputData = addMealInputData;
        }
    }
}
