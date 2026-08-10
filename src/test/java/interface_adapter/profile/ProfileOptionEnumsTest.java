package interface_adapter.profile;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ProfileOptionEnumsTest {

    @Test
    void dietaryRestrictionOptionExposesDisplayName() {
        assertEquals("Gluten-Free", DietaryRestrictionOption.GLUTEN_FREE.getDisplayName());
        assertEquals("Gluten-Free", DietaryRestrictionOption.GLUTEN_FREE.toString());
    }

    @Test
    void equipmentOptionExposesDisplayName() {
        assertEquals("Barbell", EquipmentOption.BARBELL.getDisplayName());
        assertEquals("Barbell", EquipmentOption.BARBELL.toString());
    }

    @Test
    void privacySettingOptionExposesDisplayName() {
        assertEquals("Share Workout Streak", PrivacySettingOption.SHARE_STREAK.getDisplayName());
        assertEquals("Share Workout Streak", PrivacySettingOption.SHARE_STREAK.toString());
    }
}
