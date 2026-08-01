package entity;

/**
 * Factory for creating food entries, a subset of meals.
 */
public class FoodEntryFactory {
    /**
     * Creates a new FoodEntry.
     * @param foodName name of the food/ingredient
     * @param nutrition the protein, calories, fat, and carbs consumed
     * @param quantity the total amount of food in the given unit
     * @param unit the unit of choice
     * @param grams the total amount of food in grams
     * @return the new FoodEntry
     */
    public FoodEntry create(String foodName, FoodNutrition nutrition, double quantity, FoodUnit unit, double grams) {

        return new FoodEntry(foodName, nutrition, quantity, unit, grams);
    }
}
