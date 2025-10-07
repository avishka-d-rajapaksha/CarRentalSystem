package com.mycompany.rentcar.dao;

import com.mycompany.rentcar.model.Booking;
import com.mycompany.rentcar.util.DBConnection;

import java.sql.*;

public class BookingDAO {

    // Add booking
    public void addBooking(Booking booking) {
        String sql = "INSERT INTO bookings (customer_id, vehicle_id, pickup_ts, return_ts, " +
                     "rate_cents, deposit_cents, tax_rate_bp, discount_cents, pickup_odometer, " +
                     "return_odometer, late_fee_cents, status, notes, created_by, created_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, booking.getCustomerId());
            pst.setInt(2, booking.getVehicleId());
            pst.setLong(3, booking.getPickupTs());
            pst.setLong(4, booking.getReturnTs());
            pst.setInt(5, booking.getRateCents());
            pst.setInt(6, booking.getDepositCents());
            pst.setInt(7, booking.getTaxRateBp());
            pst.setInt(8, booking.getDiscountCents());
            if (booking.getPickupOdometer() != null) pst.setInt(9, booking.getPickupOdometer());
            else pst.setNull(9, Types.INTEGER);
            if (booking.getReturnOdometer() != null) pst.setInt(10, booking.getReturnOdometer());
            else pst.setNull(10, Types.INTEGER);
            pst.setInt(11, booking.getLateFeeCents());
            pst.setString(12, booking.getStatus());
            pst.setString(13, booking.getNotes());
            pst.setInt(14, booking.getCreatedBy());
            pst.setLong(15, booking.getCreatedAt());
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Close booking (mark as returned)
    public boolean closeBooking(int bookingId, long returnDate,
                                int returnOdometer, int lateFeeCents, String status) {
        String sql = "UPDATE bookings SET return_ts = ?, return_odometer = ?, " +
                     "late_fee_cents = ?, status = ?, updated_at = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setLong(1, returnDate);
            pst.setInt(2, returnOdometer);
            pst.setInt(3, lateFeeCents);
            pst.setString(4, status);
            pst.setLong(5, System.currentTimeMillis() / 1000L);
            pst.setInt(6, bookingId);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get booking by ID
    public Booking getBookingById(int id) {
        String sql = "SELECT * FROM bookings WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) return mapResultSetToBooking(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get booking by vehicle registration (join with vehicle table)
    public Booking getBookingByVehicleReg(String regNo) {
        String sql = "SELECT b.* FROM bookings b " +
                     "JOIN vehicles v ON b.vehicle_id = v.id WHERE v.plate_no = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, regNo);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) return mapResultSetToBooking(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Map ResultSet to Booking object
    private Booking mapResultSetToBooking(ResultSet rs) throws SQLException {
        Booking booking = new Booking();
        booking.setId(rs.getInt("id"));
        booking.setCustomerId(rs.getInt("customer_id"));
        booking.setVehicleId(rs.getInt("vehicle_id"));
        booking.setPickupTs(rs.getLong("pickup_ts"));
        booking.setReturnTs(rs.getLong("return_ts"));
        booking.setStatus(rs.getString("status"));
        booking.setRateCents(rs.getInt("rate_cents"));
        booking.setDepositCents(rs.getInt("deposit_cents"));
        booking.setTaxRateBp(rs.getInt("tax_rate_bp"));
        booking.setDiscountCents(rs.getInt("discount_cents"));
        int pickupOdo = rs.getInt("pickup_odometer");
        booking.setPickupOdometer(rs.wasNull() ? null : pickupOdo);
        int returnOdo = rs.getInt("return_odometer");
        booking.setReturnOdometer(rs.wasNull() ? null : returnOdo);
        booking.setLateFeeCents(rs.getInt("late_fee_cents"));
        booking.setNotes(rs.getString("notes"));
        booking.setCreatedBy(rs.getInt("created_by"));
        booking.setCreatedAt(rs.getLong("created_at"));
        long updated = rs.getLong("updated_at");
        booking.setUpdatedAt(rs.wasNull() ? null : updated);
        return booking;
    }
}