package com.example.pahanaedubackend.dao;

import com.example.pahanaedubackend.model.Bill;
import com.example.pahanaedubackend.model.BillItem;
import com.example.pahanaedubackend.util.DBUtil;

import java.sql.*;
import java.util.List;

public class BillDAO {
    public boolean createBill(Bill bill, List<BillItem> billItems) {
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
                    return true;
                }
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
