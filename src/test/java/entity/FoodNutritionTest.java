package entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class FoodNutritionTest {

    @Test
    public void constructorStoresAllValues() {
        final FoodNutrition nutrition = new FoodNutrition(200.0, 20.0, 15.0, 5.0);

        assertEquals(200.0, nutrition.getCalories(), 0.0001);
        assertEquals(20.0, nutrition.getProtein(), 0.0001);
        assertEquals(15.0, nutrition.getCarbs(), 0.0001);
        assertEquals(5.0, nutrition.getFat(), 0.0001);
    }

    @Test
    public void settersUpdateValues() {
        final FoodNutrition nutrition = new FoodNutrition(0, 0, 0, 0);

        nutrition.setCalories(100.0);
        nutrition.setProtein(10.0);
        nutrition.setCarbs(8.0);
        nutrition.setFat(2.0);

        assertEquals(100.0, nutrition.getCalories(), 0.0001);
        assertEquals(10.0, nutrition.getProtein(), 0.0001);
        assertEquals(8.0, nutrition.getCarbs(), 0.0001);
        assertEquals(2.0, nutrition.getFat(), 0.0001);
    }

    @Test
    public void scaledToMultipliesAllValuesByRatio() {
        final FoodNutrition nutrition = new FoodNutrition(200.0, 20.0, 15.0, 5.0);

        final FoodNutrition scaled = nutrition.scaledTo(2.0);

        assertEquals(400.0, scaled.getCalories(), 0.0001);
        assertEquals(40.0, scaled.getProtein(), 0.0001);
        assertEquals(30.0, scaled.getCarbs(), 0.0001);
        assertEquals(10.0, scaled.getFat(), 0.0001);
        assertEquals(200.0, nutrition.getCalories(), 0.0001);
    }
}
