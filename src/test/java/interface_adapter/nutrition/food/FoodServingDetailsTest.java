package interface_adapter.nutrition.food;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Food Serving Details serving-size/quantity/unit math.
 */
class FoodServingDetailsTest {

    @Test
    void defaultsAreGramAndSingleServing() {
        final FoodServingDetails details = new FoodServingDetails();

        assertEquals("1", details.getQuantity());
        assertEquals(FoodUnitOption.GRAM, details.getUnit());
        assertEquals("0.0", details.getTotalCaloriesDisplay());
        assertEquals("0.0", details.getTotalGramsDisplay());
        assertEquals("0.0", details.getTotalProteinDisplay());
        assertEquals("0.0", details.getTotalCarbsDisplay());
        assertEquals("0.0", details.getTotalFatDisplay());
    }

    @Test
    void setServingDataStoresAllFields() {
        final FoodServingDetails details = new FoodServingDetails();

        details.setServingData("1 cup", 240, 240, 200, 5, 30, 8);

        assertEquals("1 cup", details.getServingLabel());
        assertEquals(240, details.getOriginalServingGrams());
        assertEquals(240, details.getServingGrams());
        assertEquals(200, details.getServingCalories());
        assertEquals(5, details.getServingProtein());
        assertEquals(30, details.getServingCarbs());
        assertEquals(8, details.getServingFat());
    }

    @Test
    void setQuantityRecalculatesTotalsWhenParseable() {
        final FoodServingDetails details = new FoodServingDetails();
        details.setServingData("serving", 100, 100, 200, 10, 20, 5);

        details.setQuantity("3");

        assertEquals("300.0", details.getTotalGramsDisplay());
        assertEquals("600.0", details.getTotalCaloriesDisplay());
        assertEquals("30.0", details.getTotalProteinDisplay());
        assertEquals("60.0", details.getTotalCarbsDisplay());
        assertEquals("15.0", details.getTotalFatDisplay());
    }

    @Test
    void setQuantityWithUnparseableValueLeavesTotalsUnchanged() {
        final FoodServingDetails details = new FoodServingDetails();
        details.setServingData("serving", 100, 100, 200, 10, 20, 5);
        details.setQuantity("2");

        details.setQuantity("not-a-number");

        assertEquals("not-a-number", details.getQuantity());
        assertEquals("200.0", details.getTotalGramsDisplay());
        assertEquals("400.0", details.getTotalCaloriesDisplay());
    }

    @Test
    void setQuantityWithNullValueLeavesTotalsUnchanged() {
        final FoodServingDetails details = new FoodServingDetails();
        details.setServingData("serving", 100, 100, 200, 10, 20, 5);
        details.setQuantity("2");

        details.setQuantity(null);

        assertEquals("200.0", details.getTotalGramsDisplay());
    }

    @Test
    void setTotalCaloriesDisplayUpdatesServingCaloriesWhenQuantityNonZero() {
        final FoodServingDetails details = new FoodServingDetails();
        details.setQuantity("2");

        details.setTotalCaloriesDisplay("100");

        assertEquals("100", details.getTotalCaloriesDisplay());
        assertEquals(50.0, details.getServingCalories());
    }

    @Test
    void setTotalProteinDisplayUpdatesServingProteinWhenQuantityNonZero() {
        final FoodServingDetails details = new FoodServingDetails();
        details.setQuantity("2");

        details.setTotalProteinDisplay("20");

        assertEquals("20", details.getTotalProteinDisplay());
        assertEquals(10.0, details.getServingProtein());
    }

    @Test
    void setTotalCarbsDisplayUpdatesServingCarbsWhenQuantityNonZero() {
        final FoodServingDetails details = new FoodServingDetails();
        details.setQuantity("2");

        details.setTotalCarbsDisplay("40");

        assertEquals("40", details.getTotalCarbsDisplay());
        assertEquals(20.0, details.getServingCarbs());
    }

