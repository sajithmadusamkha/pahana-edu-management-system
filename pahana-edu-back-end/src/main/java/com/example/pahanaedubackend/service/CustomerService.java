package com.example.pahanaedubackend.service;

import com.example.pahanaedubackend.dao.CustomerDAO;
import com.example.pahanaedubackend.model.Customer;
import com.example.pahanaedubackend.util.PasswordUtil;

public class CustomerService {
    private final CustomerDAO customerDAO = new CustomerDAO();

    public boolean registerCustomer(String accNo, String name, String address, String phone, String password) {
        String hashedPassword = PasswordUtil.hashPassword(password);
        Customer customer = new Customer(accNo, name, address, phone, hashedPassword);
        return customerDAO.addCustomer(customer);
    }
}
