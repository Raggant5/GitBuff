package use_case.nutrition.meal.prepare_edit_meal;

import entity.Meal;

public class PrepareEditMealOutputData {

    private final Meal meal;

    public PrepareEditMealOutputData(Meal meal) {
        this.meal = meal;
    }

    public Meal getMeal() {
        return meal;
    }
}
