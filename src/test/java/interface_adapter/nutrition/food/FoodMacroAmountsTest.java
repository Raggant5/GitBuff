package interface_adapter.nutrition.food;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Food Macro Amounts DTO.
 */
class FoodMacroAmountsTest {

    @Test
    void constructorStoresAllFieldsForRetrieval() {
        final FoodMacroAmounts macros = new FoodMacroAmounts(250.0, 12.0, 30.0, 8.0);

        assertEquals(250.0, macros.getCalories());
        assertEquals(12.0, macros.getProtein());
        assertEquals(30.0, macros.getCarbs());
        assertEquals(8.0, macros.getFat());
    }
}
