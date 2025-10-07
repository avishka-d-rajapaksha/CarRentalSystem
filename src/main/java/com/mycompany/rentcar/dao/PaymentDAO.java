package com.mycompany.rentcar.dao;

import com.mycompany.rentcar.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PaymentDAO {
    // Sum all CAPTURED payments (any category) for a booking
    public int getTotalCapturedAmountCentsByBooking(int bookingId) {
        String sql = "SELECT COALESCE(SUM(amount_cents),0) AS total_cents " +
                     "FROM payments WHERE booking_id = ? AND status = 'CAPTURED'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("total_cents");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}