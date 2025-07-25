package com.example.pahanaedubackend.service;

import com.example.pahanaedubackend.dao.CustomerDAO;
import com.example.pahanaedubackend.dao.UserDAO;
import com.example.pahanaedubackend.model.Customer;
import com.example.pahanaedubackend.util.PasswordUtil;

import java.util.List;

public class CustomerService {
    private final CustomerDAO customerDAO = new CustomerDAO();

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
}
