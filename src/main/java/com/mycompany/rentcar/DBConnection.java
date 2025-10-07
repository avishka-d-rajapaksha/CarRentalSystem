package com.mycompany.rentcar;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // Database details
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/carrental";
    private static final String USER = "root";   // <-- change if needed
    private static final String PASSWORD = "";   // <-- put your MySQL password here

    // Create and return a connection
    public static Connection getConnection() {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Database connected successfully!");
            return conn;
        } catch (SQLException e) {
            System.err.println("❌ Database connection failed: " + e.getMessage());
            return null;
        }
    }
}