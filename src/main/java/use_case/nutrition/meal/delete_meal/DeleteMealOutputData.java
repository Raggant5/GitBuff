package use_case.nutrition.meal.delete_meal;

public class DeleteMealOutputData {

    private final int mealId;

    public DeleteMealOutputData(int mealId) {
        this.mealId = mealId;
    }

    public int getMealId() {
        return mealId;
    }
}
