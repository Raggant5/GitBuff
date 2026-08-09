package entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class MealTest {

    @Test
    public void constructorStoresUserIdDateAndNameWithEmptyFoodEntries() {
        final LocalDate date = LocalDate.of(2026, 8, 6);
        final Meal meal = new Meal("amir", date, "Lunch");

        assertEquals("amir", meal.getUserId());
        assertEquals(date, meal.getDate());
        assertEquals("Lunch", meal.getName());
        assertTrue(meal.getFoodEntries().isEmpty());
        assertNull(meal.getId());
    }

    @Test
    public void setFoodEntriesCopiesRatherThanAliasingTheInputList() {
        final Meal meal = new Meal("amir", LocalDate.now(), "Lunch");
        final List<FoodEntry> foods = new ArrayList<>();
        foods.add(new FoodEntry("Chicken", new FoodNutrition(200, 20, 0, 5), 1.0, FoodUnit.GRAM, 150));

        meal.setFoodEntries(foods);
        foods.clear();

        assertEquals(1, meal.getFoodEntries().size());
    }

    @Test
    public void setFoodEntriesReplacesPreviousContents() {
        final Meal meal = new Meal("amir", LocalDate.now(), "Lunch");
        meal.setFoodEntries(List.of(new FoodEntry("Chicken", new FoodNutrition(200, 20, 0, 5), 1.0,
                FoodUnit.GRAM, 150)));

        meal.setFoodEntries(List.of(new FoodEntry("Rice", new FoodNutrition(130, 3, 28, 0.3), 1.0,
                FoodUnit.GRAM, 100)));

        assertEquals(1, meal.getFoodEntries().size());
        assertEquals("Rice", meal.getFoodEntries().get(0).getFoodName());
    }

    @Test
    public void removeFoodEntryRemovesMatchingEntry() {
        final Meal meal = new Meal("amir", LocalDate.now(), "Lunch");
        meal.setFoodEntries(List.of(new FoodEntry("Chicken", new FoodNutrition(200, 20, 0, 5), 1.0,
                FoodUnit.GRAM, 150)));

        meal.removeFoodEntry(meal.getFoodEntries().get(0));

        assertTrue(meal.getFoodEntries().isEmpty());
    }

    @Test
    public void idAndNameAreSettable() {
        final Meal meal = new Meal("amir", LocalDate.now(), "Lunch");

        meal.setId(3);
        meal.setName("Dinner");
        final LocalDate updated = LocalDate.of(2026, 2, 2);
        meal.setDate(updated);

        assertEquals(3, meal.getId());
        assertEquals("Dinner", meal.getName());
        assertEquals(updated, meal.getDate());
    }
}
