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

    @Test
    void unitSystemOptionExposesDisplayName() {
        assertEquals("Metric (kg, cm)", UnitSystemOption.METRIC.getDisplayName());
        assertEquals("Metric (kg, cm)", UnitSystemOption.METRIC.toString());
    }

    @Test
    void genderOptionExposesDisplayName() {
        assertEquals("Male", GenderOption.MALE.getDisplayName());
        assertEquals("Male", GenderOption.MALE.toString());
    }

    @Test
    void fitnessGoalOptionExposesDisplayName() {
        assertEquals("Lose Weight", FitnessGoalOption.LOSE_WEIGHT.getDisplayName());
        assertEquals("Lose Weight", FitnessGoalOption.LOSE_WEIGHT.toString());
    }

    @Test
    void activityLevelOptionExposesDescription() {
        assertEquals("Not active (little or no exercise)", ActivityLevelOption.NOT_ACTIVE.getDescription());
        assertEquals("Not active (little or no exercise)", ActivityLevelOption.NOT_ACTIVE.toString());
    }
}
