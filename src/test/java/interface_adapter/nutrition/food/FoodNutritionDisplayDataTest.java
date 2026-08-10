package interface_adapter.nutrition.food;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Food Nutrition Display Data DTO.
 */
class FoodNutritionDisplayDataTest {

    @Test
    void stringConstructorStoresRawValues() {
        final FoodNutritionDisplayData nutrition = new FoodNutritionDisplayData("100", "10", "20", "5");

        assertEquals("100", nutrition.getCalories());
        assertEquals("10", nutrition.getProtein());
        assertEquals("20", nutrition.getCarbs());
        assertEquals("5", nutrition.getFat());
    }

    @Test
    void doubleConstructorFormatsValuesForDisplay() {
        final FoodNutritionDisplayData nutrition = new FoodNutritionDisplayData(100.0, 10.5, 20.25, 5.0);

        assertEquals("100.0", nutrition.getCalories());
        assertEquals("10.5", nutrition.getProtein());
        assertEquals("20.25", nutrition.getCarbs());
        assertEquals("5.0", nutrition.getFat());
    }
}
