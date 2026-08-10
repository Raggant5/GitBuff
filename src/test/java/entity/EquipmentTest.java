package entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class EquipmentTest {

    @Test
    void getDisplayNameReturnsExpectedString() {
        assertEquals("Bodyweight Only", Equipment.BODYWEIGHT_ONLY.getDisplayName());
        assertEquals("Cable Machine", Equipment.CABLE_MACHINE.getDisplayName());
    }

    @Test
    void toStringMatchesDisplayName() {
        assertEquals(Equipment.DUMBBELLS.getDisplayName(), Equipment.DUMBBELLS.toString());
    }
}
