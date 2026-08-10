package data_access;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

class DatainitializerTest {

    private static final String[] EXPECTED_TABLES = {
            "users", "user_equipment", "user_dietary_restrictions", "user_workout_days",
            "user_privacy_settings", "meals", "food_entries", "logged_workouts", "exercises_performed",
    };

    @Test
    void initializeCreatesAllExpectedTablesAndIsIdempotent() throws SQLException {
        assertDoesNotThrow(Datainitializer::initialize);
        assertDoesNotThrow(Datainitializer::initialize);

        final Set<String> tableNames = new HashSet<>();
        try (Connection connection = Database.connect();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(
                     "SELECT name FROM sqlite_master WHERE type='table'")) {
            while (resultSet.next()) {
                tableNames.add(resultSet.getString("name"));
            }
        }

        for (final String expectedTable : EXPECTED_TABLES) {
            assertTrue(tableNames.contains(expectedTable), "Expected table " + expectedTable + " to exist.");
        }
    }
}
