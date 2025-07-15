package com.example.pahanaedubackend.service;

import com.example.pahanaedubackend.dao.CustomerDAO;
import com.example.pahanaedubackend.model.Customer;
import com.example.pahanaedubackend.util.PasswordUtil;

public class CustomerService {
    private final CustomerDAO customerDAO = new CustomerDAO();

    public boolean registerCustomer(Customer customer) {
        return customerDAO.addCustomer(customer);
    }

    public boolean login(String accountNumber, String password) {
        Customer customer = customerDAO.getCustomerByAccountNumber(accountNumber);
        if (customer == null) return false;

        return PasswordUtil.checkPassword(password, customer.getPassword());
    }
}
