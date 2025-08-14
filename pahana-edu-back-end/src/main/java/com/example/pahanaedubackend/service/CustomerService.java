package com.example.pahanaedubackend.service;

import com.example.pahanaedubackend.dao.CustomerDAO;
import com.example.pahanaedubackend.factory.impl.DAOFactory;
import com.example.pahanaedubackend.model.Customer;

import java.util.List;
import java.util.Map;

public class CustomerService {
    private final CustomerDAO customerDAO;

    // Constructor using Factory Pattern
    public CustomerService() {
        this.customerDAO = DAOFactory.getInstance().getCustomerDAO();
    }

    public boolean registerCustomer(Customer customer) {
        return customerDAO.addCustomer(customer);
    }

    public List<Customer> getAllCustomers() {
        return customerDAO.getAllCustomers();
    }

    public boolean updateCustomer(Customer customer) {
        return customerDAO.updateCustomer(customer);
    }

    public boolean deleteCustomer(String accountNumber) {
        return customerDAO.deleteCustomer(accountNumber);
    }

    public List<Map<String, Object>> getRecentCustomers(int limit) {
        return customerDAO.getRecentCustomers(limit);
    }
}
