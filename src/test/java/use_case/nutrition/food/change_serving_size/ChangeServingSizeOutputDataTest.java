package use_case.nutrition.food.change_serving_size;

import static org.junit.jupiter.api.Assertions.assertEquals;

import entity.FoodUnit;
import org.junit.jupiter.api.Test;

public class ChangeServingSizeOutputDataTest {

    @Test
    public void constructorStoresAllFields() {
        final ChangeServingSizeOutputData outputData = new ChangeServingSizeOutputData(
                FoodUnit.CUP, 240.0, 400.0, 40.0, 30.0, 10.0);

        assertEquals(FoodUnit.CUP, outputData.getUnit());
        assertEquals(240.0, outputData.getServingGrams(), 0.0001);
        assertEquals(400.0, outputData.getServingCalories(), 0.0001);
        assertEquals(40.0, outputData.getServingProtein(), 0.0001);
        assertEquals(30.0, outputData.getServingCarbs(), 0.0001);
        assertEquals(10.0, outputData.getServingFat(), 0.0001);
    }
}
