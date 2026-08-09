package entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class MealFactoryTest {

    @Test
    public void createReturnsMealWithGivenFieldsAndNoId() {
        final MealFactory factory = new MealFactory();
        final LocalDate date = LocalDate.of(2026, 8, 6);

        final Meal meal = factory.create("amir", date, "Lunch");

        assertEquals("amir", meal.getUserId());
        assertEquals(date, meal.getDate());
        assertEquals("Lunch", meal.getName());
        assertTrue(meal.getFoodEntries().isEmpty());
        assertNull(meal.getId());
    }
}
