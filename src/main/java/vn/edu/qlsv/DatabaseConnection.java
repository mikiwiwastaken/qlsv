package vn.edu.qlsv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseConnection {
    private static final String URL = setting(
            "qlsv.db.url", "QLSV_DB_URL", "jdbc:mariadb://localhost:3306/qlsv");
    private static final String USER = setting("qlsv.db.user", "QLSV_DB_USER", "root");
    private static final String PASSWORD = setting("qlsv.db.password", "QLSV_DB_PASSWORD", "");

    private DatabaseConnection() {
    }

    private static String setting(String property, String environmentVariable, String defaultValue) {
        String value = System.getProperty(property);
        if (value == null || value.isBlank()) {
            value = System.getenv(environmentVariable);
        }
        return value == null || value.isBlank() ? defaultValue : value;
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
