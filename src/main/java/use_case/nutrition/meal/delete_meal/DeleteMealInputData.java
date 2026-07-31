package use_case.nutrition.meal.delete_meal;

public class DeleteMealInputData {
    private final int mealId;

    public DeleteMealInputData(int mealId) {
        this.mealId = mealId;
    }

    public int getMealId() {
        return mealId;
    }

}
