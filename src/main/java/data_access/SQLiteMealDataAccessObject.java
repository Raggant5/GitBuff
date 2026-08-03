package data_access;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import entity.FoodEntry;
import entity.FoodNutrition;
import entity.FoodUnit;
import entity.Meal;
import use_case.nutrition.food.delete_food.DeleteFoodDataAccessInterface;
import use_case.nutrition.food.edit_food.EditFoodDataAccessInterface;
import use_case.nutrition.meal.add_meal.AddMealDataAccessInterface;
import use_case.nutrition.meal.delete_meal.DeleteMealDataAccessInterface;
import use_case.nutrition.meal.edit_meal.EditMealDataAccessInterface;
import use_case.nutrition.meal.get_meals.ViewMealDataAccessInterface;

/**
 * SQLite implementation for storing meals and food entries.
 */
public class SQLiteMealDataAccessObject implements
        AddMealDataAccessInterface,
        ViewMealDataAccessInterface,
        EditMealDataAccessInterface,
        EditFoodDataAccessInterface,
        DeleteMealDataAccessInterface,
        DeleteFoodDataAccessInterface {

    @Override
    public int saveMeal(Meal meal) {
        final String sql = """
                INSERT INTO meals (
                    user_id,
                    meal_name,
                    meal_date
                )
                VALUES (?, ?, ?)
                """;

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(
                     sql,
                     Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, meal.getUserId());
            stmt.setString(2, meal.getName());
            stmt.setString(3, meal.getDate().toString());

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    final int id = keys.getInt(1);
                    meal.setId(id);
                    return id;
                }
            }
        }
        catch (SQLException exc) {
            throw new RuntimeException("Failed to save meal.", exc);
        }

        throw new IllegalStateException("Meal ID was not generated.");
    }


    @Override
    public int saveFoodEntry(final FoodEntry foodEntry) {
        final String sql = """
            INSERT INTO food_entries (
                meal_id,
                food_name,
                quantity,
                unit,
                grams,
                calories,
                protein,
                carbohydrates,
                fat
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(
                     sql,
                     Statement.RETURN_GENERATED_KEYS)) {

            final FoodNutrition nutrition = foodEntry.getNutrition();

            stmt.setInt(1, foodEntry.getMealId());
            stmt.setString(2, foodEntry.getFoodName());
            stmt.setDouble(3, foodEntry.getQuantity());
            stmt.setString(4, foodEntry.getUnit().name());
            stmt.setDouble(5, foodEntry.getGrams());
            stmt.setDouble(6, nutrition.getCalories());
            stmt.setDouble(7, nutrition.getProtein());
            stmt.setDouble(8, nutrition.getCarbs());
            stmt.setDouble(9, nutrition.getFat());

            final int rowsInserted = stmt.executeUpdate();

            System.out.println(
                    "Inserted " + rowsInserted
                            + " food row(s): "
                            + foodEntry.getFoodName()
                            + ", mealId=" + foodEntry.getMealId()
            );

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    final int id = keys.getInt(1);

                    System.out.println("Generated food ID: " + id);

                    foodEntry.setId(id);
                    return id;
                }
            }
        }
        catch (SQLException exc) {
            exc.printStackTrace();
            throw new RuntimeException("Failed to save food entry.", exc);
        }

        throw new IllegalStateException(
                "Food-entry ID was not generated."
        );
    }

    @Override
    public List<FoodEntry> getFoodEntriesForMeal(int mealId) {
        final List<FoodEntry> entries = new ArrayList<>();

        final String sql = """
                SELECT
                    id,
                    meal_id,
                    food_name,
                    quantity,
                    unit,
                    grams,
                    calories,
                    protein,
                    carbohydrates,
                    fat
                FROM food_entries
                WHERE meal_id = ?
                ORDER BY id
                """;

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, mealId);

            try (ResultSet results = stmt.executeQuery()) {
                while (results.next()) {
                    final FoodNutrition nutrition = new FoodNutrition(
                            results.getDouble("calories"),
                            results.getDouble("protein"),
                            results.getDouble("carbohydrates"),
                            results.getDouble("fat")
                    );

                    final FoodUnit unit = FoodUnit.valueOf(
                            results.getString("unit")
                    );

                    final FoodEntry foodEntry = new FoodEntry(
                            results.getString("food_name"),
                            nutrition,
                            results.getDouble("quantity"),
                            unit,
                            results.getDouble("grams")
                    );

                    foodEntry.setId(results.getInt("id"));
                    foodEntry.setMealId(results.getInt("meal_id"));

                    entries.add(foodEntry);
                }
            }
        }
        catch (SQLException exc) {
            throw new RuntimeException(
                    "Failed to load food entries.",
                    exc
            );
        }

        return entries;
    }

    @Override
    public List<Meal> getMealsForUser(String userId) {
        final List<Meal> meals = new ArrayList<>();

        final String sql = """
                SELECT
                    id,
                    user_id,
                    meal_name,
                    meal_date
                FROM meals
                WHERE user_id = ?
                ORDER BY meal_date DESC, id DESC
                """;

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userId);

            try (ResultSet results = stmt.executeQuery()) {
                while (results.next()) {
                    final Meal meal = new Meal(
                            results.getString("user_id"),
                            LocalDate.parse(
                                    results.getString("meal_date")
                            ),
                            results.getString("meal_name")
                    );

                    meal.setId(results.getInt("id"));
                    meal.setFoodEntries(
                            getFoodEntriesForMeal(meal.getId())
                    );

                    meals.add(meal);
                }
            }
        }
        catch (SQLException exc) {
            throw new RuntimeException(
                    "Failed to load meals.",
                    exc
            );
        }

        return meals;
    }

    @Override
    public void editMeal(Meal meal) {
        final String sql = """
                UPDATE meals
                SET meal_name = ?,
                    meal_date = ?
                WHERE id = ?
                """;

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, meal.getName());
            stmt.setString(2, meal.getDate().toString());
            stmt.setInt(3, meal.getId());

            stmt.executeUpdate();
        }
        catch (SQLException exc) {
            throw new RuntimeException("Failed to edit meal.", exc);
        }
    }

    @Override
    public void editFoodEntry(FoodEntry foodEntry) {
        final String sql = """
                UPDATE food_entries
                SET food_name = ?,
                    quantity = ?,
                    unit = ?,
                    grams = ?,
                    calories = ?,
                    protein = ?,
                    carbohydrates = ?,
                    fat = ?
                WHERE id = ?
                """;

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            final FoodNutrition nutrition = foodEntry.getNutrition();

            stmt.setString(1, foodEntry.getFoodName());
            stmt.setDouble(2, foodEntry.getQuantity());
            stmt.setString(3, foodEntry.getUnit().name());
            stmt.setDouble(4, foodEntry.getGrams());
            stmt.setDouble(5, nutrition.getCalories());
            stmt.setDouble(6, nutrition.getProtein());
            stmt.setDouble(7, nutrition.getCarbs());
            stmt.setDouble(8, nutrition.getFat());
            stmt.setInt(9, foodEntry.getId());

            stmt.executeUpdate();
        }
        catch (SQLException exc) {
            throw new RuntimeException(
                    "Failed to edit food entry.",
                    exc
            );
        }
    }

    @Override
    public void deleteMeal(int mealId) {
        final String deleteFoodEntriesSql = """
                DELETE FROM food_entries
                WHERE meal_id = ?
                """;

        final String deleteMealSql = """
                DELETE FROM meals
                WHERE id = ?
                """;

        try (Connection conn = Database.connect()) {
            conn.setAutoCommit(false);

            try (PreparedStatement foodStmt =
                         conn.prepareStatement(deleteFoodEntriesSql);
                 PreparedStatement mealStmt =
                         conn.prepareStatement(deleteMealSql)) {

                foodStmt.setInt(1, mealId);
                foodStmt.executeUpdate();

                mealStmt.setInt(1, mealId);
                mealStmt.executeUpdate();

                conn.commit();
            }
            catch (SQLException exc) {
                conn.rollback();
                throw exc;
            }
            finally {
                conn.setAutoCommit(true);
            }
        }
        catch (SQLException exc) {
            throw new RuntimeException("Failed to delete meal.", exc);
        }
    }

    @Override
    public void deleteFoodEntry(int foodEntryId) {
        final String sql = """
                DELETE FROM food_entries
                WHERE id = ?
                """;

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, foodEntryId);
            stmt.executeUpdate();
        }
        catch (SQLException exc) {
            throw new RuntimeException(
                    "Failed to delete food entry.",
                    exc
            );
        }
    }
}