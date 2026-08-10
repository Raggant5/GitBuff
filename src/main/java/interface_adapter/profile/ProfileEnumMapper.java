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
        final ActivityLevelOption result;
        if (activityLevel == null) {
            result = null;
        }
        else {
            result = ActivityLevelOption.valueOf(activityLevel.name());
        }
        return result;
    }

    /**
     * Maps an entity fitness goal to its display option.
     *
     * @param goal the entity-layer value, possibly {@code null}
     * @return the matching display option, or {@code null} if the input was {@code null}
     */
    public static FitnessGoalOption toOption(final FitnessGoal goal) {
        final FitnessGoalOption result;
        if (goal == null) {
            result = null;
        }
        else {
            result = FitnessGoalOption.valueOf(goal.name());
        }
        return result;
    }

    /**
     * Maps an entity gender to its display option.
     *
     * @param gender the entity-layer value, possibly {@code null}
     * @return the matching display option, or {@code null} if the input was {@code null}
     */
    public static GenderOption toOption(final Gender gender) {
        final GenderOption result;
        if (gender == null) {
            result = null;
        }
        else {
            result = GenderOption.valueOf(gender.name());
        }
        return result;
    }

    /**
     * Maps an entity unit system to its display option.
     *
     * @param unitSystem the entity-layer value, possibly {@code null}
     * @return the matching display option, or {@code null} if the input was {@code null}
     */
    public static UnitSystemOption toOption(final UnitSystem unitSystem) {
        final UnitSystemOption result;
        if (unitSystem == null) {
            result = null;
        }
        else {
            result = UnitSystemOption.valueOf(unitSystem.name());
        }
        return result;
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
     * Maps a display activity level back to its entity value.
     *
     * @param option the display-layer value, possibly {@code null}
     * @return the matching entity value, or {@code null} if the input was {@code null}
     */
    public static ActivityLevel toEntity(final ActivityLevelOption option) {
        final ActivityLevel result;
        if (option == null) {
            result = null;
        }
        else {
            result = ActivityLevel.valueOf(option.name());
        }
        return result;
    }

    /**
     * Maps a display fitness goal back to its entity value.
     *
     * @param option the display-layer value, possibly {@code null}
     * @return the matching entity value, or {@code null} if the input was {@code null}
     */
    public static FitnessGoal toEntity(final FitnessGoalOption option) {
        final FitnessGoal result;
        if (option == null) {
            result = null;
        }
        else {
            result = FitnessGoal.valueOf(option.name());
        }
        return result;
    }

    /**
     * Maps a display gender back to its entity value.
     *
     * @param option the display-layer value, possibly {@code null}
     * @return the matching entity value, or {@code null} if the input was {@code null}
     */
    public static Gender toEntity(final GenderOption option) {
        final Gender result;
        if (option == null) {
            result = null;
        }
        else {
            result = Gender.valueOf(option.name());
        }
        return result;
    }

    /**
     * Maps a display unit system back to its entity value.
     *
     * @param option the display-layer value, possibly {@code null}
     * @return the matching entity value, or {@code null} if the input was {@code null}
     */
    public static UnitSystem toEntity(final UnitSystemOption option) {
        final UnitSystem result;
        if (option == null) {
            result = null;
        }
        else {
            result = UnitSystem.valueOf(option.name());
        }
        return result;
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
        final Set<EquipmentOption> result;
        if (equipment == null) {
            result = EnumSet.noneOf(EquipmentOption.class);
        }
        else {
            result = EnumSet.noneOf(EquipmentOption.class);
            for (final Equipment item : equipment) {
                result.add(toOption(item));
            }
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
        final Set<Equipment> result;
        if (options == null) {
            result = EnumSet.noneOf(Equipment.class);
        }
        else {
            result = EnumSet.noneOf(Equipment.class);
            for (final EquipmentOption option : options) {
                result.add(toEntity(option));
            }
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
        final Set<DietaryRestrictionOption> result;
        if (restrictions == null) {
            result = EnumSet.noneOf(DietaryRestrictionOption.class);
        }
        else {
            result = EnumSet.noneOf(DietaryRestrictionOption.class);
            for (final DietaryRestriction restriction : restrictions) {
                result.add(DietaryRestrictionOption.valueOf(restriction.name()));
            }
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
        final Set<DietaryRestriction> result;
        if (options == null) {
            result = EnumSet.noneOf(DietaryRestriction.class);
        }
        else {
            result = EnumSet.noneOf(DietaryRestriction.class);
            for (final DietaryRestrictionOption option : options) {
                result.add(DietaryRestriction.valueOf(option.name()));
            }
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
        final Set<PrivacySettingOption> result;
        if (settings == null) {
            result = EnumSet.noneOf(PrivacySettingOption.class);
        }
        else {
            result = EnumSet.noneOf(PrivacySettingOption.class);
            for (final PrivacySetting setting : settings) {
                result.add(PrivacySettingOption.valueOf(setting.name()));
            }
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
        final Set<PrivacySetting> result;
        if (options == null) {
            result = EnumSet.noneOf(PrivacySetting.class);
        }
        else {
            result = EnumSet.noneOf(PrivacySetting.class);
            for (final PrivacySettingOption option : options) {
                result.add(PrivacySetting.valueOf(option.name()));
            }
        }
        return result;
    }
}
