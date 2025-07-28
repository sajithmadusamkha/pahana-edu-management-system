package com.example.pahanaedubackend.service;

import com.example.pahanaedubackend.dao.BillDAO;
import com.example.pahanaedubackend.dao.ItemDAO;
import com.example.pahanaedubackend.factory.DAOFactory;
import com.example.pahanaedubackend.model.Bill;
import com.example.pahanaedubackend.model.BillItem;
import com.example.pahanaedubackend.model.Item;
import com.example.pahanaedubackend.util.DBUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class BillService {
    private final BillDAO billDAO = new BillDAO();
    private final ItemDAO itemDAO = new ItemDAO();

    public int createBill(String customerAccountNumber, List<BillItem> billItems) {
        double total = 0.0;
        List<BillItem> validItems = new ArrayList<>();

        for (BillItem billItem : billItems) {
            Item item = itemDAO.getItemById(billItem.getItemId());
            if (item != null && item.getQuantity() >= billItem.getQuantity()) {
                double itemTotal = item.getPrice() * billItem.getQuantity();
                total += itemTotal;

                billItem.setPrice(item.getPrice());
                validItems.add(billItem);

                // update item stock
                itemDAO.updateStock(item.getId(), item.getQuantity() - billItem.getQuantity());
            } else {
                return -1; // insufficient stock or invalid item
            }
        }

        Bill bill = new Bill();
        bill.setBillDate(new Date());
        bill.setTotal(total);
        bill.setCustomerAccountNumber(customerAccountNumber);

        return billDAO.createBill(bill, validItems);
    }

    public Map<String, Object> getBillDetails(int billId) {
        return billDAO.getBillDetails(billId);
    }
}
