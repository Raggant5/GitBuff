package interface_adapter.nutrition.food;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import entity.FoodUnit;

/**
 * Unit tests for the Food Enum Mapper.
 */
class FoodEnumMapperTest {

    @Test
    void toOptionMapsMatchingEntityValue() {
        assertEquals(FoodUnitOption.CUP, FoodEnumMapper.toOption(FoodUnit.CUP));
    }

    @Test
    void toOptionReturnsNullForNullInput() {
        assertNull(FoodEnumMapper.toOption(null));
    }

    @Test
    void toEntityMapsMatchingDisplayValue() {
        assertEquals(FoodUnit.TABLESPOON, FoodEnumMapper.toEntity(FoodUnitOption.TABLESPOON));
    }

    @Test
    void toEntityReturnsNullForNullInput() {
        assertNull(FoodEnumMapper.toEntity(null));
    }
}
