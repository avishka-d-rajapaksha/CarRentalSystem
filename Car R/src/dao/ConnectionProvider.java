
package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionProvider {

    private static final String URL = "jdbc:mysql://127.0.0.1:3306/car_rental";
    private static final String USER = "root"; // Change if your DB has another username
    private static final String PASSWORD = ""; // Change if your DB has a password

    // Method to get a database connection
    public static Connection getCon() {
        Connection con = null;
        try {
            // Load MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Create connection
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connected successfully!");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found.");
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database.");
        }
        return con;
    }
}
