package use_case.nutrition.food.change_serving_size;

import static org.junit.jupiter.api.Assertions.assertEquals;

import entity.FoodUnit;
import org.junit.jupiter.api.Test;

public class ChangeServingSizeInputDataTest {

    @Test
    public void constructorStoresAllFields() {
        final ChangeServingSizeInputData inputData = new ChangeServingSizeInputData(
                FoodUnit.CUP, 50.0, 100.0, 200.0, 20.0, 15.0, 5.0);

        assertEquals(FoodUnit.CUP, inputData.getUnit());
        assertEquals(50.0, inputData.getOriginalServingGrams(), 0.0001);
        assertEquals(100.0, inputData.getServingGrams(), 0.0001);
        assertEquals(200.0, inputData.getServingCalories(), 0.0001);
        assertEquals(20.0, inputData.getServingProtein(), 0.0001);
        assertEquals(15.0, inputData.getServingCarbs(), 0.0001);
        assertEquals(5.0, inputData.getServingFat(), 0.0001);
    }
}
