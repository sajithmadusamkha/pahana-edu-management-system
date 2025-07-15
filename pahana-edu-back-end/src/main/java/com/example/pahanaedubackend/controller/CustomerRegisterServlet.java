package com.example.pahanaedubackend.controller;

import com.example.pahanaedubackend.model.Customer;
import com.example.pahanaedubackend.service.CustomerService;
import com.example.pahanaedubackend.util.PasswordUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/register-customer")
public class CustomerRegisterServlet extends HttpServlet {
    private final CustomerService customerService = new CustomerService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        Customer customer = objectMapper.readValue(request.getInputStream(), Customer.class);

        // Hash the password before saving
        customer.setPassword(PasswordUtil.hashPassword(customer.getPassword()));

        boolean success = customerService.registerCustomer(customer);


        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        if (success) {
            response.getWriter().println("Customer registered successfully.");
        } else {
            response.getWriter().println("Failed to register customer.");
        }
    }
}
