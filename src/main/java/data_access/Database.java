package data_access;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

    public class Database {
        private Database() {
            /* This utility class should not be instantiated */
        }

        private static final String URL = "jdbc:sqlite:data/gitbuff.db";

        public static Connection connect() throws SQLException {
            return DriverManager.getConnection(URL);
        }
    }
