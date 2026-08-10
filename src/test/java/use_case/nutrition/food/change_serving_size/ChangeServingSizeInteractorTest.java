package use_case.nutrition.food.change_serving_size;

import static org.junit.jupiter.api.Assertions.assertEquals;

import entity.FoodUnit;
import org.junit.jupiter.api.Test;

public class ChangeServingSizeInteractorTest {

    @Test
    public void executeWithNonDefaultUnitScalesNutritionByGramsPerUnitRatio() {
        final ChangeServingSizeInputData inputData = new ChangeServingSizeInputData(
                FoodUnit.CUP, 0, 120.0, 200.0, 20.0, 15.0, 5.0);
        final ChangeServingSizeOutputData[] captured = new ChangeServingSizeOutputData[1];

        final ChangeServingSizeOutputBoundary presenter = outputData -> captured[0] = outputData;

        new ChangeServingSizeInteractor(presenter).execute(inputData);

        assertEquals(FoodUnit.CUP, captured[0].getUnit());
        assertEquals(240.0, captured[0].getServingGrams(), 0.0001);
        assertEquals(400.0, captured[0].getServingCalories(), 0.0001);
        assertEquals(40.0, captured[0].getServingProtein(), 0.0001);
        assertEquals(30.0, captured[0].getServingCarbs(), 0.0001);
        assertEquals(10.0, captured[0].getServingFat(), 0.0001);
    }

    @Test
    public void executeWithDefaultServingRestoresOriginalServingGrams() {
        final ChangeServingSizeInputData inputData = new ChangeServingSizeInputData(
                FoodUnit.DEFAULT_SERVING, 50.0, 100.0, 200.0, 20.0, 15.0, 5.0);
        final ChangeServingSizeOutputData[] captured = new ChangeServingSizeOutputData[1];

        final ChangeServingSizeOutputBoundary presenter = outputData -> captured[0] = outputData;

        new ChangeServingSizeInteractor(presenter).execute(inputData);

        assertEquals(50.0, captured[0].getServingGrams(), 0.0001);
        assertEquals(100.0, captured[0].getServingCalories(), 0.0001);
    }

    @Test
    public void executeWithDefaultServingAndNoOriginalGramsKeepsCurrentServingGrams() {
        final ChangeServingSizeInputData inputData = new ChangeServingSizeInputData(
                FoodUnit.DEFAULT_SERVING, 0, 100.0, 200.0, 20.0, 15.0, 5.0);
        final ChangeServingSizeOutputData[] captured = new ChangeServingSizeOutputData[1];

        final ChangeServingSizeOutputBoundary presenter = outputData -> captured[0] = outputData;

        new ChangeServingSizeInteractor(presenter).execute(inputData);

        assertEquals(100.0, captured[0].getServingGrams(), 0.0001);
        assertEquals(200.0, captured[0].getServingCalories(), 0.0001);
    }

    @Test
    public void executeWithZeroOldServingGramsSkipsScaling() {
        final ChangeServingSizeInputData inputData = new ChangeServingSizeInputData(
                FoodUnit.CUP, 0, 0, 200.0, 20.0, 15.0, 5.0);
        final ChangeServingSizeOutputData[] captured = new ChangeServingSizeOutputData[1];

        final ChangeServingSizeOutputBoundary presenter = outputData -> captured[0] = outputData;

        new ChangeServingSizeInteractor(presenter).execute(inputData);

        assertEquals(240.0, captured[0].getServingGrams(), 0.0001);
        assertEquals(200.0, captured[0].getServingCalories(), 0.0001);
        assertEquals(20.0, captured[0].getServingProtein(), 0.0001);
        assertEquals(15.0, captured[0].getServingCarbs(), 0.0001);
        assertEquals(5.0, captured[0].getServingFat(), 0.0001);
    }
}
