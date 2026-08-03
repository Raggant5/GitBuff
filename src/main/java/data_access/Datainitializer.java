package data_access;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Creates the application's SQLite database tables.
 */
public final class Datainitializer {

    private Datainitializer() {
        // Utility class.
    }

    /**
     * Creates every required database table if it does not already exist.
     *
     * @throws SQLException if database initialization fails
     */
    public static void initialize() throws SQLException {
        try (Connection connection = Database.connect();
             Statement statement = connection.createStatement()) {

            statement.execute("""
                    CREATE TABLE IF NOT EXISTS users (
                        username TEXT PRIMARY KEY,
                        password TEXT NOT NULL,
                        height REAL NOT NULL DEFAULT 0,
                        weight REAL NOT NULL DEFAULT 0,
                        activity_level TEXT NOT NULL
                            DEFAULT 'MODERATELY_ACTIVE',
                        fitness_goal TEXT NOT NULL
                            DEFAULT 'MAINTAIN_GENERAL_FITNESS',
                        profile_picture_path TEXT,
                        date_of_birth TEXT,
                        gender TEXT NOT NULL
                            DEFAULT 'PREFER_NOT_TO_SAY',
                        bio TEXT NOT NULL DEFAULT '',
                        preferred_unit_system TEXT NOT NULL
                            DEFAULT 'METRIC',
                        preferred_workout_duration_minutes INTEGER
                            NOT NULL DEFAULT 45
                    );
                    """);

            statement.execute("""
                    CREATE TABLE IF NOT EXISTS user_equipment (
                        username TEXT NOT NULL,
                        equipment TEXT NOT NULL,
                        PRIMARY KEY (username, equipment),
                        FOREIGN KEY (username)
                            REFERENCES users(username)
                            ON DELETE CASCADE
                    );
                    """);

            statement.execute("""
                    CREATE TABLE IF NOT EXISTS user_dietary_restrictions (
                        username TEXT NOT NULL,
                        dietary_restriction TEXT NOT NULL,
                        PRIMARY KEY (
                            username,
                            dietary_restriction
                        ),
                        FOREIGN KEY (username)
                            REFERENCES users(username)
                            ON DELETE CASCADE
                    );
                    """);

            statement.execute("""
                    CREATE TABLE IF NOT EXISTS user_workout_days (
                        username TEXT NOT NULL,
                        workout_day TEXT NOT NULL,
                        PRIMARY KEY (username, workout_day),
                        FOREIGN KEY (username)
                            REFERENCES users(username)
                            ON DELETE CASCADE
                    );
                    """);

            statement.execute("""
                    CREATE TABLE IF NOT EXISTS user_privacy_settings (
                        username TEXT NOT NULL,
                        privacy_setting TEXT NOT NULL,
                        PRIMARY KEY (
                            username,
                            privacy_setting
                        ),
                        FOREIGN KEY (username)
                            REFERENCES users(username)
                            ON DELETE CASCADE
                    );
                    """);

            statement.execute("""
                    CREATE TABLE IF NOT EXISTS meals (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        user_id TEXT NOT NULL,
                        meal_name TEXT NOT NULL,
                        meal_date TEXT NOT NULL,
                        FOREIGN KEY (user_id)
                            REFERENCES users(username)
                            ON DELETE CASCADE
                    );
                    """);

            statement.execute("""
                    CREATE TABLE IF NOT EXISTS food_entries (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        meal_id INTEGER NOT NULL,
                        food_name TEXT NOT NULL,
                        quantity REAL NOT NULL DEFAULT 0,
                        unit TEXT NOT NULL,
                        grams REAL NOT NULL DEFAULT 0,
                        calories REAL NOT NULL DEFAULT 0,
                        protein REAL NOT NULL DEFAULT 0,
                        carbohydrates REAL NOT NULL DEFAULT 0,
                        fat REAL NOT NULL DEFAULT 0,
                        FOREIGN KEY (meal_id)
                            REFERENCES meals(id)
                            ON DELETE CASCADE
                    );
                    """);

            System.out.println("Database initialized.");
        }
    }
}

