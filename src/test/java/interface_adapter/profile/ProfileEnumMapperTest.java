package interface_adapter.profile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import entity.ActivityLevel;
import entity.DietaryRestriction;
import entity.Equipment;
import entity.FitnessGoal;
import entity.Gender;
import entity.PrivacySetting;
import entity.UnitSystem;
import org.junit.jupiter.api.Test;

class ProfileEnumMapperTest {

    @Test
    void activityLevelRoundTrips() {
        assertEquals(ActivityLevelOption.VERY_ACTIVE, ProfileEnumMapper.toOption(ActivityLevel.VERY_ACTIVE));
        assertEquals(ActivityLevel.VERY_ACTIVE, ProfileEnumMapper.toEntity(ActivityLevelOption.VERY_ACTIVE));
        assertNull(ProfileEnumMapper.toOption((ActivityLevel) null));
        assertNull(ProfileEnumMapper.toEntity((ActivityLevelOption) null));
    }

    @Test
    void fitnessGoalRoundTrips() {
        assertEquals(FitnessGoalOption.MUSCLE_AND_STRENGTH_GAIN,
                ProfileEnumMapper.toOption(FitnessGoal.MUSCLE_AND_STRENGTH_GAIN));
        assertEquals(FitnessGoal.MUSCLE_AND_STRENGTH_GAIN,
                ProfileEnumMapper.toEntity(FitnessGoalOption.MUSCLE_AND_STRENGTH_GAIN));
        assertNull(ProfileEnumMapper.toOption((FitnessGoal) null));
        assertNull(ProfileEnumMapper.toEntity((FitnessGoalOption) null));
    }

    @Test
    void genderRoundTrips() {
        assertEquals(GenderOption.MALE, ProfileEnumMapper.toOption(Gender.MALE));
        assertEquals(Gender.MALE, ProfileEnumMapper.toEntity(GenderOption.MALE));
        assertNull(ProfileEnumMapper.toOption((Gender) null));
        assertNull(ProfileEnumMapper.toEntity((GenderOption) null));
    }

    @Test
    void unitSystemRoundTrips() {
        assertEquals(UnitSystemOption.METRIC, ProfileEnumMapper.toOption(UnitSystem.METRIC));
        assertEquals(UnitSystem.METRIC, ProfileEnumMapper.toEntity(UnitSystemOption.METRIC));
        assertNull(ProfileEnumMapper.toOption((UnitSystem) null));
        assertNull(ProfileEnumMapper.toEntity((UnitSystemOption) null));
    }

    @Test
    void singleEquipmentRoundTrips() {
        assertEquals(EquipmentOption.BARBELL, ProfileEnumMapper.toOption(Equipment.BARBELL));
        assertEquals(Equipment.BARBELL, ProfileEnumMapper.toEntity(EquipmentOption.BARBELL));
    }

    @Test
    void equipmentSetsRoundTripAndHandleNull() {
        final Set<EquipmentOption> options = ProfileEnumMapper.toEquipmentOptions(Set.of(Equipment.BARBELL));
        assertTrue(options.contains(EquipmentOption.BARBELL));
        final Set<Equipment> entities = ProfileEnumMapper.toEquipmentEntities(options);
        assertTrue(entities.contains(Equipment.BARBELL));

        assertTrue(ProfileEnumMapper.toEquipmentOptions(null).isEmpty());
        assertTrue(ProfileEnumMapper.toEquipmentEntities(null).isEmpty());
    }

    @Test
    void dietaryRestrictionSetsRoundTripAndHandleNull() {
        final Set<DietaryRestrictionOption> options =
                ProfileEnumMapper.toDietaryOptions(Set.of(DietaryRestriction.VEGAN));
        assertTrue(options.contains(DietaryRestrictionOption.VEGAN));
        final Set<DietaryRestriction> entities = ProfileEnumMapper.toDietaryEntities(options);
        assertTrue(entities.contains(DietaryRestriction.VEGAN));

        assertTrue(ProfileEnumMapper.toDietaryOptions(null).isEmpty());
        assertTrue(ProfileEnumMapper.toDietaryEntities(null).isEmpty());
    }

    @Test
    void privacySettingSetsRoundTripAndHandleNull() {
        final Set<PrivacySettingOption> options =
                ProfileEnumMapper.toPrivacyOptions(Set.of(PrivacySetting.SHARE_STREAK));
        assertTrue(options.contains(PrivacySettingOption.SHARE_STREAK));
        final Set<PrivacySetting> entities = ProfileEnumMapper.toPrivacyEntities(options);
        assertTrue(entities.contains(PrivacySetting.SHARE_STREAK));

        assertTrue(ProfileEnumMapper.toPrivacyOptions(null).isEmpty());
        assertTrue(ProfileEnumMapper.toPrivacyEntities(null).isEmpty());
    }
}
