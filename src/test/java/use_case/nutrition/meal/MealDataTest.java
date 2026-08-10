package use_case.nutrition.meal;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import entity.FoodEntry;
import entity.FoodNutrition;
import entity.FoodUnit;
import entity.Meal;

class MealDataTest {

    private static final double CALORIES = 200.0;
    private static final double PROTEIN = 10.0;
    private static final double CARBS = 30.0;
    private static final double FAT = 5.0;
    private static final double QUANTITY = 1.0;
    private static final double GRAMS = 100.0;

    @Test
    void fromConvertsMealWithFoodEntries() {
        final Meal meal = new Meal("aahir", LocalDate.of(2026, 1, 1), "Breakfast");
        meal.setId(1);
        final FoodNutrition nutrition = new FoodNutrition(CALORIES, PROTEIN, CARBS, FAT);
        final FoodEntry foodEntry = new FoodEntry("Oatmeal", nutrition, QUANTITY, FoodUnit.GRAM, GRAMS);
        meal.getFoodEntries().add(foodEntry);

        final MealData mealData = MealData.from(meal);

        assertEquals("aahir", mealData.getUserId());
        assertEquals("Breakfast", mealData.getName());
        assertEquals(1, mealData.getFoodEntries().size());
        assertEquals("Oatmeal", mealData.getFoodEntries().get(0).getFoodName());
        assertEquals(CALORIES, mealData.getFoodEntries().get(0).getNutrition().getCalories());
    }
}
