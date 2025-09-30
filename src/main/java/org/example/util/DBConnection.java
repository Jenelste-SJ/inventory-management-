package org.example.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/inventoryDatabase"; // change db name
    private static final String USER = "root";   // change
    private static final String PASSWORD = "Clashofclans"; // change

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            System.out.println("❌ Database connection failed: " + e.getMessage());
            return null;
        }
    }
}
