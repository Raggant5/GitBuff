package interface_adapter.nutrition.meal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import interface_adapter.nutrition.food.FoodEntryDisplayData;
import interface_adapter.nutrition.food.FoodNutritionDisplayData;
import interface_adapter.nutrition.food.FoodUnitOption;

/**
 * Unit tests for MealEditorState.
 */
class MealEditorStateTest {

    private static FoodEntryDisplayData buildFood(final Integer id, final String name) {
        final FoodNutritionDisplayData nutrition = new FoodNutritionDisplayData("100", "10", "20", "5");
        return new FoodEntryDisplayData(id, name, nutrition, 1, FoodUnitOption.GRAM, 100);
    }

    @Test
    void addFoodEntryAppendsToList() {
        final MealEditorState state = new MealEditorState();

        state.addFoodEntry(buildFood(1, "Egg"));

        assertEquals(1, state.getFoodEntriesForMeal().size());
        assertEquals("Egg", state.getFoodEntriesForMeal().get(0).getFoodName());
    }

    @Test
    void removeFoodEntryByIdRemovesOnlyMatchingEntry() {
        final MealEditorState state = new MealEditorState();
        state.addFoodEntry(buildFood(1, "Egg"));
        state.addFoodEntry(buildFood(2, "Toast"));

        state.removeFoodEntryById(1);

        assertEquals(1, state.getFoodEntriesForMeal().size());
        assertEquals("Toast", state.getFoodEntriesForMeal().get(0).getFoodName());
    }

    @Test
    void replaceFoodEntryReplacesMatchingEntryInPlace() {
        final MealEditorState state = new MealEditorState();
        state.setFoodEntriesForMeal(new ArrayList<>(List.of(buildFood(1, "Egg"), buildFood(2, "Toast"))));

        state.replaceFoodEntry(buildFood(2, "Waffle"));

        assertEquals("Egg", state.getFoodEntriesForMeal().get(0).getFoodName());
        assertEquals("Waffle", state.getFoodEntriesForMeal().get(1).getFoodName());
    }

    @Test
    void replaceFoodEntryWithNoMatchLeavesListUnchanged() {
        final MealEditorState state = new MealEditorState();
        state.setFoodEntriesForMeal(new ArrayList<>(List.of(buildFood(1, "Egg"))));

        state.replaceFoodEntry(buildFood(2, "Waffle"));

        assertEquals(1, state.getFoodEntriesForMeal().size());
        assertEquals("Egg", state.getFoodEntriesForMeal().get(0).getFoodName());
    }

    @Test
    void addFoodEntryToBeDeletedAppendsIdToDeleteStage() {
        final MealEditorState state = new MealEditorState();

        state.addFoodEntryToBeDeleted(9);

        assertEquals(List.of(9), state.getFoodEntriesDeleteStage());
    }

    @Test
    void nextTempIdCanBeSetAndRead() {
        final MealEditorState state = new MealEditorState();

        state.setNextTempId(-5);

        assertEquals(-5, state.getNextTempId());
    }

    @Test
    void resetClearsAllMutableFieldsBackToDefaults() {
        final MealEditorState state = new MealEditorState();
        state.setEditingMealId(3);
        state.setDate(LocalDate.now());
        state.setName("Dinner");
        state.addFoodEntry(buildFood(1, "Egg"));
        state.addFoodEntryToBeDeleted(2);
        state.setErrorMessage("oops");
        state.setShowFoodEditor(true);
        state.setNextTempId(-9);

        state.reset();

        assertNull(state.getEditingMealId());
        assertNull(state.getDate());
        assertEquals("", state.getName());
        assertTrue(state.getFoodEntriesForMeal().isEmpty());
        assertTrue(state.getFoodEntriesDeleteStage().isEmpty());
        assertEquals("", state.getErrorMessage());
        assertFalse(state.getShowFoodEditor());
        assertEquals(-1, state.getNextTempId());
    }
}
