package org.example.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    private static final String URL = System.getenv("DB_URL"); // change db name
    private static final String USER = System.getenv("DB_USERNAME");   // change
    private static final String PASSWORD = System.getenv("DB_PASSWORD"); // change

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            System.out.println("❌ Database connection failed: " + e.getMessage());
            return null;
        }
    }

}
