package com.mycompany.rentcar.dao;
import com.mycompany.rentcar.User;
import com.mycompany.rentcar.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    
    // Check if username already exists
    public static boolean usernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
    
    // Sign-up method
    public static boolean signUp(User user) {
        // First check if username already exists
        if (usernameExists(user.getUsername())) {
            System.out.println("Username already exists: " + user.getUsername());
            return false;
        }
        
        String sql = "INSERT INTO users (username, password_hash, full_name, role, is_active, created_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        // Ensure ENUM role is valid - now including USER
        String role = user.getRole().toUpperCase();
        if (!role.equals("ADMIN") && !role.equals("MANAGER") && !role.equals("STAFF") && !role.equals("USER")) {
            System.out.println("Invalid role: " + role);
            return false;
        }
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPasswordHash());
            ps.setString(3, user.getFullName());
            ps.setString(4, role); // Correct ENUM value
            ps.setInt(5, user.isActive() ? 1 : 0);
            ps.setLong(6, System.currentTimeMillis() / 1000); // UNIX timestamp
            
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    // Login method
    public static User login(String username, String passwordHash) {
        String sql = "SELECT * FROM users WHERE username=? AND password_hash=? AND is_active=1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, passwordHash);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setFullName(rs.getString("full_name"));
                user.setRole(rs.getString("role"));
                user.setActive(rs.getInt("is_active") == 1);
                return user;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}