package interface_adapter.log_workout.exercise;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Strength Details Display Data.
 */
class StrengthDetailsDisplayDataTest {

    @Test
    void stringConstructorStoresValuesAsGiven() {
        final StrengthDetailsDisplayData data = new StrengthDetailsDisplayData("3", "10", "50.0");

        assertEquals("3", data.getSets());
        assertEquals("10", data.getReps());
        assertEquals("50.0", data.getWeight());
    }

    @Test
    void numericConstructorFormatsNonNullValues() {
        final StrengthDetailsDisplayData data = new StrengthDetailsDisplayData(3, 10, 50.0);

        assertEquals("3", data.getSets());
        assertEquals("10", data.getReps());
        assertEquals("50.0", data.getWeight());
    }

    @Test
    void numericConstructorProducesEmptyStringsForNullValues() {
        final StrengthDetailsDisplayData data = new StrengthDetailsDisplayData(
                (Integer) null, (Integer) null, (Double) null);

        assertEquals("", data.getSets());
        assertEquals("", data.getReps());
        assertEquals("", data.getWeight());
    }
}
