package use_case.nutrition.food.search_food;

import static org.junit.jupiter.api.Assertions.assertEquals;

import entity.FoodUnit;
import org.junit.jupiter.api.Test;
import use_case.nutrition.food.FoodNutritionData;

public class FoodSearchResultDataTest {

    @Test
    public void constructorStoresAllFields() {
        final FoodNutritionData nutrition = new FoodNutritionData(200.0, 20.0, 15.0, 5.0);
        final FoodSearchResultData data = new FoodSearchResultData(
                "Chicken Breast", "1 breast", 150.0, nutrition, FoodUnit.DEFAULT_SERVING, 1.0);

        assertEquals("Chicken Breast", data.getFoodName());
        assertEquals("1 breast", data.getServingLabel());
        assertEquals(150.0, data.getServingGrams(), 0.0001);
        assertEquals(nutrition, data.getNutrition());
        assertEquals(FoodUnit.DEFAULT_SERVING, data.getUnit());
        assertEquals(1.0, data.getQuantity(), 0.0001);
    }
}
