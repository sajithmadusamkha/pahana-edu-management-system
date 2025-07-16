package com.example.pahanaedubackend.service;

import com.example.pahanaedubackend.dao.CustomerDAO;
import com.example.pahanaedubackend.dao.UserDAO;
import com.example.pahanaedubackend.model.Customer;
import com.example.pahanaedubackend.util.PasswordUtil;

public class CustomerService {
    private final CustomerDAO customerDAO = new CustomerDAO();
    private final UserDAO userDAO = new UserDAO(); // ✅ Add this line

    public boolean registerCustomer(Customer customer, String plainPassword) {
        boolean profileSaved = customerDAO.addCustomer(customer);

        if (profileSaved) {
            String hashedPassword = PasswordUtil.hashPassword(plainPassword);
            return userDAO.createUser(customer.getAccountNumber(), hashedPassword, "C");
        }

        return false;
    }
}
