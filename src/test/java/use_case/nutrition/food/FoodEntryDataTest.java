package use_case.nutrition.food;

import static org.junit.jupiter.api.Assertions.assertEquals;

import entity.FoodUnit;
import org.junit.jupiter.api.Test;

public class FoodEntryDataTest {

    @Test
    public void constructorStoresAllFields() {
        final FoodNutritionData nutrition = new FoodNutritionData(200.0, 20.0, 15.0, 5.0);
        final FoodEntryData entry = new FoodEntryData(9, "Chicken Breast", nutrition, 1.0, FoodUnit.GRAM, 150.0);

        assertEquals(9, entry.getId());
        assertEquals("Chicken Breast", entry.getFoodName());
        assertEquals(nutrition, entry.getNutrition());
        assertEquals(1.0, entry.getQuantity(), 0.0001);
        assertEquals(FoodUnit.GRAM, entry.getUnit());
        assertEquals(150.0, entry.getGrams(), 0.0001);
    }
}
