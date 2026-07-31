package data_access;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Initializes database schemas upon application startup.
 */
public final class Datainitializer {

    private static final String CREATE_USERS_TABLE = """
            CREATE TABLE IF NOT EXISTS users (
                username TEXT PRIMARY KEY,
                password TEXT NOT NULL,
                height REAL NOT NULL DEFAULT 0,
                weight REAL NOT NULL DEFAULT 0,
                activity_level TEXT NOT NULL DEFAULT 'MODERATELY_ACTIVE',
                fitness_goal TEXT NOT NULL DEFAULT 'MAINTAIN',
                profile_picture_path TEXT
            );
            """;

    private Datainitializer() {
        // Utility class
    }

    /**
     * Creates required database tables if missing.
     *
     * @throws SQLException if execution fails
     */
    public static void initialize() throws SQLException {
        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(CREATE_USERS_TABLE);
        }
    }
}