package use_case.nutrition.meal.add_meal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import entity.FoodEntry;
import entity.FoodEntryFactory;
import entity.FoodUnit;
import entity.Meal;
import entity.MealFactory;
import org.junit.jupiter.api.Test;
import use_case.DataAccessException;
import use_case.nutrition.food.FoodEntryInputData;
import use_case.nutrition.food.FoodNutritionInput;

public class AddMealInteractorTest {

    @Test
    public void executeWithFoodEntriesSavesMealAndAssignsGeneratedIds() {
        final FakeAddMealDataAccessObject dataAccess = new FakeAddMealDataAccessObject();
        final FoodEntryInputData foodData = new FoodEntryInputData(null, "Chicken Breast",
                new FoodNutritionInput("200", "20", "0", "5"), 1.0, FoodUnit.GRAM, 150.0);
        final AddMealInputData inputData = new AddMealInputData("Lunch", "amir", LocalDate.of(2026, 8, 6),
                List.of(foodData));

        final AddMealOutputBoundary presenter = new AddMealOutputBoundary() {
            @Override
            public void prepareSuccessView(AddMealOutputData outputData) {
                assertTrue(outputData.getId() > 0);
                assertEquals("amir", outputData.getUserId());
                assertEquals("Lunch", outputData.getName());
                assertEquals(LocalDate.of(2026, 8, 6), outputData.getDate());
                assertEquals(1, outputData.getFoodEntries().size());
                assertTrue(outputData.getFoodEntries().get(0).getId() > 0);
            }

            @Override
            public void prepareFailView(String errorMessage) {
                throw new AssertionError("Expected success view, got failure: " + errorMessage);
            }
        };

        new AddMealInteractor(presenter, dataAccess, new MealFactory(), new FoodEntryFactory()).execute(inputData);

        assertEquals(1, dataAccess.savedMeals.size());
        assertEquals(1, dataAccess.savedFoodEntries.size());
    }

    @Test
    public void executeWithNonNumericFoodNutritionDefaultsToZero() {
        final FakeAddMealDataAccessObject dataAccess = new FakeAddMealDataAccessObject();
        final FoodEntryInputData foodData = new FoodEntryInputData(null, "Mystery Snack",
                new FoodNutritionInput("not-a-number", "20", "0", "5"), 1.0, FoodUnit.GRAM, 150.0);
        final AddMealInputData inputData = new AddMealInputData("Lunch", "amir", LocalDate.of(2026, 8, 6),
                List.of(foodData));

        final AddMealOutputBoundary presenter = new AddMealOutputBoundary() {
            @Override
            public void prepareSuccessView(AddMealOutputData outputData) {
                assertEquals(0.0, outputData.getFoodEntries().get(0).getNutrition().getCalories(), 0.0001);
            }

            @Override
            public void prepareFailView(String errorMessage) {
                throw new AssertionError("Expected success view, got failure: " + errorMessage);
            }
        };

        new AddMealInteractor(presenter, dataAccess, new MealFactory(), new FoodEntryFactory()).execute(inputData);
    }

    @Test
    public void executeWithBlankNameFails() {
        final FakeAddMealDataAccessObject dataAccess = new FakeAddMealDataAccessObject();
        final AddMealInputData inputData = new AddMealInputData("", "amir", LocalDate.now(), List.of());
        final boolean[] failed = {false};

        final AddMealOutputBoundary presenter = new AddMealOutputBoundary() {
            @Override
            public void prepareSuccessView(AddMealOutputData outputData) {
                throw new AssertionError("Expected failure view");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                failed[0] = true;
                assertFalse(errorMessage.isEmpty());
            }
        };

        new AddMealInteractor(presenter, dataAccess, new MealFactory(), new FoodEntryFactory()).execute(inputData);
        assertTrue(failed[0]);
    }

    @Test
    public void executeWithNullNameFails() {
        final FakeAddMealDataAccessObject dataAccess = new FakeAddMealDataAccessObject();
        final AddMealInputData inputData = new AddMealInputData(null, "amir", LocalDate.now(), List.of());
        final boolean[] failed = {false};

        final AddMealOutputBoundary presenter = new AddMealOutputBoundary() {
            @Override
            public void prepareSuccessView(AddMealOutputData outputData) {
                throw new AssertionError("Expected failure view");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                failed[0] = true;
                assertFalse(errorMessage.isEmpty());
            }
        };

        new AddMealInteractor(presenter, dataAccess, new MealFactory(), new FoodEntryFactory()).execute(inputData);
        assertTrue(failed[0]);
    }

    @Test
    public void executeWhenDataAccessThrowsPreparesFailView() {
        final AddMealDataAccessInterface failingDataAccess = new AddMealDataAccessInterface() {
            @Override
            public int saveMeal(Meal meal) {
                throw new DataAccessException("db unavailable");
            }

            @Override
            public int saveFoodEntry(FoodEntry foodEntry) {
                throw new DataAccessException("db unavailable");
            }
        };
        final AddMealInputData inputData = new AddMealInputData("Lunch", "amir", LocalDate.now(), List.of());
        final boolean[] failed = {false};

        final AddMealOutputBoundary presenter = new AddMealOutputBoundary() {
            @Override
            public void prepareSuccessView(AddMealOutputData outputData) {
                throw new AssertionError("Expected failure view");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                failed[0] = true;
                assertFalse(errorMessage.isEmpty());
            }
        };

        new AddMealInteractor(presenter, failingDataAccess, new MealFactory(), new FoodEntryFactory())
                .execute(inputData);
        assertTrue(failed[0]);
    }

    private static final class FakeAddMealDataAccessObject implements AddMealDataAccessInterface {
        private final List<Meal> savedMeals = new ArrayList<>();
        private final List<FoodEntry> savedFoodEntries = new ArrayList<>();
        private int nextMealId = 1;
        private int nextFoodEntryId = 1;

        @Override
        public int saveMeal(Meal meal) {
            final int id = nextMealId++;
            meal.setId(id);
            savedMeals.add(meal);
            return id;
        }

        @Override
        public int saveFoodEntry(FoodEntry foodEntry) {
            final int id = nextFoodEntryId++;
            foodEntry.setId(id);
            savedFoodEntries.add(foodEntry);
            return id;
        }
    }
}
