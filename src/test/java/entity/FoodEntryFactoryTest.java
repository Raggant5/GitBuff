package entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class FoodEntryFactoryTest {

    @Test
    public void createReturnsFoodEntryWithGivenFieldsAndNoId() {
        final FoodEntryFactory factory = new FoodEntryFactory();
        final FoodNutrition nutrition = new FoodNutrition(200.0, 20.0, 15.0, 5.0);

        final FoodEntry food = factory.create("Chicken Breast", nutrition, 1.0, FoodUnit.GRAM, 150.0);

        assertEquals("Chicken Breast", food.getFoodName());
        assertEquals(nutrition, food.getNutrition());
        assertEquals(150.0, food.getGrams(), 0.0001);
        assertNull(food.getId());
    }
}
