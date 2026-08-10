package use_case.nutrition.meal.edit_meal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import entity.FoodEntry;
import entity.FoodUnit;
import entity.Meal;
import org.junit.jupiter.api.Test;
import use_case.DataAccessException;
import use_case.nutrition.food.FoodEntryInputData;
import use_case.nutrition.food.FoodNutritionInput;
import use_case.EventPublisher;
import use_case.nutrition.meal.MealChangedEvent;
import use_case.nutrition.meal.get_meals.ViewMealDataAccessInterface;

public class EditMealInteractorTest {

    @Test
    public void executeSavesNewFoodEntryAndUpdatesExistingFoodEntry() {
        final Meal existingMeal = new Meal("amir", LocalDate.of(2026, 8, 1), "Lunch");
        existingMeal.setId(5);
        final FakeViewMealDataAccessObject viewDataAccess = new FakeViewMealDataAccessObject(existingMeal);
        final FakeEditMealDataAccessObject editDataAccess = new FakeEditMealDataAccessObject();

        final FoodEntryInputData newFood = new FoodEntryInputData(null, "Chicken Breast",
                new FoodNutritionInput("200", "20", "0", "5"), 1.0, FoodUnit.GRAM, 150.0);
        final FoodEntryInputData existingFood = new FoodEntryInputData(2, "Rice",
                new FoodNutritionInput("130", "3", "28", "0.3"), 1.0, FoodUnit.GRAM, 100.0);
        final EditMealInputData inputData = new EditMealInputData(5, "Updated Lunch",
                List.of(newFood, existingFood), List.of());

        final EditMealOutputBoundary presenter = new EditMealOutputBoundary() {
            @Override
            public void prepareSuccessView(EditMealOutputData outputData) {
                assertEquals(5, outputData.getId());
                assertEquals("Updated Lunch", outputData.getName());
                assertEquals(LocalDate.of(2026, 8, 1), outputData.getDate());
                assertEquals(2, outputData.getFoodEntries().size());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                throw new AssertionError("Expected success view, got failure: " + errorMessage);
            }
        };

        new EditMealInteractor(presenter, editDataAccess, viewDataAccess,
                new entity.FoodEntryFactory(), new FakeMealEventPublisher()).execute(inputData);

        assertEquals(1, editDataAccess.savedNewEntries.size());
        assertTrue(editDataAccess.editedEntries.stream().anyMatch(food -> food.getId() == 2));
        assertEquals("Updated Lunch", editDataAccess.editedMeal.getName());
    }

    @Test
    public void executeTreatsTempNegativeFoodEntryIdAsNotYetSaved() {
        final Meal existingMeal = new Meal("amir", LocalDate.now(), "Lunch");
        existingMeal.setId(5);
        final FakeViewMealDataAccessObject viewDataAccess = new FakeViewMealDataAccessObject(existingMeal);
        final FakeEditMealDataAccessObject editDataAccess = new FakeEditMealDataAccessObject();

        final FoodEntryInputData tempFood = new FoodEntryInputData(-3, "Chicken Breast",
                new FoodNutritionInput("200", "20", "0", "5"), 1.0, FoodUnit.GRAM, 150.0);
        final EditMealInputData inputData = new EditMealInputData(5, "Lunch", List.of(tempFood), List.of());

        final EditMealOutputBoundary presenter = new EditMealOutputBoundary() {
            @Override
            public void prepareSuccessView(EditMealOutputData outputData) {
                // no-op
            }

            @Override
            public void prepareFailView(String errorMessage) {
                throw new AssertionError("Expected success view, got failure: " + errorMessage);
            }
        };

        new EditMealInteractor(presenter, editDataAccess, viewDataAccess,
                new entity.FoodEntryFactory(), new FakeMealEventPublisher()).execute(inputData);

        assertEquals(1, editDataAccess.savedNewEntries.size());
        assertTrue(editDataAccess.editedEntries.isEmpty());
    }

    @Test
    public void executeWithNonNumericFoodNutritionDefaultsToZero() {
        final Meal existingMeal = new Meal("amir", LocalDate.of(2026, 8, 1), "Lunch");
        existingMeal.setId(5);
        final FakeViewMealDataAccessObject viewDataAccess = new FakeViewMealDataAccessObject(existingMeal);
        final FakeEditMealDataAccessObject editDataAccess = new FakeEditMealDataAccessObject();

        final FoodEntryInputData badFood = new FoodEntryInputData(null, "Mystery Snack",
                new FoodNutritionInput("not-a-number", "20", "0", "5"), 1.0, FoodUnit.GRAM, 150.0);
        final EditMealInputData inputData = new EditMealInputData(5, "Lunch", List.of(badFood), List.of());

        final EditMealOutputBoundary presenter = new EditMealOutputBoundary() {
            @Override
            public void prepareSuccessView(EditMealOutputData outputData) {
                assertEquals(0.0, outputData.getFoodEntries().get(0).getNutrition().getCalories(), 0.0001);
            }

            @Override
            public void prepareFailView(String errorMessage) {
                throw new AssertionError("Expected success view, got failure: " + errorMessage);
            }
        };

        new EditMealInteractor(presenter, editDataAccess, viewDataAccess,
                new entity.FoodEntryFactory(), new FakeMealEventPublisher()).execute(inputData);
    }

