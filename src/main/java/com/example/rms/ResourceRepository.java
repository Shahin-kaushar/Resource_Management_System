package com.example.rms;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ResourceRepository {

    public List<Resource> findAll() throws SQLException {
        List<Resource> resources = new ArrayList<>();

        String sql = "SELECT * FROM resources ORDER BY id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                resources.add(new Resource(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getInt("capacity"),
                        rs.getDouble("unit_price_first_hour"),
                        rs.getDouble("unit_price_additional_hour")
                ));
            }
        }

        return resources;
    }

    public Resource findById(int id) throws SQLException {
        String sql = "SELECT * FROM resources WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Resource(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("category"),
                            rs.getInt("capacity"),
                            rs.getDouble("unit_price_first_hour"),
                            rs.getDouble("unit_price_additional_hour")
                    );
                }
            }
        }

        return null;
    }
}
