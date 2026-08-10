package entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class GenderTest {

    @Test
    void getDisplayNameReturnsExpectedString() {
        assertEquals("Male", Gender.MALE.getDisplayName());
        assertEquals("Prefer Not to Say", Gender.PREFER_NOT_TO_SAY.getDisplayName());
    }

    @Test
    void toStringMatchesDisplayName() {
        assertEquals(Gender.FEMALE.getDisplayName(), Gender.FEMALE.toString());
    }
}
