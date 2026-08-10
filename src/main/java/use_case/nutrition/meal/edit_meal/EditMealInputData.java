package use_case.nutrition.meal.edit_meal;

import java.util.List;

import use_case.nutrition.food.FoodEntryInputData;

public class EditMealInputData {

    private final int mealId;
    private final String name;
    private final List<FoodEntryInputData> foodEntries;
    private final List<Integer> foodEntryIdsToDelete;

    public EditMealInputData(int mealId, String name, List<FoodEntryInputData> foodEntries,
                             List<Integer> foodEntryIdsToDelete) {
        this.mealId = mealId;
        this.name = name;
        this.foodEntries = foodEntries;
        this.foodEntryIdsToDelete = foodEntryIdsToDelete;
    }

    public int getMealId() {
        return mealId;
    }

    public String getName() {
        return name;
    }

    public List<FoodEntryInputData> getFoodEntries() {
        return foodEntries;
    }

    public List<Integer> getFoodEntryIdsToDelete() {
        return foodEntryIdsToDelete;
    }
}
