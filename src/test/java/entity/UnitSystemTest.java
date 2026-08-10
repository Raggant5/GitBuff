package entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class UnitSystemTest {

    @Test
    void getDisplayNameReturnsExpectedString() {
        assertEquals("Metric (kg, cm)", UnitSystem.METRIC.getDisplayName());
        assertEquals("Imperial (lbs, in)", UnitSystem.IMPERIAL.getDisplayName());
    }

    @Test
    void toStringMatchesDisplayName() {
        assertEquals(UnitSystem.METRIC.getDisplayName(), UnitSystem.METRIC.toString());
    }
}
