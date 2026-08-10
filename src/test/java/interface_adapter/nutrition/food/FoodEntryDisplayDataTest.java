package interface_adapter.nutrition.food;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Food Entry Display Data DTO.
 */
class FoodEntryDisplayDataTest {

    @Test
    void constructorStoresAllFieldsForRetrieval() {
        final FoodNutritionDisplayData nutrition = new FoodNutritionDisplayData("100", "10", "20", "5");

        final FoodEntryDisplayData entry = new FoodEntryDisplayData(4, "Oats", nutrition, 1.5,
                FoodUnitOption.GRAM, 45.0);

        assertEquals(4, entry.getId());
        assertEquals("Oats", entry.getFoodName());
        assertSame(nutrition, entry.getNutrition());
        assertEquals(1.5, entry.getQuantity());
        assertEquals(FoodUnitOption.GRAM, entry.getUnit());
        assertEquals(45.0, entry.getGrams());
    }
}
