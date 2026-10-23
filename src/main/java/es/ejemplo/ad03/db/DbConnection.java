package es.ejemplo.ad03.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DbConnection {
    private static final String DEFAULT_URL =
            "jdbc:mysql://localhost:3306/formacion?serverTimezone=UTC&useSSL=false";

    private DbConnection() {
    }

    public static Connection open() throws SQLException {
        String url = environmentOrDefault("AD03_DB_URL", DEFAULT_URL);
        String user = System.getenv("AD03_DB_USER");
        String password = System.getenv("AD03_DB_PASSWORD");

        if (user == null || user.isBlank()) {
            throw new SQLException("Define la variable de entorno AD03_DB_USER antes de ejecutar el programa.");
        }
        if (password == null) {
            throw new SQLException("Define la variable de entorno AD03_DB_PASSWORD antes de ejecutar el programa.");
        }
        return DriverManager.getConnection(url, user, password);
    }

    private static String environmentOrDefault(String name, String defaultValue) {
        String value = System.getenv(name);
        return value == null || value.isBlank() ? defaultValue : value;
    }
}
