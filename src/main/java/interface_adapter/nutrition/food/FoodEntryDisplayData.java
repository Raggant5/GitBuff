package interface_adapter.nutrition.food;

import entity.FoodEntry;
import entity.FoodUnit;

/**
 * Display-only view of a FoodEntry, for rendering in panels/lists without the panel depending on the
 * entity directly.
 */
public class FoodEntryDisplayData {

    private final FoodEntry foodEntry;

    public FoodEntryDisplayData(FoodEntry foodEntry) {
        this.foodEntry = foodEntry;
    }

    public String getFoodName() {
        return foodEntry.getFoodName();
    }

    public double getCalories() {
        return foodEntry.getNutrition().getCalories();
    }

    public double getProtein() {
        return foodEntry.getNutrition().getProtein();
    }

    public double getFat() {
        return foodEntry.getNutrition().getFat();
    }

    public double getCarbs() {
        return foodEntry.getNutrition().getCarbs();
    }

    public double getQuantity() {
        return foodEntry.getQuantity();
    }

    public FoodUnit getUnit() {
        return foodEntry.getUnit();
    }

    public double getGrams() {
        return foodEntry.getGrams();
    }

    /**
     * Gets the underlying entity for the Edit/Delete controllers.
     * @return the wrapped food entry
     */
    public FoodEntry getEntity() {
        return foodEntry;
    }
}
