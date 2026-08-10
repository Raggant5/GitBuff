package interface_adapter.nutrition.food;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class FoodUnitOptionTest {

    @Test
    void toStringReturnsDisplayName() {
        assertEquals("g", FoodUnitOption.GRAM.toString());
        assertEquals("serving", FoodUnitOption.DEFAULT_SERVING.toString());
        assertEquals("cup", FoodUnitOption.CUP.toString());
        assertEquals("tbsp", FoodUnitOption.TABLESPOON.toString());
        assertEquals("tsp", FoodUnitOption.TEASPOON.toString());
    }
}
