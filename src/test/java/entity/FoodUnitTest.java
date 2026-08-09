package entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class FoodUnitTest {

    @Test
    public void gramConversionRatesMatchExpectedValues() {
        assertEquals(1, FoodUnit.GRAM.getGramsPerUnit());
        assertEquals(0, FoodUnit.DEFAULT_SERVING.getGramsPerUnit());
        assertEquals(240, FoodUnit.CUP.getGramsPerUnit());
        assertEquals(15, FoodUnit.TABLESPOON.getGramsPerUnit());
        assertEquals(5, FoodUnit.TEASPOON.getGramsPerUnit());
    }

    @Test
    public void toStringReturnsDisplayName() {
        assertEquals("g", FoodUnit.GRAM.toString());
        assertEquals("serving", FoodUnit.DEFAULT_SERVING.toString());
        assertEquals("cup", FoodUnit.CUP.toString());
    }
}
