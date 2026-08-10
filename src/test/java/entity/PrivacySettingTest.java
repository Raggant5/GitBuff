package entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PrivacySettingTest {

    @Test
    void getDisplayNameReturnsExpectedString() {
        assertEquals("Share Profile Bio & Picture", PrivacySetting.SHARE_PROFILE.getDisplayName());
        assertEquals("Share Meal Logs", PrivacySetting.SHARE_MEAL_LOGS.getDisplayName());
    }

    @Test
    void toStringMatchesDisplayName() {
        assertEquals(PrivacySetting.SHARE_STREAK.getDisplayName(), PrivacySetting.SHARE_STREAK.toString());
    }
}
