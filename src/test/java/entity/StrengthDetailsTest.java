package entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class StrengthDetailsTest {

    @Test
    public void constructorStoresSetsRepsWeight() {
        final StrengthDetails details = new StrengthDetails(3, 10, 45.5);

        assertEquals(3, details.getSets());
        assertEquals(10, details.getReps());
        assertEquals(45.5, details.getWeight(), 0.0001);
    }

    @Test
    public void allowsNullFieldsForCardioExercises() {
        final StrengthDetails details = new StrengthDetails(null, null, null);

        assertNull(details.getSets());
        assertNull(details.getReps());
        assertNull(details.getWeight());
    }
}
