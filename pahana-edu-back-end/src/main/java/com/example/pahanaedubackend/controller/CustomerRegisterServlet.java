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
import java.util.HashMap;
import java.util.Map;

@WebServlet("/register-customer")
public class CustomerRegisterServlet extends HttpServlet {
    private final CustomerService customerService = new CustomerService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> data = mapper.readValue(request.getInputStream(), Map.class);

        String accountNumber = data.get("accountNumber");
        String name = data.get("name");
        String phone = data.get("phone");
        String address = data.get("address");
        String password = data.get("password");

        Customer customer = new Customer();
        customer.setAccountNumber(accountNumber);
        customer.setName(name);
        customer.setAddress(address);
        customer.setPhone(phone);

        boolean success = customerService.registerCustomer(customer, password);

        Map<String, Object> res = new HashMap<>();
        if (success) {
            res.put("success", true);
            res.put("message", "Customer registered successfully");
        } else {
            res.put("success", false);
            res.put("message", "Failed to register customer");
        }

        mapper.writeValue(response.getWriter(), res);
    }
}
