package use_case.nutrition.food.change_serving_size;

import entity.FoodNutrition;
import entity.FoodUnit;

public class ChangeServingSizeInteractor implements ChangeServingSizeInputBoundary {

    private final ChangeServingSizeOutputBoundary presenter;

    public ChangeServingSizeInteractor(ChangeServingSizeOutputBoundary presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute(ChangeServingSizeInputData inputData) {
        final FoodUnit unit = inputData.getUnit();
        final double oldServingGrams = inputData.getServingGrams();
        final double newServingGrams;
        if (unit == FoodUnit.DEFAULT_SERVING) {
            if (inputData.getOriginalServingGrams() != 0) {
                newServingGrams = inputData.getOriginalServingGrams();
            }
            else {
                newServingGrams = oldServingGrams;
            }
        }
        else {
            newServingGrams = unit.getGramsPerUnit();
        }

        FoodNutrition nutrition = new FoodNutrition(inputData.getServingCalories(), inputData.getServingProtein(),
                inputData.getServingCarbs(), inputData.getServingFat());
        if (oldServingGrams != 0) {
            nutrition = nutrition.scaledTo(newServingGrams / oldServingGrams);
        }

        presenter.prepareSuccessView(new ChangeServingSizeOutputData(unit, newServingGrams,
                nutrition.getCalories(), nutrition.getProtein(), nutrition.getCarbs(), nutrition.getFat()));
    }
}
