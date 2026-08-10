package entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class DietaryRestrictionTest {

    @Test
    void getDisplayNameReturnsExpectedString() {
        assertEquals("Gluten-Free", DietaryRestriction.GLUTEN_FREE.getDisplayName());
        assertEquals("None", DietaryRestriction.NONE.getDisplayName());
    }

    @Test
    void toStringMatchesDisplayName() {
        assertEquals(DietaryRestriction.VEGAN.getDisplayName(), DietaryRestriction.VEGAN.toString());
    }
}
