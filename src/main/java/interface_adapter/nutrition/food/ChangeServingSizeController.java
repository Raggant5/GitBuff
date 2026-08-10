package interface_adapter.nutrition.food;

import use_case.nutrition.food.change_serving_size.ChangeServingSizeInputBoundary;
import use_case.nutrition.food.change_serving_size.ChangeServingSizeInputData;

public class ChangeServingSizeController {

    private final ChangeServingSizeInputBoundary changeServingSizeInteractor;

    public ChangeServingSizeController(ChangeServingSizeInputBoundary changeServingSizeInteractor) {
        this.changeServingSizeInteractor = changeServingSizeInteractor;
    }

    /**
     * Executes the Change Serving Size use case.
     * @param unit the newly selected unit
     * @param originalServingGrams the anchor grams to restore for DEFAULT_SERVING, 0 if not yet known
     * @param servingGrams the current per-serving grams before the change
     * @param servingCalories the current per-serving calories before the change
     * @param servingProtein the current per-serving protein before the change
     * @param servingCarbs the current per-serving carbs before the change
     * @param servingFat the current per-serving fat before the change
     */
    public void execute(FoodUnitOption unit, double originalServingGrams, double servingGrams,
                        double servingCalories, double servingProtein, double servingCarbs, double servingFat) {
        changeServingSizeInteractor.execute(new ChangeServingSizeInputData(FoodEnumMapper.toEntity(unit),
                originalServingGrams, servingGrams, servingCalories, servingProtein, servingCarbs, servingFat));
    }
}
