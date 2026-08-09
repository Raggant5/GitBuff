package data_access;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Provides database connection utilities for the GitBuff application.
 */
public final class Database {

    private static final String DATABASE_DIRECTORY = "data";
    private static final String URL = "jdbc:sqlite:data/gitbuff.db";

    private Database() {
        // Prevent instantiation of utility class.
    }

    /**
     * Opens a connection to the SQLite database and enables foreign keys.
     *
     * @return an active database connection.
     * @throws SQLException if the data directory cannot be created or the database connection fails.
     */
    public static Connection connect() throws SQLException {
        createDataDirectory();

        final Connection connection = DriverManager.getConnection(URL);

        try (Statement statement = connection.createStatement()) {
            statement.execute("PRAGMA foreign_keys = ON;");
        }

        return connection;
    }

    /**
     * Creates the database directory when it does not already exist.
     *
     * @throws SQLException if the directory cannot be created or if a file named data already exists.
     */
    private static void createDataDirectory() throws SQLException {
        final File dataDirectory = new File(DATABASE_DIRECTORY);

        if (!dataDirectory.exists() && !dataDirectory.mkdirs()) {
            throw new SQLException("Could not create data directory.");
        }

        if (!dataDirectory.isDirectory()) {
            throw new SQLException("'data' exists but is not a directory.");
        }
    }
}
