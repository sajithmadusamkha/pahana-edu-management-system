package com.example.pahanaedubackend.dao;

import com.example.pahanaedubackend.model.Admin;
import com.example.pahanaedubackend.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AdminDAO {
    public boolean addAdmin(Admin admin) {
        String sql = "INSERT INTO admin (username, full_name) VALUES (?, ?)";

        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, admin.getUsername());
            stmt.setString(2, admin.getFullName());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
