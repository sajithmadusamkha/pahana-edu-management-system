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
import javax.servlet.http.HttpSession;
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

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("admin") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"success\":false, \"message\":\"Unauthorized: Admin login required\"}");
            return;
        }

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> data = mapper.readValue(request.getInputStream(), Map.class);

        Customer customer = new Customer();
        customer.setAccountNumber((String) data.get("accountNumber"));
        customer.setFullName((String) data.get("fullName"));
        customer.setTelephone((String) data.get("telephone"));
        customer.setAddress((String) data.get("address"));

        // Handle unitsConsumed which can be either Integer or String
        Object unitsConsumedObj = data.get("unitsConsumed");
        int unitsConsumed;
        if (unitsConsumedObj instanceof Integer) {
            unitsConsumed = (Integer) unitsConsumedObj;
        } else if (unitsConsumedObj instanceof String) {
            unitsConsumed = Integer.parseInt((String) unitsConsumedObj);
        } else {
            throw new IllegalArgumentException("Invalid unitsConsumed value: " + unitsConsumedObj);
        }
        customer.setUnitsConsumed(unitsConsumed);

        boolean success = customerService.registerCustomer(customer);

        Map<String, Object> result = new HashMap<>();
        result.put("success", success);
        result.put("message", success ? "Customer registered successfully" : "Customer registration failed");

        mapper.writeValue(response.getWriter(), result);
    }
}
