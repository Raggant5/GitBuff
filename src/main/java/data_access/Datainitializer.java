package data_access;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Datainitializer {

    public static void initialize() throws SQLException {

        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement()) {

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    username TEXT PRIMARY KEY,
                    password TEXT NOT NULL
                );
            """);

            System.out.println("Database initialized.");
        }
    }
}