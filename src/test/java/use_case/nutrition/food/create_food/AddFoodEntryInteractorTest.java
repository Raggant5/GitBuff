package use_case.nutrition.food.create_food;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import entity.FoodEntryFactory;
import entity.FoodUnit;
import org.junit.jupiter.api.Test;
import use_case.nutrition.food.FoodNutritionInput;
import use_case.nutrition.food.FoodValidationErrors;

public class AddFoodEntryInteractorTest {

    @Test
    public void executeWithValidInputSucceeds() {
        final AddFoodEntryInputData inputData = new AddFoodEntryInputData("Chicken Breast",
                new FoodNutritionInput("200", "20", "0", "5"), "1", FoodUnit.GRAM, "150");

        final AddFoodEntryOutputBoundary presenter = new AddFoodEntryOutputBoundary() {
            @Override
            public void prepareSuccessView(AddFoodEntryOutputData outputData) {
                assertEquals("Chicken Breast", outputData.getFoodName());
                assertEquals(200.0, outputData.getNutrition().getCalories(), 0.0001);
                assertEquals(20.0, outputData.getNutrition().getProtein(), 0.0001);
                assertEquals(0.0, outputData.getNutrition().getCarbs(), 0.0001);
                assertEquals(5.0, outputData.getNutrition().getFat(), 0.0001);
                assertEquals(1, outputData.getQuantity(), 0.0001);
                assertEquals(FoodUnit.GRAM, outputData.getUnit());
                assertEquals(150, outputData.getGrams(), 0.0001);
                assertNull(outputData.getId());
            }

            @Override
            public void prepareFailView(FoodValidationErrors errors) {
                throw new AssertionError("Expected success view");
            }
        };

        new AddFoodEntryInteractor(presenter, new FoodEntryFactory()).execute(inputData);
    }

    @Test
    public void executeWithBlankNameFails() {
        final AddFoodEntryInputData inputData = new AddFoodEntryInputData("",
                new FoodNutritionInput("200", "20", "0", "5"), "1", FoodUnit.GRAM, "150");
        final boolean[] failed = {false};

        final AddFoodEntryOutputBoundary presenter = new AddFoodEntryOutputBoundary() {
            @Override
            public void prepareSuccessView(AddFoodEntryOutputData outputData) {
                throw new AssertionError("Expected failure view");
            }

            @Override
            public void prepareFailView(FoodValidationErrors errors) {
                failed[0] = true;
                assertFalse(errors.getGeneralError().isEmpty());
            }
        };

        new AddFoodEntryInteractor(presenter, new FoodEntryFactory()).execute(inputData);
        assertTrue(failed[0]);
    }

    @Test
    public void executeWithNullNameFails() {
        final AddFoodEntryInputData inputData = new AddFoodEntryInputData(null,
                new FoodNutritionInput("200", "20", "0", "5"), "1", FoodUnit.GRAM, "150");
        final boolean[] failed = {false};

        final AddFoodEntryOutputBoundary presenter = new AddFoodEntryOutputBoundary() {
            @Override
            public void prepareSuccessView(AddFoodEntryOutputData outputData) {
                throw new AssertionError("Expected failure view");
            }

            @Override
            public void prepareFailView(FoodValidationErrors errors) {
                failed[0] = true;
                assertFalse(errors.getGeneralError().isEmpty());
            }
        };

        new AddFoodEntryInteractor(presenter, new FoodEntryFactory()).execute(inputData);
        assertTrue(failed[0]);
    }

    @Test
    public void executeWithNegativeCaloriesFailsWithCaloriesError() {
        final AddFoodEntryInputData inputData = new AddFoodEntryInputData("Chicken Breast",
                new FoodNutritionInput("-5", "20", "0", "5"), "1", FoodUnit.GRAM, "150");
        final boolean[] failed = {false};

        final AddFoodEntryOutputBoundary presenter = new AddFoodEntryOutputBoundary() {
            @Override
            public void prepareSuccessView(AddFoodEntryOutputData outputData) {
                throw new AssertionError("Expected failure view");
            }

            @Override
            public void prepareFailView(FoodValidationErrors errors) {
                failed[0] = true;
                assertFalse(errors.getCaloriesError().isEmpty());
            }
        };

        new AddFoodEntryInteractor(presenter, new FoodEntryFactory()).execute(inputData);
        assertTrue(failed[0]);
    }

