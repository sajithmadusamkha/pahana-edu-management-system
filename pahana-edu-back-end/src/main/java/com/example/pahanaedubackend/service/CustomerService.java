package com.example.pahanaedubackend.service;

import com.example.pahanaedubackend.dao.CustomerDAO;
import com.example.pahanaedubackend.model.Customer;

public class CustomerService {
    private final CustomerDAO customerDAO = new CustomerDAO();

    public boolean registerCustomer(String accNo, String name, String address, String phone) {
        Customer customer = new Customer(accNo, name, address, phone);
        return customerDAO.addCustomer(customer);
    }
}
