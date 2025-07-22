package com.example.pahanaedubackend.service;

import com.example.pahanaedubackend.dao.ItemDAO;
import com.example.pahanaedubackend.model.Item;

import java.util.List;

public class ItemService {
    private final ItemDAO itemDAO = new ItemDAO();

    public boolean createItem(Item item) {
        return itemDAO.addItem(item);
    }

    public List<Item> getAllItems() {
        return itemDAO.getAllItems();
    }

    public boolean updateItem(Item item) {
        return itemDAO.updateItem(item);
    }

    public boolean deleteItem(int itemId) {
        return itemDAO.deleteItem(itemId);
    }
}
