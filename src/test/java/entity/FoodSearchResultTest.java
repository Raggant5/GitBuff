package entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class FoodSearchResultTest {

    @Test
    public void constructorStoresAllFields() {
        final FoodSearchResult result = new FoodSearchResult("Apple", "1 Medium Apple", 182.0, 94.6, 0.5,
                21.1, 0.5);

        assertEquals("Apple", result.getFoodName());
        assertEquals("1 Medium Apple", result.getServingLabel());
        assertEquals(182.0, result.getServingGrams(), 0.0001);
        assertEquals(94.6, result.getServingCalories(), 0.0001);
        assertEquals(0.5, result.getServingProtein(), 0.0001);
        assertEquals(21.1, result.getServingCarbs(), 0.0001);
        assertEquals(0.5, result.getServingFat(), 0.0001);
    }
}