    @Test
    public void executeWithNegativeProteinFailsWithProteinError() {
        final AddFoodEntryInputData inputData = new AddFoodEntryInputData("Chicken Breast",
                new FoodNutritionInput("200", "-20", "0", "5"), "1", FoodUnit.GRAM, "150");
        final boolean[] failed = {false};

        final AddFoodEntryOutputBoundary presenter = new AddFoodEntryOutputBoundary() {
            @Override
            public void prepareSuccessView(AddFoodEntryOutputData outputData) {
                throw new AssertionError("Expected failure view");
            }

            @Override
            public void prepareFailView(FoodValidationErrors errors) {
                failed[0] = true;
                assertFalse(errors.getProteinError().isEmpty());
            }
        };

        new AddFoodEntryInteractor(presenter, new FoodEntryFactory()).execute(inputData);
        assertTrue(failed[0]);
    }

    @Test
    public void executeWithNegativeCarbsFailsWithCarbsError() {
        final AddFoodEntryInputData inputData = new AddFoodEntryInputData("Chicken Breast",
                new FoodNutritionInput("200", "20", "-1", "5"), "1", FoodUnit.GRAM, "150");
        final boolean[] failed = {false};

        final AddFoodEntryOutputBoundary presenter = new AddFoodEntryOutputBoundary() {
            @Override
            public void prepareSuccessView(AddFoodEntryOutputData outputData) {
                throw new AssertionError("Expected failure view");
            }

            @Override
            public void prepareFailView(FoodValidationErrors errors) {
                failed[0] = true;
                assertFalse(errors.getCarbsError().isEmpty());
            }
        };

        new AddFoodEntryInteractor(presenter, new FoodEntryFactory()).execute(inputData);
        assertTrue(failed[0]);
    }

    @Test
    public void executeWithNegativeFatFailsWithFatError() {
        final AddFoodEntryInputData inputData = new AddFoodEntryInputData("Chicken Breast",
                new FoodNutritionInput("200", "20", "0", "-5"), "1", FoodUnit.GRAM, "150");
        final boolean[] failed = {false};

        final AddFoodEntryOutputBoundary presenter = new AddFoodEntryOutputBoundary() {
            @Override
            public void prepareSuccessView(AddFoodEntryOutputData outputData) {
                throw new AssertionError("Expected failure view");
            }

            @Override
            public void prepareFailView(FoodValidationErrors errors) {
                failed[0] = true;
                assertFalse(errors.getFatError().isEmpty());
            }
        };

        new AddFoodEntryInteractor(presenter, new FoodEntryFactory()).execute(inputData);
        assertTrue(failed[0]);
    }

    @Test
    public void executeWithNegativeGramsFailsWithGramsError() {
        final AddFoodEntryInputData inputData = new AddFoodEntryInputData("Chicken Breast",
                new FoodNutritionInput("200", "20", "0", "5"), "1", FoodUnit.GRAM, "-150");
        final boolean[] failed = {false};

        final AddFoodEntryOutputBoundary presenter = new AddFoodEntryOutputBoundary() {
            @Override
            public void prepareSuccessView(AddFoodEntryOutputData outputData) {
                throw new AssertionError("Expected failure view");
            }

            @Override
            public void prepareFailView(FoodValidationErrors errors) {
                failed[0] = true;
                assertFalse(errors.getGramsError().isEmpty());
            }
        };

        new AddFoodEntryInteractor(presenter, new FoodEntryFactory()).execute(inputData);
        assertTrue(failed[0]);
    }

    @Test
    public void executeWithNonNumericQuantityFailsWithQuantityError() {
        final AddFoodEntryInputData inputData = new AddFoodEntryInputData("Chicken Breast",
                new FoodNutritionInput("200", "20", "0", "5"), "not-a-number", FoodUnit.GRAM, "150");
        final boolean[] failed = {false};

        final AddFoodEntryOutputBoundary presenter = new AddFoodEntryOutputBoundary() {
            @Override
            public void prepareSuccessView(AddFoodEntryOutputData outputData) {
                throw new AssertionError("Expected failure view");
            }

            @Override
            public void prepareFailView(FoodValidationErrors errors) {
                failed[0] = true;
                assertFalse(errors.getQuantityError().isEmpty());
            }
        };

        new AddFoodEntryInteractor(presenter, new FoodEntryFactory()).execute(inputData);
        assertTrue(failed[0]);
    }
}
