package com.mycompany.rentcar.dao;

import com.mycompany.rentcar.model.Vehicle;
import com.mycompany.rentcar.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleDAO {

    // Get all vehicles
    public static List<Vehicle> getAllVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM vehicles ORDER BY id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                vehicles.add(mapResultSetToVehicle(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicles;
    }

    public static Vehicle getVehicleById(int vehicleId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    // Search vehicle by registration number, make, or model (partial match)
    public List<Vehicle> searchByRegNoOrName(String query) {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM vehicles WHERE plate_no LIKE ? OR make LIKE ? OR model LIKE ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + query + "%");
            ps.setString(2, "%" + query + "%");
            ps.setString(3, "%" + query + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    vehicles.add(mapResultSetToVehicle(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicles;
    }

    // ✅ Fixed: Search by regNo OR model
    public List<Vehicle> searchByRegNoOrModel(String query) {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM vehicles WHERE plate_no LIKE ? OR model LIKE ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + query + "%");
            ps.setString(2, "%" + query + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    vehicles.add(mapResultSetToVehicle(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicles;
    }

    // Search vehicle by exact registration number
    public static Vehicle searchByRegNo(String regNo) {
        String sql = "SELECT * FROM vehicles WHERE plate_no = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, regNo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapResultSetToVehicle(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Add new vehicle
    public static boolean addVehicle(Vehicle vehicle) {
        String sql = "INSERT INTO vehicles (make, model, year, plate_no, color, daily_rate_cents, status, odometer, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, vehicle.getMake());
            ps.setString(2, vehicle.getModel());
            ps.setInt(3, vehicle.getYear());
            ps.setString(4, vehicle.getLicensePlate());
            ps.setString(5, vehicle.getColor());
            ps.setInt(6, (int) (vehicle.getDailyRate() * 100));
            ps.setString(7, vehicle.getStatus());
            ps.setInt(8, vehicle.getMileage());
            ps.setLong(9, System.currentTimeMillis() / 1000);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update vehicle
    public static boolean updateVehicle(Vehicle vehicle) {
        String sql = "UPDATE vehicles SET make = ?, model = ?, year = ?, plate_no = ?, color = ?, daily_rate_cents = ?, status = ?, odometer = ?, updated_at = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, vehicle.getMake());
            ps.setString(2, vehicle.getModel());
            ps.setInt(3, vehicle.getYear());
            ps.setString(4, vehicle.getLicensePlate());
            ps.setString(5, vehicle.getColor());
            ps.setInt(6, (int) (vehicle.getDailyRate() * 100));
            ps.setString(7, vehicle.getStatus());
            ps.setInt(8, vehicle.getMileage());
            ps.setLong(9, System.currentTimeMillis() / 1000);
            ps.setInt(10, vehicle.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete vehicle
    public static boolean deleteVehicle(int id) {
        String sql = "DELETE FROM vehicles WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Map ResultSet to Vehicle object
    private static Vehicle mapResultSetToVehicle(ResultSet rs) throws SQLException {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(rs.getInt("id"));
        vehicle.setMake(rs.getString("make"));
        vehicle.setModel(rs.getString("model"));
        vehicle.setYear(rs.getInt("year"));
        vehicle.setLicensePlate(rs.getString("plate_no"));
        vehicle.setColor(rs.getString("color"));
        vehicle.setDailyRate(rs.getInt("daily_rate_cents") / 100.0);
        vehicle.setStatus(rs.getString("status"));
        vehicle.setMileage(rs.getInt("odometer"));
        return vehicle;
    }
}