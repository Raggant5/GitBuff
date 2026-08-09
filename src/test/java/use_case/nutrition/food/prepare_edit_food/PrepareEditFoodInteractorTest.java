package use_case.nutrition.food.prepare_edit_food;

import static org.junit.jupiter.api.Assertions.assertEquals;

import entity.FoodUnit;
import org.junit.jupiter.api.Test;
import use_case.nutrition.food.FoodNutritionInput;

public class PrepareEditFoodInteractorTest {

    @Test
    public void executePassesInputFieldsThroughUnchanged() {
        final PrepareEditFoodInputData inputData = new PrepareEditFoodInputData(9, "Chicken Breast",
                new FoodNutritionInput("200", "20", "0", "5"), 1.0, FoodUnit.GRAM, 150.0);
        final PrepareEditFoodOutputData[] captured = new PrepareEditFoodOutputData[1];

        final PrepareEditFoodOutputBoundary presenter = outputData -> captured[0] = outputData;

        new PrepareEditFoodInteractor(presenter).execute(inputData);

        assertEquals(9, captured[0].getId());
        assertEquals("Chicken Breast", captured[0].getFoodName());
        assertEquals(200.0, captured[0].getNutrition().getCalories(), 0.0001);
        assertEquals(150.0, captured[0].getGrams(), 0.0001);
        assertEquals(1.0, captured[0].getQuantity(), 0.0001);
        assertEquals(FoodUnit.GRAM, captured[0].getUnit());
    }

    @Test
    public void executeWithNonNumericNutritionDefaultsToZero() {
        final PrepareEditFoodInputData inputData = new PrepareEditFoodInputData(9, "Chicken Breast",
                new FoodNutritionInput("not-a-number", "20", "0", "5"), 1.0, FoodUnit.GRAM, 150.0);
        final PrepareEditFoodOutputData[] captured = new PrepareEditFoodOutputData[1];

        final PrepareEditFoodOutputBoundary presenter = outputData -> captured[0] = outputData;

        new PrepareEditFoodInteractor(presenter).execute(inputData);

        assertEquals(0.0, captured[0].getNutrition().getCalories(), 0.0001);
    }
}
