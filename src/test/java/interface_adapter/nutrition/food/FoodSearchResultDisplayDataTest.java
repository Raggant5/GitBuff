package interface_adapter.nutrition.food;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Food Search Result Display Data DTO.
 */
class FoodSearchResultDisplayDataTest {

    @Test
    void constructorStoresAllFieldsForRetrieval() {
        final FoodMacroAmounts nutrition = new FoodMacroAmounts(95, 0.5, 25, 0.3);

        final FoodSearchResultDisplayData result = new FoodSearchResultDisplayData("Apple", "1 medium", 182.0,
                nutrition, FoodUnitOption.DEFAULT_SERVING, 1.0);

        assertEquals("Apple", result.getFoodName());
        assertEquals("1 medium", result.getServingLabel());
        assertEquals(182.0, result.getServingGrams());
        assertSame(nutrition, result.getNutrition());
        assertEquals(FoodUnitOption.DEFAULT_SERVING, result.getUnit());
        assertEquals(1.0, result.getQuantity());
    }
}
