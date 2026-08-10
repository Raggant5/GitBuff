package interface_adapter.nutrition.meal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for ViewMealsState.
 */
class ViewMealsStateTest {

    private static MealDisplayData buildMeal(final int id, final String name) {
        return new MealDisplayData(id, LocalDate.now(), name, new ArrayList<>());
    }

    @Test
    void addMealAppendsToExistingList() {
        final ViewMealsState state = new ViewMealsState();

        state.addMeal(buildMeal(1, "Lunch"));

        assertEquals(1, state.getMeals().size());
        assertEquals("Lunch", state.getMeals().get(0).getName());
    }

    @Test
    void replaceMealReplacesMatchingEntryInPlace() {
        final ViewMealsState state = new ViewMealsState();
        state.addMeal(buildMeal(1, "Lunch"));
        state.addMeal(buildMeal(2, "Dinner"));

        state.replaceMeal(buildMeal(2, "Late Dinner"));

        assertEquals("Lunch", state.getMeals().get(0).getName());
        assertEquals("Late Dinner", state.getMeals().get(1).getName());
    }

    @Test
    void replaceMealWithNoMatchLeavesListUnchanged() {
        final ViewMealsState state = new ViewMealsState();
        state.addMeal(buildMeal(1, "Lunch"));

        state.replaceMeal(buildMeal(2, "Dinner"));

        assertEquals(1, state.getMeals().size());
        assertEquals("Lunch", state.getMeals().get(0).getName());
    }

    @Test
    void removeMealRemovesOnlyMatchingId() {
        final ViewMealsState state = new ViewMealsState();
        state.addMeal(buildMeal(1, "Lunch"));
        state.addMeal(buildMeal(2, "Dinner"));

        state.removeMeal(1);

        assertEquals(1, state.getMeals().size());
        assertEquals(2, state.getMeals().get(0).getId());
    }

    @Test
    void errorCanBeSetAndRead() {
        final ViewMealsState state = new ViewMealsState();

        state.setError("boom");

        assertEquals("boom", state.getError());
        assertTrue(state.getMeals().isEmpty());
    }
}
