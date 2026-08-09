package use_case.nutrition.food.search_food;

import static org.junit.jupiter.api.Assertions.assertEquals;

import entity.FoodUnit;
import org.junit.jupiter.api.Test;

public class SelectSearchResultFoodOutputDataTest {

    @Test
    public void constructorStoresAllFields() {
        final SelectSearchResultFoodOutputData outputData = new SelectSearchResultFoodOutputData(
                "200", "20", "0", FoodUnit.GRAM);

        assertEquals("200", outputData.getCalories());
        assertEquals("20", outputData.getProtein());
        assertEquals("0", outputData.getCarbs());
        assertEquals(FoodUnit.GRAM, outputData.getUnit());
    }
}
