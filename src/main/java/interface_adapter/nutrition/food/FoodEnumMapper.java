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
        return unit == null ? null : FoodUnitOption.valueOf(unit.name());
    }

    /**
     * Maps a display food unit back to its entity value.
     *
     * @param option the display-layer value, possibly null
     * @return the matching entity value, or null if the input was null
     */
    public static FoodUnit toEntity(final FoodUnitOption option) {
        return option == null ? null : FoodUnit.valueOf(option.name());
    }
}