    @Test
    void setTotalFatDisplayUpdatesServingFatWhenQuantityNonZero() {
        final FoodServingDetails details = new FoodServingDetails();
        details.setQuantity("2");

        details.setTotalFatDisplay("10");

        assertEquals("10", details.getTotalFatDisplay());
        assertEquals(5.0, details.getServingFat());
    }

    @Test
    void totalDisplaySettersDoNotUpdateServingAmountsWhenQuantityIsUnparseable() {
        final FoodServingDetails details = new FoodServingDetails();
        details.setServingData("serving", 100, 100, 200, 10, 20, 5);
        details.setQuantity("not-a-number");

        details.setTotalCaloriesDisplay("999");
        details.setTotalProteinDisplay("999");
        details.setTotalCarbsDisplay("999");
        details.setTotalFatDisplay("999");

        assertEquals(200, details.getServingCalories());
        assertEquals(10, details.getServingProtein());
        assertEquals(20, details.getServingCarbs());
        assertEquals(5, details.getServingFat());
        assertEquals("999", details.getTotalCaloriesDisplay());
    }

    @Test
    void setTotalGramsDisplayScalesServingMacrosByRatioWhenPreviousGramsNonZero() {
        final FoodServingDetails details = new FoodServingDetails();
        details.setServingData("serving", 100, 100, 50, 5, 10, 2);
        details.setQuantity("1");

        details.setTotalGramsDisplay("200");

        assertEquals(200.0, details.getServingGrams());
        assertEquals(100.0, details.getServingCalories());
        assertEquals(10.0, details.getServingProtein());
        assertEquals(20.0, details.getServingCarbs());
        assertEquals(4.0, details.getServingFat());
        assertEquals("200.0", details.getTotalGramsDisplay());
        assertEquals("100.0", details.getTotalCaloriesDisplay());
    }

    @Test
    void setTotalGramsDisplaySkipsRatioScalingWhenPreviousGramsWereZero() {
        final FoodServingDetails details = new FoodServingDetails();
        details.setQuantity("1");

        details.setTotalGramsDisplay("50");

        assertEquals(50.0, details.getServingGrams());
        assertEquals(0.0, details.getServingCalories());
    }

    @Test
    void setTotalGramsDisplayDoesNotUpdateServingGramsWhenQuantityIsZero() {
        final FoodServingDetails details = new FoodServingDetails();
        details.setServingData("serving", 10, 10, 50, 5, 10, 2);
        details.setQuantity("0");

        details.setTotalGramsDisplay("999");

        assertEquals(10.0, details.getServingGrams());
        assertEquals("0.0", details.getTotalGramsDisplay());
    }

    @Test
    void setUnitAndGetUnitRoundTrip() {
        final FoodServingDetails details = new FoodServingDetails();

        details.setUnit(FoodUnitOption.TABLESPOON);

        assertEquals(FoodUnitOption.TABLESPOON, details.getUnit());
    }

    @Test
    void resetRestoresDefaultValues() {
        final FoodServingDetails details = new FoodServingDetails();
        details.setServingData("1 cup", 240, 240, 200, 5, 30, 8);
        details.setQuantity("3");
        details.setUnit(FoodUnitOption.CUP);

        details.reset();

        assertEquals("", details.getServingLabel());
        assertEquals(0.0, details.getOriginalServingGrams());
        assertEquals(0.0, details.getServingGrams());
        assertEquals(0.0, details.getServingCalories());
        assertEquals(0.0, details.getServingProtein());
        assertEquals(0.0, details.getServingCarbs());
        assertEquals(0.0, details.getServingFat());
        assertEquals("0.0", details.getTotalCaloriesDisplay());
        assertEquals("0.0", details.getTotalGramsDisplay());
        assertEquals("0.0", details.getTotalProteinDisplay());
        assertEquals("0.0", details.getTotalCarbsDisplay());
        assertEquals("0.0", details.getTotalFatDisplay());
        assertEquals("1", details.getQuantity());
        assertEquals(FoodUnitOption.GRAM, details.getUnit());
    }
}
