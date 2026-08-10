package entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class FoodEntryTest {

    @Test
    public void constructorStoresAllFields() {
        final FoodNutrition nutrition = new FoodNutrition(200.0, 20.0, 15.0, 5.0);
        final FoodEntry food = new FoodEntry("Chicken Breast", nutrition, 1.0, FoodUnit.GRAM, 150.0);

        assertEquals("Chicken Breast", food.getFoodName());
        assertEquals(nutrition, food.getNutrition());
        assertEquals(1.0, food.getQuantity(), 0.0001);
        assertEquals(FoodUnit.GRAM, food.getUnit());
        assertEquals(150.0, food.getGrams(), 0.0001);
        assertNull(food.getId());
        assertNull(food.getMealId());
    }

    @Test
    public void settersUpdateFields() {
        final FoodEntry food = new FoodEntry("Rice", new FoodNutrition(130.0, 3.0, 28.0, 0.3), 1.0,
                FoodUnit.GRAM, 100.0);

        food.setId(5);
        food.setMealId(2);
        food.setFoodName("Brown Rice");
        food.setQuantity(2.0);
        food.setUnit(FoodUnit.CUP);
        food.setGrams(200.0);
        final FoodNutrition updatedNutrition = new FoodNutrition(260.0, 6.0, 56.0, 0.6);
        food.setNutrition(updatedNutrition);

        assertEquals(5, food.getId());
        assertEquals(2, food.getMealId());
        assertEquals("Brown Rice", food.getFoodName());
        assertEquals(2.0, food.getQuantity(), 0.0001);
        assertEquals(FoodUnit.CUP, food.getUnit());
        assertEquals(200.0, food.getGrams(), 0.0001);
        assertEquals(updatedNutrition, food.getNutrition());
    }
}
