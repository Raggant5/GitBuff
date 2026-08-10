package use_case.nutrition.food.edit_food;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import entity.FoodEntryFactory;
import entity.FoodUnit;
import org.junit.jupiter.api.Test;
import use_case.nutrition.food.FoodNutritionInput;
import use_case.nutrition.food.FoodValidationErrors;

public class EditFoodInteractorTest {

    @Test
    public void executeWithExistingIdSucceedsAndReturnsSameId() {
        final EditFoodInputData inputData = new EditFoodInputData(9, "Chicken Breast",
                new FoodNutritionInput("200", "20", "0", "5"), "1", FoodUnit.GRAM, "150");

        final EditFoodOutputBoundary presenter = new EditFoodOutputBoundary() {
            @Override
            public void prepareSuccessView(EditFoodOutputData outputData) {
                assertEquals(9, outputData.getId());
                assertEquals("Chicken Breast", outputData.getFoodName());
                assertEquals(200.0, outputData.getNutrition().getCalories(), 0.0001);
                assertEquals(1, outputData.getQuantity(), 0.0001);
                assertEquals(FoodUnit.GRAM, outputData.getUnit());
                assertEquals(150, outputData.getGrams(), 0.0001);
            }

            @Override
            public void prepareFailView(FoodValidationErrors errors) {
                throw new AssertionError("Expected success view");
            }
        };

        new EditFoodInteractor(presenter, new FoodEntryFactory()).execute(inputData);
    }

    @Test
    public void executeWithUnsavedTempIdSucceedsAndKeepsId() {
        final EditFoodInputData inputData = new EditFoodInputData(-2, "Rice",
                new FoodNutritionInput("130", "3", "28", "0.3"), "1", FoodUnit.GRAM, "100");

        final EditFoodOutputBoundary presenter = new EditFoodOutputBoundary() {
            @Override
            public void prepareSuccessView(EditFoodOutputData outputData) {
                assertEquals(-2, outputData.getId());
            }

            @Override
            public void prepareFailView(FoodValidationErrors errors) {
                throw new AssertionError("Expected success view");
            }
        };

        new EditFoodInteractor(presenter, new FoodEntryFactory()).execute(inputData);
    }

    @Test
    public void executeWithBlankNameFails() {
        final EditFoodInputData inputData = new EditFoodInputData(9, "",
                new FoodNutritionInput("200", "20", "0", "5"), "1", FoodUnit.GRAM, "150");
        final boolean[] failed = {false};

        final EditFoodOutputBoundary presenter = new EditFoodOutputBoundary() {
            @Override
            public void prepareSuccessView(EditFoodOutputData outputData) {
                throw new AssertionError("Expected failure view");
            }

            @Override
            public void prepareFailView(FoodValidationErrors errors) {
                failed[0] = true;
                assertFalse(errors.getGeneralError().isEmpty());
            }
        };

        new EditFoodInteractor(presenter, new FoodEntryFactory()).execute(inputData);
        assertTrue(failed[0]);
    }

    @Test
    public void executeWithNullNameFails() {
        final EditFoodInputData inputData = new EditFoodInputData(9, null,
                new FoodNutritionInput("200", "20", "0", "5"), "1", FoodUnit.GRAM, "150");
        final boolean[] failed = {false};

        final EditFoodOutputBoundary presenter = new EditFoodOutputBoundary() {
            @Override
            public void prepareSuccessView(EditFoodOutputData outputData) {
                throw new AssertionError("Expected failure view");
            }

            @Override
            public void prepareFailView(FoodValidationErrors errors) {
                failed[0] = true;
                assertFalse(errors.getGeneralError().isEmpty());
            }
        };

        new EditFoodInteractor(presenter, new FoodEntryFactory()).execute(inputData);
        assertTrue(failed[0]);
    }

    @Test
    public void executeWithNegativeCaloriesFailsWithCaloriesError() {
        final EditFoodInputData inputData = new EditFoodInputData(9, "Chicken Breast",
                new FoodNutritionInput("-200", "20", "0", "5"), "1", FoodUnit.GRAM, "150");
        final boolean[] failed = {false};

        final EditFoodOutputBoundary presenter = new EditFoodOutputBoundary() {
            @Override
            public void prepareSuccessView(EditFoodOutputData outputData) {
                throw new AssertionError("Expected failure view");
            }

            @Override
            public void prepareFailView(FoodValidationErrors errors) {
                failed[0] = true;
                assertFalse(errors.getCaloriesError().isEmpty());
            }
        };

        new EditFoodInteractor(presenter, new FoodEntryFactory()).execute(inputData);
        assertTrue(failed[0]);
    }

