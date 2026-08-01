package use_case.nutrition.meal.prepare_edit_meal;

import entity.Meal;

public class PrepareEditMealInputData {

    private final Meal meal;

    public PrepareEditMealInputData(Meal meal) {
        this.meal = meal;
    }

    public Meal getMeal() {
        return meal;
    }
}
