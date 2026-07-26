package data_access;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private static final String DATABASE_DIRECTORY = "data";
    private static final String URL = "jdbc:sqlite:data/gitbuff.db";

    private Database() {
        // Utility class
    }

    public static Connection connect() throws SQLException {
        createDataDirectory();
        return DriverManager.getConnection(URL);
    }

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