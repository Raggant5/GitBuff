package data_access;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import entity.FoodEntry;
import entity.FoodNutrition;
import entity.FoodUnit;
import entity.Meal;
import use_case.DataAccessException;
import use_case.dashboard.MacroData;

class SqliteMealDataAccessObjectTest {

    private SqliteMealDataAccessObject dao;
    private String username;

    @BeforeEach
    void setUp() throws SQLException {
        dao = new SqliteMealDataAccessObject();

        username = "test_" + UUID.randomUUID();

        final String sql =
                "INSERT INTO users (username, password) VALUES (?, ?)";

        try (Connection connection = Database.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, username);
            statement.setString(2, "password");

            statement.executeUpdate();
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        try (Connection connection = Database.connect()) {

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 "DELETE FROM food_entries "
                                         + "WHERE meal_id IN "
                                         + "(SELECT id FROM meals "
                                         + "WHERE user_id = ?)")) {

                statement.setString(1, username);
                statement.executeUpdate();
            }

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 "DELETE FROM meals "
                                         + "WHERE user_id = ?")) {

                statement.setString(1, username);
                statement.executeUpdate();
            }

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 "DELETE FROM users "
                                         + "WHERE username = ?")) {

                statement.setString(1, username);
                statement.executeUpdate();
            }
        }
    }

    @Test
    void saveMealSetsId() {
        final Meal meal =
                new Meal(
                        username,
                        LocalDate.now(),
                        "Breakfast"
                );

        final int mealId =
                dao.saveMeal(meal);

        assertTrue(mealId > 0);
        assertEquals(mealId, meal.getId());
    }

    @Test
    void saveFoodEntrySetsIdAndLoadsFood() {
        final Meal meal =
                new Meal(
                        username,
                        LocalDate.now(),
                        "Breakfast"
                );

        final int mealId =
                dao.saveMeal(meal);

        final FoodEntry food =
                makeFood(
                        "Oats",
                        300.0,
                        10.0,
                        50.0,
                        6.0
                );

        food.setMealId(mealId);

        final int foodId =
                dao.saveFoodEntry(food);

        assertTrue(foodId > 0);
        assertEquals(foodId, food.getId());
        assertEquals(mealId, food.getMealId());

        final List<FoodEntry> foods =
                dao.getFoodEntriesForMeal(mealId);

        assertEquals(1, foods.size());

        final FoodEntry loaded =
                foods.get(0);

        assertEquals(
                "Oats",
                loaded.getFoodName()
        );

        assertEquals(
                300.0,
                loaded.getNutrition().getCalories(),
                0.001
        );

        assertEquals(
                10.0,
                loaded.getNutrition().getProtein(),
                0.001
        );

        assertEquals(
                50.0,
                loaded.getNutrition().getCarbs(),
                0.001
        );

        assertEquals(
                6.0,
                loaded.getNutrition().getFat(),
                0.001
        );
    }

    @Test
    void getMealsForUserLoadsMealAndFoods() {
        final Meal meal =
                new Meal(
                        username,
                        LocalDate.now(),
                        "Lunch"
                );

        final int mealId =
                dao.saveMeal(meal);

        final FoodEntry food =
                makeFood(
                        "Chicken",
                        250.0,
                        40.0,
                        0.0,
                        8.0
                );

        food.setMealId(mealId);

        dao.saveFoodEntry(food);

        final List<Meal> meals =
                dao.getMealsForUser(username);

        assertEquals(1, meals.size());

        final Meal loaded =
                meals.get(0);

        assertEquals(
                mealId,
                loaded.getId()
        );

        assertEquals(
                username,
                loaded.getUserId()
        );

        assertEquals(
                "Lunch",
                loaded.getName()
        );

        assertEquals(
                LocalDate.now(),
                loaded.getDate()
        );

        assertEquals(
                1,
                loaded.getFoodEntries().size()
        );

        assertEquals(
                "Chicken",
                loaded.getFoodEntries()
                        .get(0)
                        .getFoodName()
        );
    }

    @Test
    void getMealByIdReturnsMealAndFoods() {
        final Meal meal =
                new Meal(
                        username,
                        LocalDate.now(),
                        "Dinner"
                );

        final int mealId =
                dao.saveMeal(meal);

        final FoodEntry food =
                makeFood(
                        "Rice",
                        200.0,
                        4.0,
                        45.0,
                        1.0
                );

        food.setMealId(mealId);

        dao.saveFoodEntry(food);

        final Meal loaded =
                dao.getMealById(mealId);

        assertNotNull(loaded);

        assertEquals(
                mealId,
                loaded.getId()
        );

        assertEquals(
                username,
                loaded.getUserId()
        );

        assertEquals(
                "Dinner",
                loaded.getName()
        );

        assertEquals(
                1,
                loaded.getFoodEntries().size()
        );

        assertEquals(
                "Rice",
                loaded.getFoodEntries()
                        .get(0)
                        .getFoodName()
        );
    }

    @Test
    void getMealByIdThrowsWhenMealDoesNotExist() {
        assertThrows(
                DataAccessException.class,
                () -> dao.getMealById(-999999)
        );
    }

    @Test
    void editMealUpdatesNameAndDate() {
        final Meal meal =
                new Meal(
                        username,
                        LocalDate.now(),
                        "Breakfast"
                );

        final int mealId =
                dao.saveMeal(meal);

        final LocalDate newDate =
                LocalDate.now().minusDays(1);

        final Meal editedMeal =
                new Meal(
                        username,
                        newDate,
                        "Brunch"
                );

        editedMeal.setId(mealId);
        editedMeal.setFoodEntries(List.of());

        final Meal result =
                dao.editMeal(
                        editedMeal,
                        List.of()
                );

        assertEquals(
                mealId,
                result.getId()
        );

        final Meal loaded =
                dao.getMealById(mealId);

        assertEquals(
                "Brunch",
                loaded.getName()
        );

        assertEquals(
                newDate,
                loaded.getDate()
        );
    }

    @Test
    void editMealAddsNewFoodEntry() {
        final Meal meal =
                new Meal(
                        username,
                        LocalDate.now(),
                        "Dinner"
                );

        final int mealId =
                dao.saveMeal(meal);

        final FoodEntry newFood =
                makeFood(
                        "Potatoes",
                        180.0,
                        5.0,
                        40.0,
                        0.2
                );

        /*
         * Leave mealId and id null.
         *
         * editMeal should assign the meal ID
         * and insert this as a new food.
         */
        final Meal editedMeal =
                new Meal(
                        username,
                        LocalDate.now(),
                        "Dinner"
                );

        editedMeal.setId(mealId);

        editedMeal.setFoodEntries(
                List.of(newFood)
        );

        dao.editMeal(
                editedMeal,
                List.of()
        );

        assertNotNull(
                newFood.getMealId()
        );

        assertNotNull(
                newFood.getId()
        );

        assertEquals(
                mealId,
                newFood.getMealId()
        );

        final List<FoodEntry> foods =
                dao.getFoodEntriesForMeal(mealId);

        assertEquals(
                1,
                foods.size()
        );

        assertEquals(
                "Potatoes",
                foods.get(0).getFoodName()
        );
    }

    @Test
    void editMealUpdatesExistingFoodEntry() {
        final Meal meal =
                new Meal(
                        username,
                        LocalDate.now(),
                        "Lunch"
                );

        final int mealId =
                dao.saveMeal(meal);

        final FoodEntry original =
                makeFood(
                        "Chicken",
                        250.0,
                        40.0,
                        0.0,
                        8.0
                );

        original.setMealId(mealId);

        final int foodId =
                dao.saveFoodEntry(original);

        final FoodEntry editedFood =
                makeFood(
                        "Chicken Breast",
                        300.0,
                        55.0,
                        2.0,
                        6.0
                );

        editedFood.setId(foodId);
        editedFood.setMealId(mealId);

        final Meal editedMeal =
                new Meal(
                        username,
                        LocalDate.now(),
                        "Lunch"
                );

        editedMeal.setId(mealId);

        editedMeal.setFoodEntries(
                List.of(editedFood)
        );

        dao.editMeal(
                editedMeal,
                List.of()
        );

        final List<FoodEntry> foods =
                dao.getFoodEntriesForMeal(mealId);

        assertEquals(
                1,
                foods.size()
        );

        final FoodEntry loaded =
                foods.get(0);

        assertEquals(
                foodId,
                loaded.getId()
        );

        assertEquals(
                "Chicken Breast",
                loaded.getFoodName()
        );

        assertEquals(
                300.0,
                loaded.getNutrition().getCalories(),
                0.001
        );

        assertEquals(
                55.0,
                loaded.getNutrition().getProtein(),
                0.001
        );

        assertEquals(
                2.0,
                loaded.getNutrition().getCarbs(),
                0.001
        );

        assertEquals(
                6.0,
                loaded.getNutrition().getFat(),
                0.001
        );
    }

    @Test
    void editMealDeletesRemovedFoodEntry() {
        final Meal meal =
                new Meal(
                        username,
                        LocalDate.now(),
                        "Snack"
                );

        final int mealId =
                dao.saveMeal(meal);

        final FoodEntry apple =
                makeFood(
                        "Apple",
                        95.0,
                        0.5,
                        25.0,
                        0.3
                );

        apple.setMealId(mealId);

        final int appleId =
                dao.saveFoodEntry(apple);

        final FoodEntry banana =
                makeFood(
                        "Banana",
                        105.0,
                        1.3,
                        27.0,
                        0.4
                );

        banana.setMealId(mealId);

        dao.saveFoodEntry(banana);

        final Meal editedMeal =
                new Meal(
                        username,
                        LocalDate.now(),
                        "Snack"
                );

        editedMeal.setId(mealId);

        editedMeal.setFoodEntries(
                List.of(banana)
        );

        final List<Integer> idsToDelete =
                new ArrayList<>();

        idsToDelete.add(appleId);

        /*
         * Cover the false side of:
         *
         * foodEntryId != null
         * && foodEntryId > 0
         */
        idsToDelete.add(null);
        idsToDelete.add(0);
        idsToDelete.add(-1);

        dao.editMeal(
                editedMeal,
                idsToDelete
        );

        final List<FoodEntry> remaining =
                dao.getFoodEntriesForMeal(mealId);

        assertEquals(
                1,
                remaining.size()
        );

        assertEquals(
                "Banana",
                remaining.get(0).getFoodName()
        );
    }

    @Test
    void deleteMealDeletesMealAndFoodEntries() {
        final Meal meal =
                new Meal(
                        username,
                        LocalDate.now(),
                        "Dinner"
                );

        final int mealId =
                dao.saveMeal(meal);

        final FoodEntry food =
                makeFood(
                        "Rice",
                        200.0,
                        4.0,
                        45.0,
                        1.0
                );

        food.setMealId(mealId);

        dao.saveFoodEntry(food);

        dao.deleteMeal(mealId);

        final List<Meal> meals =
                dao.getMealsForUser(username);

        assertTrue(
                meals.isEmpty()
        );

        final List<FoodEntry> foods =
                dao.getFoodEntriesForMeal(mealId);

        assertTrue(
                foods.isEmpty()
        );
    }

    @Test
    void caloriesByDateAggregatesFoodsByDate() {
        final LocalDate firstDate =
                LocalDate.now().minusDays(2);

        final LocalDate secondDate =
                LocalDate.now().minusDays(1);

        final Meal firstMeal =
                new Meal(
                        username,
                        firstDate,
                        "First Meal"
                );

        final int firstMealId =
                dao.saveMeal(firstMeal);

        final FoodEntry first =
                makeFood(
                        "Food A",
                        200.0,
                        10.0,
                        20.0,
                        5.0
                );

        first.setMealId(firstMealId);

        dao.saveFoodEntry(first);

        final FoodEntry second =
                makeFood(
                        "Food B",
                        350.0,
                        20.0,
                        30.0,
                        10.0
                );

        second.setMealId(firstMealId);

        dao.saveFoodEntry(second);

        final Meal secondMeal =
                new Meal(
                        username,
                        secondDate,
                        "Second Meal"
                );

        final int secondMealId =
                dao.saveMeal(secondMeal);

        final FoodEntry third =
                makeFood(
                        "Food C",
                        100.0,
                        5.0,
                        15.0,
                        2.0
                );

        third.setMealId(secondMealId);

        dao.saveFoodEntry(third);

        final Map<LocalDate, Double> calories =
                dao.getCaloriesByDate(username);

        assertEquals(
                2,
                calories.size()
        );

        assertEquals(
                550.0,
                calories.get(firstDate),
                0.001
        );

        assertEquals(
                100.0,
                calories.get(secondDate),
                0.001
        );
    }

    @Test
    void caloriesByDateReturnsEmptyMapForUserWithNoMeals() {
        final Map<LocalDate, Double> calories =
                dao.getCaloriesByDate(username);

        assertTrue(
                calories.isEmpty()
        );
    }

    @Test
    void macrosForTodayReturnsMacroData() {
        final Meal meal =
                new Meal(
                        username,
                        LocalDate.now(),
                        "Today"
                );

        final int mealId =
                dao.saveMeal(meal);

        final FoodEntry first =
                makeFood(
                        "Chicken",
                        300.0,
                        40.0,
                        5.0,
                        8.0
                );

        first.setMealId(mealId);

        dao.saveFoodEntry(first);

        final FoodEntry second =
                makeFood(
                        "Rice",
                        250.0,
                        5.0,
                        50.0,
                        2.0
                );

        second.setMealId(mealId);

        dao.saveFoodEntry(second);

        final MacroData macros =
                dao.getMacrosForToday(username);

        assertNotNull(macros);
    }

    @Test
    void macrosForTodayWorksWhenNoFoodExists() {
        final MacroData macros =
                dao.getMacrosForToday(username);

        assertNotNull(macros);
    }

    private FoodEntry makeFood(
            final String name,
            final double calories,
            final double protein,
            final double carbs,
            final double fat
    ) {

        final FoodNutrition nutrition =
                new FoodNutrition(
                        calories,
                        protein,
                        carbs,
                        fat
                );

        return new FoodEntry(
                name,
                nutrition,
                1.0,
                FoodUnit.values()[0],
                100.0
        );
    }
}