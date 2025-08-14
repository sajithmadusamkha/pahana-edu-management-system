package com.example.pahanaedubackend.service;

import com.example.pahanaedubackend.dao.ItemDAO;
import com.example.pahanaedubackend.factory.impl.DAOFactory;
import com.example.pahanaedubackend.model.Item;

import java.util.List;
import java.util.Map;

public class ItemService {
    private final ItemDAO itemDAO;

    // Constructor using Factory Pattern
    public ItemService() {
        this.itemDAO = DAOFactory.getInstance().getItemDAO();
    }

    public boolean createItem(Item item) {
        return itemDAO.addItem(item);
    }

    public List<Item> getAllItems() {
        return itemDAO.getAllItems();
    }

    public boolean updateItem(Item item) {
        return itemDAO.updateItem(item);
    }

    public boolean isItemUsedInBills(int itemId) {
        return itemDAO.isItemUsedInBills(itemId);
    }

    public boolean deleteItem(int itemId) {
        return itemDAO.deleteItem(itemId);
    }

    public List<Map<String, Object>> getRecentItems(int limit) {
        return itemDAO.getRecentItems(limit);
    }
}
