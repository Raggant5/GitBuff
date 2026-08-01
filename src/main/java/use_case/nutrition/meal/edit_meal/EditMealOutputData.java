package use_case.nutrition.meal.edit_meal;

import entity.Meal;

public class EditMealOutputData {

    private final Meal meal;

    public EditMealOutputData(Meal meal) {
        this.meal = meal;
    }

    public Meal getMeal() {
        return meal;
    }

}