    @Test
    public void executeOnlyDeletesFoodEntriesWithRealPersistedIds() {
        final Meal existingMeal = new Meal("amir", LocalDate.now(), "Lunch");
        existingMeal.setId(5);
        final FakeViewMealDataAccessObject viewDataAccess = new FakeViewMealDataAccessObject(existingMeal);
        final FakeEditMealDataAccessObject editDataAccess = new FakeEditMealDataAccessObject();

        final EditMealInputData inputData = new EditMealInputData(5, "Lunch", List.of(),
                Arrays.asList(3, null, -1, -2));

        final EditMealOutputBoundary presenter = new EditMealOutputBoundary() {
            @Override
            public void prepareSuccessView(EditMealOutputData outputData) {
                // no-op
            }

            @Override
            public void prepareFailView(String errorMessage) {
                throw new AssertionError("Expected success view, got failure: " + errorMessage);
            }
        };

        new EditMealInteractor(presenter, editDataAccess, viewDataAccess,
                new entity.FoodEntryFactory(), new FakeMealEventPublisher()).execute(inputData);

        assertEquals(List.of(3), editDataAccess.deletedIds);
    }

    @Test
    public void executeWithBlankNameFails() {
        final Meal existingMeal = new Meal("amir", LocalDate.now(), "Lunch");
        existingMeal.setId(5);
        final FakeViewMealDataAccessObject viewDataAccess = new FakeViewMealDataAccessObject(existingMeal);
        final FakeEditMealDataAccessObject editDataAccess = new FakeEditMealDataAccessObject();
        final EditMealInputData inputData = new EditMealInputData(5, "", List.of(), List.of());
        final boolean[] failed = {false};

        final EditMealOutputBoundary presenter = new EditMealOutputBoundary() {
            @Override
            public void prepareSuccessView(EditMealOutputData outputData) {
                throw new AssertionError("Expected failure view");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                failed[0] = true;
                assertFalse(errorMessage.isEmpty());
            }
        };

        new EditMealInteractor(presenter, editDataAccess, viewDataAccess,
                new entity.FoodEntryFactory(), new FakeMealEventPublisher()).execute(inputData);
        assertTrue(failed[0]);
    }

    @Test
    public void executeWithNullNameFails() {
        final Meal existingMeal = new Meal("amir", LocalDate.now(), "Lunch");
        existingMeal.setId(5);
        final FakeViewMealDataAccessObject viewDataAccess = new FakeViewMealDataAccessObject(existingMeal);
        final FakeEditMealDataAccessObject editDataAccess = new FakeEditMealDataAccessObject();
        final EditMealInputData inputData = new EditMealInputData(5, null, List.of(), List.of());
        final boolean[] failed = {false};

        final EditMealOutputBoundary presenter = new EditMealOutputBoundary() {
            @Override
            public void prepareSuccessView(EditMealOutputData outputData) {
                throw new AssertionError("Expected failure view");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                failed[0] = true;
                assertFalse(errorMessage.isEmpty());
            }
        };

        new EditMealInteractor(presenter, editDataAccess, viewDataAccess,
                new entity.FoodEntryFactory(), new FakeMealEventPublisher()).execute(inputData);
        assertTrue(failed[0]);
    }

    @Test
    public void executeWhenDataAccessThrowsPreparesFailView() {
        final Meal existingMeal = new Meal("amir", LocalDate.now(), "Lunch");
        existingMeal.setId(5);
        final FakeViewMealDataAccessObject viewDataAccess = new FakeViewMealDataAccessObject(existingMeal);
        final EditMealDataAccessInterface failingDataAccess = (meal, foodEntryIdsToDelete) -> {
            throw new DataAccessException("db unavailable");
        };
        final EditMealInputData inputData = new EditMealInputData(5, "Lunch", List.of(), List.of());
        final boolean[] failed = {false};

        final EditMealOutputBoundary presenter = new EditMealOutputBoundary() {
            @Override
            public void prepareSuccessView(EditMealOutputData outputData) {
                throw new AssertionError("Expected failure view");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                failed[0] = true;
                assertFalse(errorMessage.isEmpty());
            }
        };

        new EditMealInteractor(presenter, failingDataAccess, viewDataAccess,
                new entity.FoodEntryFactory(), new FakeMealEventPublisher()).execute(inputData);
        assertTrue(failed[0]);
    }

    private static final class FakeViewMealDataAccessObject implements ViewMealDataAccessInterface {
        private final Meal meal;

        private FakeViewMealDataAccessObject(Meal meal) {
            this.meal = meal;
        }

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
            return meal;
        }
    }

    private static final class FakeEditMealDataAccessObject implements EditMealDataAccessInterface {
        private final List<FoodEntry> savedNewEntries = new ArrayList<>();
        private final List<FoodEntry> editedEntries = new ArrayList<>();
        private final List<Integer> deletedIds = new ArrayList<>();
        private Meal editedMeal;
        private int nextFoodEntryId = 100;

        @Override
        public Meal editMeal(Meal meal, List<Integer> foodEntryIdsToDelete) {
            for (Integer id : foodEntryIdsToDelete) {
                if (id != null && id > 0) {
                    deletedIds.add(id);
                }
            }

            for (FoodEntry food : meal.getFoodEntries()) {
                if (food.getId() == null) {
                    food.setId(nextFoodEntryId++);
                    savedNewEntries.add(food);
                }
                else {
                    editedEntries.add(food);
                }
            }

            editedMeal = meal;
            return meal;
        }
    }

    private static final class FakeMealEventPublisher implements EventPublisher<MealChangedEvent> {
        @Override
        public void publish(MealChangedEvent event) {
            // these tests don't assert on calendar side effects
        }
    }
}
