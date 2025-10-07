package com.mycompany.rentcar.dao;

import com.mycompany.rentcar.model.Customer;
import com.mycompany.rentcar.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    private final Connection conn;

    // Constructor to initialize database connection
    public CustomerDAO() throws SQLException {
        this.conn = DBConnection.getConnection();
    }

    /**
     * Get a customer by ID
     */
    public Customer getCustomerById(int customerId) throws SQLException {
        String sql = "SELECT * FROM customers WHERE id = ?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, customerId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Customer c = new Customer();
                    c.setId(rs.getInt("id"));
                    c.setFullName(rs.getString("full_name"));
                    c.setLicenseNo(rs.getString("license_no"));
                    c.setPhone(rs.getString("phone"));
                    c.setEmail(rs.getString("email"));
                    c.setAddress(rs.getString("address"));
                    c.setBlacklisted(rs.getBoolean("blacklisted"));
                    c.setNotes(rs.getString("notes"));
                    c.setCreatedAt(rs.getLong("created_at"));
                    return c;
                }
            }
        }
        return null; // customer not found
    }

    /**
     * Add a new customer
     */
    public void addCustomer(Customer c) throws SQLException {
        String sql = "INSERT INTO customers (full_name, license_no, phone, email, address, blacklisted, notes, created_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, c.getFullName());
            pst.setString(2, c.getLicenseNo());
            pst.setString(3, c.getPhone());
            pst.setString(4, c.getEmail());
            pst.setString(5, c.getAddress());
            pst.setBoolean(6, c.isBlacklisted());
            pst.setString(7, c.getNotes());
            pst.setLong(8, System.currentTimeMillis());
            pst.executeUpdate();
        }
    }

    /**
     * Update existing customer
     */
    public void updateCustomer(Customer c) throws SQLException {
        String sql = "UPDATE customers SET full_name=?, license_no=?, phone=?, email=?, address=?, blacklisted=?, notes=? WHERE id=?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, c.getFullName());
            pst.setString(2, c.getLicenseNo());
            pst.setString(3, c.getPhone());
            pst.setString(4, c.getEmail());
            pst.setString(5, c.getAddress());
            pst.setBoolean(6, c.isBlacklisted());
            pst.setString(7, c.getNotes());
            pst.setInt(8, c.getId());
            pst.executeUpdate();
        }
    }

    /**
     * Delete a customer by ID
     */
    public void deleteCustomer(int id) throws SQLException {
        String sql = "DELETE FROM customers WHERE id=?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }

    /**
     * Search customers by name or license (partial match)
     */
    public List<Customer> searchByLicenseOrName(String keyword) throws SQLException {
        List<Customer> result = new ArrayList<>();
        String sql = "SELECT * FROM customers WHERE full_name LIKE ? OR license_no LIKE ? ORDER BY id";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            String pattern = "%" + keyword + "%";
            pst.setString(1, pattern);
            pst.setString(2, pattern);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Customer c = new Customer();
                    c.setId(rs.getInt("id"));
                    c.setFullName(rs.getString("full_name"));
                    c.setLicenseNo(rs.getString("license_no"));
                    c.setPhone(rs.getString("phone"));
                    c.setEmail(rs.getString("email"));
                    c.setAddress(rs.getString("address"));
                    c.setBlacklisted(rs.getBoolean("blacklisted"));
                    c.setNotes(rs.getString("notes"));
                    c.setCreatedAt(rs.getLong("created_at"));
                    result.add(c);
                }
            }
        }
        return result;
    }
}
    