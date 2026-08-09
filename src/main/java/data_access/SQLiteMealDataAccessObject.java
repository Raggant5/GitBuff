package data_access;
import use_case.dashboard.MacroData;
import use_case.DataAccessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import use_case.dashboard.DashboardDataAccessInterface;
import entity.FoodEntry;
import entity.FoodNutrition;
import entity.FoodUnit;
import entity.Meal;
import use_case.nutrition.meal.add_meal.AddMealDataAccessInterface;
import use_case.nutrition.meal.delete_meal.DeleteMealDataAccessInterface;
import use_case.nutrition.meal.edit_meal.EditMealDataAccessInterface;
import use_case.nutrition.meal.get_meals.ViewMealDataAccessInterface;

/**
 * SQLite implementation for storing meals and food entries.
 */
public final class SQLiteMealDataAccessObject implements
        AddMealDataAccessInterface,
        ViewMealDataAccessInterface,
        EditMealDataAccessInterface,
        DeleteMealDataAccessInterface,
        DashboardDataAccessInterface {

    private static final int PARAMETER_ONE = 1;
    private static final int PARAMETER_TWO = 2;
    private static final int PARAMETER_THREE = 3;
    private static final int PARAMETER_FOUR = 4;
    private static final int PARAMETER_FIVE = 5;
    private static final int PARAMETER_SIX = 6;
    private static final int PARAMETER_SEVEN = 7;
    private static final int PARAMETER_EIGHT = 8;
    private static final int PARAMETER_NINE = 9;

    @Override
    public int saveMeal(final Meal meal) {
        final String sql = """
                INSERT INTO meals (
                    user_id,
                    meal_name,
                    meal_date
                )
                VALUES (?, ?, ?)
                """;

        try (Connection connection = Database.connect();
             PreparedStatement statement =
                     connection.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS
                     )) {

            statement.setString(
                    PARAMETER_ONE,
                    meal.getUserId()
            );
            statement.setString(
                    PARAMETER_TWO,
                    meal.getName()
            );
            statement.setString(
                    PARAMETER_THREE,
                    meal.getDate().toString()
            );

            statement.executeUpdate();

            try (ResultSet generatedKeys =
                         statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    final int mealId =
                            generatedKeys.getInt(PARAMETER_ONE);

                    meal.setId(mealId);
                    return mealId;
                }
            }
        }
        catch (final SQLException exception) {
            throw new DataAccessException(
                    "Failed to save meal.",
                    exception
            );
        }

        throw new IllegalStateException(
                "Meal ID was not generated."
        );
    }

    @Override
    public int saveFoodEntry(final FoodEntry foodEntry) {
        try (Connection connection = Database.connect()) {
            return insertFoodEntryRow(connection, foodEntry);
        }
        catch (final SQLException exception) {
            throw new DataAccessException(
                    "Failed to save food entry.",
                    exception
            );
        }
    }

    /**
     * Inserts a food-entry row using the given connection.
     *
     * @param connection shared database connection
     * @param foodEntry food entry to insert
     * @return generated food-entry ID
     * @throws SQLException if the insert fails
     */
    private static int insertFoodEntryRow(
            final Connection connection,
            final FoodEntry foodEntry
    ) throws SQLException {

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

        try (PreparedStatement statement =
                     connection.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS
                     )) {

            final FoodNutrition nutrition =
                    foodEntry.getNutrition();

            statement.setInt(
                    PARAMETER_ONE,
                    foodEntry.getMealId()
            );
            statement.setString(
                    PARAMETER_TWO,
                    foodEntry.getFoodName()
            );
            statement.setDouble(
                    PARAMETER_THREE,
                    foodEntry.getQuantity()
            );
            statement.setString(
                    PARAMETER_FOUR,
                    foodEntry.getUnit().name()
            );
            statement.setDouble(
                    PARAMETER_FIVE,
                    foodEntry.getGrams()
            );
            statement.setDouble(
                    PARAMETER_SIX,
                    nutrition.getCalories()
            );
            statement.setDouble(
                    PARAMETER_SEVEN,
                    nutrition.getProtein()
            );
            statement.setDouble(
                    PARAMETER_EIGHT,
                    nutrition.getCarbs()
            );
            statement.setDouble(
                    PARAMETER_NINE,
                    nutrition.getFat()
            );

            statement.executeUpdate();

            try (ResultSet generatedKeys =
                         statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    final int foodEntryId =
                            generatedKeys.getInt(PARAMETER_ONE);

                    foodEntry.setId(foodEntryId);
                    return foodEntryId;
                }
            }
        }

        throw new IllegalStateException(
                "Food-entry ID was not generated."
        );
    }

    @Override
    public List<FoodEntry> getFoodEntriesForMeal(
            final int mealId
    ) {
        final List<FoodEntry> foodEntries =
                new ArrayList<>();

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

        try (Connection connection = Database.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(PARAMETER_ONE, mealId);

            try (ResultSet results =
                         statement.executeQuery()) {
                while (results.next()) {
                    final FoodNutrition nutrition =
                            new FoodNutrition(
                                    results.getDouble(
                                            "calories"
                                    ),
                                    results.getDouble(
                                            "protein"
                                    ),
                                    results.getDouble(
                                            "carbohydrates"
                                    ),
                                    results.getDouble(
                                            "fat"
                                    )
                            );

                    final FoodUnit unit =
                            FoodUnit.valueOf(
                                    results.getString("unit")
                            );

                    final FoodEntry foodEntry =
                            new FoodEntry(
                                    results.getString(
                                            "food_name"
                                    ),
                                    nutrition,
                                    results.getDouble(
                                            "quantity"
                                    ),
                                    unit,
                                    results.getDouble(
                                            "grams"
                                    )
                            );

                    foodEntry.setId(
                            results.getInt("id")
                    );
                    foodEntry.setMealId(
                            results.getInt("meal_id")
                    );

                    foodEntries.add(foodEntry);
                }
            }
        }
        catch (final SQLException exception) {
            throw new DataAccessException(
                    "Failed to load food entries.",
                    exception
            );
        }

        return foodEntries;
    }

    @Override
    public List<Meal> getMealsForUser(
            final String userId
    ) {
        final List<Meal> meals =
                new ArrayList<>();

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

        try (Connection connection = Database.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(PARAMETER_ONE, userId);

            try (ResultSet results =
                         statement.executeQuery()) {
                while (results.next()) {
                    final LocalDate mealDate =
                            LocalDate.parse(
                                    results.getString(
                                            "meal_date"
                                    )
                            );

                    final Meal meal =
                            new Meal(
                                    results.getString(
                                            "user_id"
                                    ),
                                    mealDate,
                                    results.getString(
                                            "meal_name"
                                    )
                            );

                    meal.setId(
                            results.getInt("id")
                    );
                    meal.setFoodEntries(
                            getFoodEntriesForMeal(
                                    meal.getId()
                            )
                    );

                    meals.add(meal);
                }
            }
        }
        catch (final SQLException exception) {
            throw new DataAccessException(
                    "Failed to load meals.",
                    exception
            );
        }

        return meals;
    }

    @Override
    public Meal getMealById(final int mealId) {
        final String sql = """
                SELECT
                    id,
                    user_id,
                    meal_name,
                    meal_date
                FROM meals
                WHERE id = ?
                """;

        try (Connection connection = Database.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(PARAMETER_ONE, mealId);

            try (ResultSet results =
                         statement.executeQuery()) {
                if (results.next()) {
                    final LocalDate mealDate =
                            LocalDate.parse(
                                    results.getString(
                                            "meal_date"
                                    )
                            );

                    final Meal meal =
                            new Meal(
                                    results.getString(
                                            "user_id"
                                    ),
                                    mealDate,
                                    results.getString(
                                            "meal_name"
                                    )
                            );

                    meal.setId(
                            results.getInt("id")
                    );
                    meal.setFoodEntries(
                            getFoodEntriesForMeal(
                                    meal.getId()
                            )
                    );

                    return meal;
                }
            }
        }
        catch (final SQLException exception) {
            throw new DataAccessException(
                    "Failed to load meal.",
                    exception
            );
        }

        throw new DataAccessException(
                "Meal not found: " + mealId
        );
    }

    /**
     * Updates an existing meal: applies the new name/date, deletes any removed food
     * entries, and inserts/updates the remaining food entries, all as a single atomic
     * transaction on one connection.
     *
     * @param meal the updated meal
     * @param foodEntryIdsToDelete ids of food entries to remove from the meal
     * @return the persisted meal, with generated ids populated on any newly-inserted food entries
     */
    @Override
    public Meal editMeal(final Meal meal, final List<Integer> foodEntryIdsToDelete) {
        final String sql = """
                UPDATE meals
                SET meal_name = ?,
                    meal_date = ?
                WHERE id = ?
                """;

        try (Connection connection = Database.connect()) {

            connection.setAutoCommit(false);

            try {
                for (Integer foodEntryId : foodEntryIdsToDelete) {
                    if (foodEntryId != null && foodEntryId > 0) {
                        deleteFoodEntryRow(connection, foodEntryId);
                    }
                }

                try (PreparedStatement statement =
                             connection.prepareStatement(sql)) {

                    statement.setString(
                            PARAMETER_ONE,
                            meal.getName()
                    );
                    statement.setString(
                            PARAMETER_TWO,
                            meal.getDate().toString()
                    );
                    statement.setInt(
                            PARAMETER_THREE,
                            meal.getId()
                    );

                    statement.executeUpdate();
                }

                for (FoodEntry food : meal.getFoodEntries()) {
                    if (food.getMealId() == null) {
                        food.setMealId(meal.getId());
                    }

                    if (food.getId() == null) {
                        insertFoodEntryRow(connection, food);
                    }
                    else {
                        updateFoodEntryRow(connection, food);
                    }
                }

                connection.commit();
            }
            catch (final SQLException exception) {
                connection.rollback();
                throw exception;
            }
            finally {
                connection.setAutoCommit(true);
            }
        }
        catch (final SQLException exception) {
            throw new DataAccessException(
                    "Failed to edit meal.",
                    exception
            );
        }

        return meal;
    }

    /**
     * Updates a food-entry row using the given connection, without committing.
     *
     * @param connection shared database connection
     * @param foodEntry food entry to update
     * @throws SQLException if the update fails
     */
    private static void updateFoodEntryRow(
            final Connection connection,
            final FoodEntry foodEntry
    ) throws SQLException {

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

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            final FoodNutrition nutrition =
                    foodEntry.getNutrition();

            statement.setString(
                    PARAMETER_ONE,
                    foodEntry.getFoodName()
            );
            statement.setDouble(
                    PARAMETER_TWO,
                    foodEntry.getQuantity()
            );
            statement.setString(
                    PARAMETER_THREE,
                    foodEntry.getUnit().name()
            );
            statement.setDouble(
                    PARAMETER_FOUR,
                    foodEntry.getGrams()
            );
            statement.setDouble(
                    PARAMETER_FIVE,
                    nutrition.getCalories()
            );
            statement.setDouble(
                    PARAMETER_SIX,
                    nutrition.getProtein()
            );
            statement.setDouble(
                    PARAMETER_SEVEN,
                    nutrition.getCarbs()
            );
            statement.setDouble(
                    PARAMETER_EIGHT,
                    nutrition.getFat()
            );
            statement.setInt(
                    PARAMETER_NINE,
                    foodEntry.getId()
            );

            statement.executeUpdate();
        }
    }

    @Override
    public void deleteMeal(final int mealId) {
        final String deleteFoodEntriesSql = """
                DELETE FROM food_entries
                WHERE meal_id = ?
                """;

        final String deleteMealSql = """
                DELETE FROM meals
                WHERE id = ?
                """;

        try (Connection connection = Database.connect()) {
            connection.setAutoCommit(false);

            try (PreparedStatement foodStatement =
                         connection.prepareStatement(
                                 deleteFoodEntriesSql
                         );
                 PreparedStatement mealStatement =
                         connection.prepareStatement(
                                 deleteMealSql
                         )) {

                foodStatement.setInt(
                        PARAMETER_ONE,
                        mealId
                );
                foodStatement.executeUpdate();

                mealStatement.setInt(
                        PARAMETER_ONE,
                        mealId
                );
                mealStatement.executeUpdate();

                connection.commit();
            }
            catch (final SQLException exception) {
                connection.rollback();
                throw exception;
            }
            finally {
                connection.setAutoCommit(true);
            }
        }
        catch (final SQLException exception) {
            throw new DataAccessException(
                    "Failed to delete meal.",
                    exception
            );
        }
    }

    /**
     * Deletes a food-entry row using the given connection, without committing.
     *
     * @param connection shared database connection
     * @param foodEntryId food entry ID
     * @throws SQLException if the delete fails
     */
    private static void deleteFoodEntryRow(
            final Connection connection,
            final int foodEntryId
    ) throws SQLException {

        final String sql = """
                DELETE FROM food_entries
                WHERE id = ?
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    PARAMETER_ONE,
                    foodEntryId
            );
            statement.executeUpdate();
        }
    }

    @Override
    public Map<LocalDate, Double> getCaloriesByDate(
            final String userId
    ) {
        final Map<LocalDate, Double> caloriesByDate =
                new LinkedHashMap<>();

        final String sql = """
                SELECT
                    meals.meal_date,
                    SUM(food_entries.calories) AS total_calories
                FROM meals
                JOIN food_entries
                    ON meals.id = food_entries.meal_id
                WHERE meals.user_id = ?
                GROUP BY meals.meal_date
                ORDER BY meals.meal_date
                """;

        try (Connection connection = Database.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(PARAMETER_ONE, userId);

            try (ResultSet results = statement.executeQuery()) {
                while (results.next()) {
                    final LocalDate date = LocalDate.parse(
                            results.getString("meal_date")
                    );

                    final double calories = results.getDouble(
                            "total_calories"
                    );

                    caloriesByDate.put(date, calories);
                }
            }
        }
        catch (final SQLException exception) {
            throw new DataAccessException(
                    "Failed to load calories by date.",
                    exception
            );
        }

        return caloriesByDate;
    }

    @Override
    public MacroData getMacrosForToday(final String userId) {
        final String sql = """
            SELECT
                COALESCE(SUM(food_entries.protein), 0) AS total_protein,
                COALESCE(SUM(food_entries.carbohydrates), 0) AS total_carbs,
                COALESCE(SUM(food_entries.fat), 0) AS total_fat
            FROM meals
            JOIN food_entries
                ON meals.id = food_entries.meal_id
            WHERE meals.user_id = ?
              AND meals.meal_date = ?
            """;

        try (Connection connection = Database.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(PARAMETER_ONE, userId);
            statement.setString(
                    PARAMETER_TWO,
                    LocalDate.now().toString()
            );

            try (ResultSet results = statement.executeQuery()) {
                if (results.next()) {
                    return new MacroData(
                            results.getDouble("total_protein"),
                            results.getDouble("total_carbs"),
                            results.getDouble("total_fat")
                    );
                }
            }
        }
        catch (final SQLException exception) {
            throw new DataAccessException(
                    "Failed to load today's macronutrients.",
                    exception
            );
        }

        return new MacroData(0, 0, 0);
    }
}
