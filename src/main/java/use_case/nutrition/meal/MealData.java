package use_case.nutrition.meal;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import entity.FoodEntry;
import entity.FoodNutrition;
import entity.Meal;
import use_case.nutrition.food.FoodEntryData;
import use_case.nutrition.food.FoodNutritionData;

/**
 * Use case boundary DTO mirroring Meal entity.
 */
public class MealData {

    private final int id;
    private final String userId;
    private final LocalDate date;
    private final String name;
    private final List<FoodEntryData> foodEntries;

    public MealData(final int id, final String userId, final LocalDate date, final String name,
                    final List<FoodEntryData> foodEntries) {
        this.id = id;
        this.userId = userId;
        this.date = date;
        this.name = name;
        this.foodEntries = foodEntries;
    }

    /**
     * Converts a {@code entity.Meal} into its boundary DTO form.
     *
     * @param meal the entity to convert
     * @return the equivalent DTO
     */
    public static MealData from(final Meal meal) {
        final List<FoodEntryData> foodEntries = new ArrayList<>();
        for (final FoodEntry food : meal.getFoodEntries()) {
            foodEntries.add(toFoodEntryData(food));
        }
        return new MealData(meal.getId(), meal.getUserId(), meal.getDate(), meal.getName(), foodEntries);
    }

    private static FoodEntryData toFoodEntryData(final FoodEntry food) {
        final FoodNutrition nutrition = food.getNutrition();
        final FoodNutritionData nutritionData = new FoodNutritionData(
                nutrition.getCalories(), nutrition.getProtein(), nutrition.getCarbs(), nutrition.getFat());
        return new FoodEntryData(food.getId(), food.getFoodName(), nutritionData,
                food.getQuantity(), food.getUnit(), food.getGrams());
    }

    public int getId() {
        return this.id;
    }

    public String getUserId() {
        return this.userId;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public String getName() {
        return this.name;
    }

    public List<FoodEntryData> getFoodEntries() {
        return this.foodEntries;
    }
}