    @Test
    public void executeWithNegativeProteinFailsWithProteinError() {
        final EditFoodInputData inputData = new EditFoodInputData(9, "Chicken Breast",
                new FoodNutritionInput("200", "-20", "0", "5"), "1", FoodUnit.GRAM, "150");
        final boolean[] failed = {false};

        final EditFoodOutputBoundary presenter = new EditFoodOutputBoundary() {
            @Override
            public void prepareSuccessView(EditFoodOutputData outputData) {
                throw new AssertionError("Expected failure view");
            }

            @Override
            public void prepareFailView(FoodValidationErrors errors) {
                failed[0] = true;
                assertFalse(errors.getProteinError().isEmpty());
            }
        };

        new EditFoodInteractor(presenter, new FoodEntryFactory()).execute(inputData);
        assertTrue(failed[0]);
    }

    @Test
    public void executeWithNegativeCarbsFailsWithCarbsError() {
        final EditFoodInputData inputData = new EditFoodInputData(9, "Chicken Breast",
                new FoodNutritionInput("200", "20", "-1", "5"), "1", FoodUnit.GRAM, "150");
        final boolean[] failed = {false};

        final EditFoodOutputBoundary presenter = new EditFoodOutputBoundary() {
            @Override
            public void prepareSuccessView(EditFoodOutputData outputData) {
                throw new AssertionError("Expected failure view");
            }

            @Override
            public void prepareFailView(FoodValidationErrors errors) {
                failed[0] = true;
                assertFalse(errors.getCarbsError().isEmpty());
            }
        };

        new EditFoodInteractor(presenter, new FoodEntryFactory()).execute(inputData);
        assertTrue(failed[0]);
    }

    @Test
    public void executeWithNegativeFatFailsWithFatError() {
        final EditFoodInputData inputData = new EditFoodInputData(9, "Chicken Breast",
                new FoodNutritionInput("200", "20", "0", "-5"), "1", FoodUnit.GRAM, "150");
        final boolean[] failed = {false};

        final EditFoodOutputBoundary presenter = new EditFoodOutputBoundary() {
            @Override
            public void prepareSuccessView(EditFoodOutputData outputData) {
                throw new AssertionError("Expected failure view");
            }

            @Override
            public void prepareFailView(FoodValidationErrors errors) {
                failed[0] = true;
                assertFalse(errors.getFatError().isEmpty());
            }
        };

        new EditFoodInteractor(presenter, new FoodEntryFactory()).execute(inputData);
        assertTrue(failed[0]);
    }

    @Test
    public void executeWithNonNumericQuantityFailsWithQuantityError() {
        final EditFoodInputData inputData = new EditFoodInputData(9, "Chicken Breast",
                new FoodNutritionInput("200", "20", "0", "5"), "not-a-number", FoodUnit.GRAM, "150");
        final boolean[] failed = {false};

        final EditFoodOutputBoundary presenter = new EditFoodOutputBoundary() {
            @Override
            public void prepareSuccessView(EditFoodOutputData outputData) {
                throw new AssertionError("Expected failure view");
            }

            @Override
            public void prepareFailView(FoodValidationErrors errors) {
                failed[0] = true;
                assertFalse(errors.getQuantityError().isEmpty());
            }
        };

        new EditFoodInteractor(presenter, new FoodEntryFactory()).execute(inputData);
        assertTrue(failed[0]);
    }

    @Test
    public void executeWithNegativeGramsFailsWithGramsError() {
        final EditFoodInputData inputData = new EditFoodInputData(9, "Chicken Breast",
                new FoodNutritionInput("200", "20", "0", "5"), "1", FoodUnit.GRAM, "-150");
        final boolean[] failed = {false};

        final EditFoodOutputBoundary presenter = new EditFoodOutputBoundary() {
            @Override
            public void prepareSuccessView(EditFoodOutputData outputData) {
                throw new AssertionError("Expected failure view");
            }

            @Override
            public void prepareFailView(FoodValidationErrors errors) {
                failed[0] = true;
                assertFalse(errors.getGramsError().isEmpty());
            }
        };

        new EditFoodInteractor(presenter, new FoodEntryFactory()).execute(inputData);
        assertTrue(failed[0]);
    }
}
