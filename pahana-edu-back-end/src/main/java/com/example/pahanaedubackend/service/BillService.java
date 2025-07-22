package com.example.pahanaedubackend.service;

import com.example.pahanaedubackend.dao.ItemDAO;
import com.example.pahanaedubackend.model.BillItem;
import com.example.pahanaedubackend.model.Item;
import com.example.pahanaedubackend.util.DBUtil;

import java.sql.*;
import java.util.List;

public class BillService {
    private final ItemDAO itemDAO = new ItemDAO();

    public boolean createBill(List<BillItem> items) {
        double total = 0;
        try (Connection conn = DBUtil.getInstance().getConnection()) {
            conn.setAutoCommit(false);

            // Step 1: Calculate total and validate stock
            for (BillItem billItem : items) {
                Item item = itemDAO.getItemById(billItem.getItemId());
                if (item == null || item.getQuantity() < billItem.getQuantity()) {
                    conn.rollback();
                    return false;
                }
                total += item.getPrice() * billItem.getQuantity();
            }

            // Step 2: Insert into bills table
            String insertBillSQL = "INSERT INTO bills (total_amount) VALUES (?)";
            try (PreparedStatement ps = conn.prepareStatement(insertBillSQL, Statement.RETURN_GENERATED_KEYS)) {
                ps.setDouble(1, total);
                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                int billId = rs.getInt(1);

                // Step 3: Insert into bill_items and update stock
                for (BillItem billItem : items) {
                    Item item = itemDAO.getItemById(billItem.getItemId());

                    String insertBillItemSQL = "INSERT INTO bill_items (bill_id, item_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement biStmt = conn.prepareStatement(insertBillItemSQL)) {
                        biStmt.setInt(1, billId);
                        biStmt.setInt(2, billItem.getItemId());
                        biStmt.setInt(3, billItem.getQuantity());
                        biStmt.setDouble(4, item.getPrice());
                        biStmt.executeUpdate();
                    }

                    // Update item stock
                    String updateStockSQL = "UPDATE items SET quantity = quantity - ? WHERE id = ?";
                    try (PreparedStatement stockStmt = conn.prepareStatement(updateStockSQL)) {
                        stockStmt.setInt(1, billItem.getQuantity());
                        stockStmt.setInt(2, billItem.getItemId());
                        stockStmt.executeUpdate();
                    }
                }

                conn.commit();
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
