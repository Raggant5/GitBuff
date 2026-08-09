package use_case.nutrition.meal.prepare_edit_meal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import entity.FoodEntry;
import entity.FoodNutrition;
import entity.FoodUnit;
import entity.Meal;
import org.junit.jupiter.api.Test;
import use_case.DataAccessException;
import use_case.nutrition.meal.get_meals.ViewMealDataAccessInterface;

public class PrepareEditMealInteractorTest {

    @Test
    public void executeFetchesMealByIdAndMapsFoodEntriesToData() {
        final Meal meal = new Meal("amir", LocalDate.of(2026, 8, 6), "Lunch");
        meal.setId(5);
        final FoodEntry food = new FoodEntry("Chicken Breast", new FoodNutrition(200, 20, 0, 5), 1.0,
                FoodUnit.GRAM, 150.0);
        food.setId(11);
        meal.setFoodEntries(List.of(food));

        final ViewMealDataAccessInterface dataAccess = new ViewMealDataAccessInterface() {
            @Override
            public List<Meal> getMealsForUser(String userId) {
                return List.of(meal);
            }

            @Override
            public List<FoodEntry> getFoodEntriesForMeal(int mealId) {
                return meal.getFoodEntries();
            }

            @Override
            public Meal getMealById(int mealId) {
                assertEquals(5, mealId);
                return meal;
            }
        };

        final PrepareEditMealOutputData[] captured = new PrepareEditMealOutputData[1];
        final PrepareEditMealOutputBoundary presenter = new PrepareEditMealOutputBoundary() {
            @Override
            public void prepareSuccessView(PrepareEditMealOutputData outputData) {
                captured[0] = outputData;
            }

            @Override
            public void prepareFailView(String errorMessage) {
                throw new AssertionError("Expected success view, got failure: " + errorMessage);
            }
        };

        new PrepareEditMealInteractor(presenter, dataAccess).execute(new PrepareEditMealInputData(5));

        assertEquals(5, captured[0].getMealId());
        assertEquals(LocalDate.of(2026, 8, 6), captured[0].getDate());
        assertEquals("Lunch", captured[0].getName());
        assertEquals(1, captured[0].getFoodEntries().size());
        assertEquals(11, captured[0].getFoodEntries().get(0).getId());
        assertEquals("Chicken Breast", captured[0].getFoodEntries().get(0).getFoodName());
    }

    @Test
    public void executeWhenDataAccessThrowsPreparesFailView() {
        final ViewMealDataAccessInterface failingDataAccess = new ViewMealDataAccessInterface() {
            @Override
            public List<Meal> getMealsForUser(String userId) {
                return List.of();
            }

            @Override
            public List<FoodEntry> getFoodEntriesForMeal(int mealId) {
                return List.of();
            }

            @Override
            public Meal getMealById(int mealId) {
                throw new DataAccessException("db unavailable");
            }
        };
        final boolean[] failed = {false};
        final PrepareEditMealOutputBoundary presenter = new PrepareEditMealOutputBoundary() {
            @Override
            public void prepareSuccessView(PrepareEditMealOutputData outputData) {
                throw new AssertionError("Expected failure view");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                failed[0] = true;
                assertFalse(errorMessage.isEmpty());
            }
        };

        new PrepareEditMealInteractor(presenter, failingDataAccess).execute(new PrepareEditMealInputData(5));

        assertTrue(failed[0]);
    }
}
