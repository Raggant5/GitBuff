package interface_adapter.profile;

import java.util.EnumSet;
import java.util.Set;

import entity.ActivityLevel;
import entity.DietaryRestriction;
import entity.Equipment;
import entity.FitnessGoal;
import entity.Gender;
import entity.PrivacySetting;
import entity.UnitSystem;

/**
 * Translates between the entity-layer profile enums and their interface_adapter-layer
 * *Option mirrors.
 */
public final class ProfileEnumMapper {

    private ProfileEnumMapper() {
        // Utility class.
    }

    /**
     * Maps an entity activity level to its display option.
     *
     * @param activityLevel the entity-layer value, possibly {@code null}
     * @return the matching display option, or {@code null} if the input was {@code null}
     */
    public static ActivityLevelOption toOption(final ActivityLevel activityLevel) {
        return activityLevel == null ? null : ActivityLevelOption.valueOf(activityLevel.name());
    }

    /**
     * Maps a display activity level back to its entity value.
     *
     * @param option the display-layer value, possibly {@code null}
     * @return the matching entity value, or {@code null} if the input was {@code null}
     */
    public static ActivityLevel toEntity(final ActivityLevelOption option) {
        return option == null ? null : ActivityLevel.valueOf(option.name());
    }

    /**
     * Maps an entity fitness goal to its display option.
     *
     * @param goal the entity-layer value, possibly {@code null}
     * @return the matching display option, or {@code null} if the input was {@code null}
     */
    public static FitnessGoalOption toOption(final FitnessGoal goal) {
        return goal == null ? null : FitnessGoalOption.valueOf(goal.name());
    }

    /**
     * Maps a display fitness goal back to its entity value.
     *
     * @param option the display-layer value, possibly {@code null}
     * @return the matching entity value, or {@code null} if the input was {@code null}
     */
    public static FitnessGoal toEntity(final FitnessGoalOption option) {
        return option == null ? null : FitnessGoal.valueOf(option.name());
    }

    /**
     * Maps an entity gender to its display option.
     *
     * @param gender the entity-layer value, possibly {@code null}
     * @return the matching display option, or {@code null} if the input was {@code null}
     */
    public static GenderOption toOption(final Gender gender) {
        return gender == null ? null : GenderOption.valueOf(gender.name());
    }

    /**
     * Maps a display gender back to its entity value.
     *
     * @param option the display-layer value, possibly {@code null}
     * @return the matching entity value, or {@code null} if the input was {@code null}
     */
    public static Gender toEntity(final GenderOption option) {
        return option == null ? null : Gender.valueOf(option.name());
    }

    /**
     * Maps an entity unit system to its display option.
     *
     * @param unitSystem the entity-layer value, possibly {@code null}
     * @return the matching display option, or {@code null} if the input was {@code null}
     */
    public static UnitSystemOption toOption(final UnitSystem unitSystem) {
        return unitSystem == null ? null : UnitSystemOption.valueOf(unitSystem.name());
    }

    /**
     * Maps a display unit system back to its entity value.
     *
     * @param option the display-layer value, possibly {@code null}
     * @return the matching entity value, or {@code null} if the input was {@code null}
     */
    public static UnitSystem toEntity(final UnitSystemOption option) {
        return option == null ? null : UnitSystem.valueOf(option.name());
    }

    /**
     * Maps a single entity equipment value to its display option.
     *
     * @param equipment the entity-layer value
     * @return the matching display option
     */
    public static EquipmentOption toOption(final Equipment equipment) {
        return EquipmentOption.valueOf(equipment.name());
    }

    /**
     * Maps a single display equipment value back to its entity value.
     *
     * @param option the display-layer value
     * @return the matching entity value
     */
    public static Equipment toEntity(final EquipmentOption option) {
        return Equipment.valueOf(option.name());
    }

    /**
     * Maps a set of entity equipment values to display options.
     *
     * @param equipment the entity-layer values, possibly {@code null}
     * @return the matching display options, or an empty set if the input was {@code null}
     */
    public static Set<EquipmentOption> toEquipmentOptions(final Set<Equipment> equipment) {
        if (equipment == null) {
            return EnumSet.noneOf(EquipmentOption.class);
        }
        final Set<EquipmentOption> result = EnumSet.noneOf(EquipmentOption.class);
        for (final Equipment item : equipment) {
            result.add(toOption(item));
        }
        return result;
    }

    /**
     * Maps a set of display equipment values back to entity values.
     *
     * @param options the display-layer values, possibly {@code null}
     * @return the matching entity values, or an empty set if the input was {@code null}
     */
    public static Set<Equipment> toEquipmentEntities(final Set<EquipmentOption> options) {
        if (options == null) {
            return EnumSet.noneOf(Equipment.class);
        }
        final Set<Equipment> result = EnumSet.noneOf(Equipment.class);
        for (final EquipmentOption option : options) {
            result.add(toEntity(option));
        }
        return result;
    }

    /**
     * Maps a set of entity dietary restrictions to display options.
     *
     * @param restrictions the entity-layer values, possibly {@code null}
     * @return the matching display options, or an empty set if the input was {@code null}
     */
    public static Set<DietaryRestrictionOption> toDietaryOptions(final Set<DietaryRestriction> restrictions) {
        if (restrictions == null) {
            return EnumSet.noneOf(DietaryRestrictionOption.class);
        }
        final Set<DietaryRestrictionOption> result = EnumSet.noneOf(DietaryRestrictionOption.class);
        for (final DietaryRestriction restriction : restrictions) {
            result.add(DietaryRestrictionOption.valueOf(restriction.name()));
        }
        return result;
    }

    /**
     * Maps a set of display dietary restrictions back to entity values.
     *
     * @param options the display-layer values, possibly {@code null}
     * @return the matching entity values, or an empty set if the input was {@code null}
     */
    public static Set<DietaryRestriction> toDietaryEntities(final Set<DietaryRestrictionOption> options) {
        if (options == null) {
            return EnumSet.noneOf(DietaryRestriction.class);
        }
        final Set<DietaryRestriction> result = EnumSet.noneOf(DietaryRestriction.class);
        for (final DietaryRestrictionOption option : options) {
            result.add(DietaryRestriction.valueOf(option.name()));
        }
        return result;
    }

    /**
     * Maps a set of entity privacy settings to display options.
     *
     * @param settings the entity-layer values, possibly {@code null}
     * @return the matching display options, or an empty set if the input was {@code null}
     */
    public static Set<PrivacySettingOption> toPrivacyOptions(final Set<PrivacySetting> settings) {
        if (settings == null) {
            return EnumSet.noneOf(PrivacySettingOption.class);
        }
        final Set<PrivacySettingOption> result = EnumSet.noneOf(PrivacySettingOption.class);
        for (final PrivacySetting setting : settings) {
            result.add(PrivacySettingOption.valueOf(setting.name()));
        }
        return result;
    }

    /**
     * Maps a set of display privacy settings back to entity values.
     *
     * @param options the display-layer values, possibly {@code null}
     * @return the matching entity values, or an empty set if the input was {@code null}
     */
    public static Set<PrivacySetting> toPrivacyEntities(final Set<PrivacySettingOption> options) {
        if (options == null) {
            return EnumSet.noneOf(PrivacySetting.class);
        }
        final Set<PrivacySetting> result = EnumSet.noneOf(PrivacySetting.class);
        for (final PrivacySettingOption option : options) {
            result.add(PrivacySetting.valueOf(option.name()));
        }
        return result;
    }
}

