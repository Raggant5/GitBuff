package interface_adapter.nutrition.food;

import entity.FoodUnit;

/**
 * Translates between entity.FoodUnit and its interface_adapter-layer FoodUnitOption mirror.
 */
public final class FoodEnumMapper {

    private FoodEnumMapper() {
        // Utility class.
    }

    /**
     * Maps an entity food unit to its display option.
     *
     * @param unit the entity-layer value, possibly null
     * @return the matching display option, or null if the input was null
     */
    public static FoodUnitOption toOption(final FoodUnit unit) {
        final FoodUnitOption result;
        if (unit == null) {
            result = null;
        }
        else {
            result = FoodUnitOption.valueOf(unit.name());
        }
        return result;
    }

    /**
     * Maps a display food unit back to its entity value.
     *
     * @param option the display-layer value, possibly null
     * @return the matching entity value, or null if the input was null
     */
    public static FoodUnit toEntity(final FoodUnitOption option) {
        final FoodUnit result;
        if (option == null) {
            result = null;
        }
        else {
            result = FoodUnit.valueOf(option.name());
        }
        return result;
    }
}
