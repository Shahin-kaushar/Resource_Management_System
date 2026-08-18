package com.example.rms;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UsageRepository {

    public int getUsedQuantity(int resourceId, LocalDateTime start, LocalDateTime end)
            throws SQLException {

        String sql = """
                SELECT COALESCE(SUM(quantity), 0)
                FROM usage_records
                WHERE resource_id = ?
                  AND status = 'ACTIVE'
                  AND start_time < ?
                  AND planned_end_time > ?
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, resourceId);
            ps.setTimestamp(2, Timestamp.valueOf(end));
            ps.setTimestamp(3, Timestamp.valueOf(start));

            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    public int createUsage(int resourceId, int quantity, int people,
                           LocalDateTime start, LocalDateTime plannedEnd,
                           double totalCost) throws SQLException {

        String sql = """
                INSERT INTO usage_records
                (resource_id, quantity, people, start_time, planned_end_time,
                 total_cost, status)
                VALUES (?, ?, ?, ?, ?, ?, 'ACTIVE')
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, resourceId);
            ps.setInt(2, quantity);
            ps.setInt(3, people);
            ps.setTimestamp(4, Timestamp.valueOf(start));
            ps.setTimestamp(5, Timestamp.valueOf(plannedEnd));
            ps.setDouble(6, totalCost);

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }

        throw new SQLException("Could not create usage record.");
    }

    public void finishUsage(int usageId) throws SQLException {
        String sql = """
                UPDATE usage_records
                SET actual_end_time = ?, status = 'COMPLETED'
                WHERE id = ? AND status = 'ACTIVE'
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(2, usageId);

            int updated = ps.executeUpdate();

            if (updated == 0) {
                throw new SQLException("Active usage record not found.");
            }
        }
    }

    public List<String> findAllUsage() throws SQLException {
        List<String> result = new ArrayList<>();

        String sql = """
                SELECT u.id, r.name, u.quantity, u.people,
                       u.start_time, u.planned_end_time,
                       u.actual_end_time, u.total_cost, u.status
                FROM usage_records u
                JOIN resources r ON u.resource_id = r.id
                ORDER BY u.id DESC
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(
                        "ID=" + rs.getInt("id") +
                        " | " + rs.getString("name") +
                        " | quantity=" + rs.getInt("quantity") +
                        " | people=" + rs.getInt("people") +
                        " | start=" + rs.getTimestamp("start_time") +
                        " | planned end=" + rs.getTimestamp("planned_end_time") +
                        " | actual end=" + rs.getTimestamp("actual_end_time") +
                        " | cost=₹" + rs.getDouble("total_cost") +
                        " | " + rs.getString("status")
                );
            }
        }

        return result;
    }
}
