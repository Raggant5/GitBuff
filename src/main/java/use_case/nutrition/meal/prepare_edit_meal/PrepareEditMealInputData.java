package use_case.nutrition.meal.prepare_edit_meal;

public class PrepareEditMealInputData {

    private final int mealId;

    public PrepareEditMealInputData(int mealId) {
        this.mealId = mealId;
    }

    public int getMealId() {
        return mealId;
    }
}
