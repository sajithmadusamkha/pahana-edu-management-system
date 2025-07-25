package com.example.pahanaedubackend.dao;

import com.example.pahanaedubackend.model.Bill;
import com.example.pahanaedubackend.model.BillItem;
import com.example.pahanaedubackend.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BillDAO {
    public int createBill(Bill bill, List<BillItem> billItems) {
        String billSql = "INSERT INTO bills (bill_date, total_amount, customer_account_number) VALUES (?, ?, ?)";
        String billItemSql = "INSERT INTO bill_items (bill_id, item_id, quantity, unit_price) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBUtil.getInstance().getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement billStmt = conn.prepareStatement(billSql, Statement.RETURN_GENERATED_KEYS)) {
                billStmt.setDate(1, new java.sql.Date(bill.getBillDate().getTime()));
                billStmt.setDouble(2, bill.getTotal());
                billStmt.setString(3, (bill.getCustomerAccountNumber()));

                billStmt.executeUpdate();

                ResultSet generatedKeys = billStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int billId = generatedKeys.getInt(1);

                    try (PreparedStatement itemStmt = conn.prepareStatement(billItemSql)) {
                        for (BillItem item : billItems) {
                            itemStmt.setInt(1, billId);
                            itemStmt.setInt(2, item.getItemId());
                            itemStmt.setInt(3, item.getQuantity());
                            itemStmt.setDouble(4, item.getPrice());
                            itemStmt.addBatch();
                        }
                        itemStmt.executeBatch();
                    }

                    conn.commit();
                    return billId;
                }
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public Map<String, Object> getBillDetails(int billId) {
        String billSql = "SELECT b.*, c.full_name, c.address, c.telephone " +
                        "FROM bills b " +
                        "LEFT JOIN customer c ON b.customer_account_number = c.account_number " +
                        "WHERE b.id = ?";

        String itemsSql = "SELECT bi.*, i.name as item_name " +
                         "FROM bill_items bi " +
                         "JOIN items i ON bi.item_id = i.id " +
                         "WHERE bi.bill_id = ?";

        try (Connection conn = DBUtil.getInstance().getConnection()) {
            Map<String, Object> billDetails = new HashMap<>();

            // Get bill information
            try (PreparedStatement billStmt = conn.prepareStatement(billSql)) {
                billStmt.setInt(1, billId);
                ResultSet billRs = billStmt.executeQuery();

                if (billRs.next()) {
                    billDetails.put("id", billRs.getInt("id"));
                    billDetails.put("billDate", billRs.getDate("bill_date"));
                    billDetails.put("total", billRs.getDouble("total_amount"));
                    billDetails.put("customerAccountNumber", billRs.getString("customer_account_number"));
                    billDetails.put("customerName", billRs.getString("full_name"));
                    billDetails.put("customerAddress", billRs.getString("address"));
                    billDetails.put("customerTelephone", billRs.getString("telephone"));
                } else {
                    return null; // Bill not found
                }
            }

            // Get bill items
            List<Map<String, Object>> items = new ArrayList<>();
            try (PreparedStatement itemsStmt = conn.prepareStatement(itemsSql)) {
                itemsStmt.setInt(1, billId);
                ResultSet itemsRs = itemsStmt.executeQuery();

                while (itemsRs.next()) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("itemId", itemsRs.getInt("item_id"));
                    item.put("itemName", itemsRs.getString("item_name"));
                    item.put("quantity", itemsRs.getInt("quantity"));
                    item.put("unitPrice", itemsRs.getDouble("unit_price"));
                    item.put("total", itemsRs.getInt("quantity") * itemsRs.getDouble("unit_price"));
                    items.add(item);
                }
            }

            billDetails.put("items", items);
            return billDetails;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
