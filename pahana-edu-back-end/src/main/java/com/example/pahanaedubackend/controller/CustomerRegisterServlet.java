package com.example.pahanaedubackend.controller;

import com.example.pahanaedubackend.model.Customer;
import com.example.pahanaedubackend.service.CustomerService;
import com.example.pahanaedubackend.util.PasswordUtil;
import com.example.pahanaedubackend.util.ValidationUtil;
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

        String accountNumber = (String) data.get("accountNumber");
        String fullName = (String) data.get("fullName");
        String telephone = (String) data.get("telephone");
        String address = (String) data.get("address");

        Customer customer = new Customer();
        customer.setAccountNumber(accountNumber.trim().toUpperCase());
        customer.setFullName(fullName.trim());
        customer.setTelephone(telephone.trim());
        customer.setAddress(address.trim());

        // Handle unitsConsumed which can be either Integer or String
        Object unitsConsumedObj = data.get("unitsConsumed");
        int unitsConsumed;
        Map<String, Object> result = new HashMap<>();

        try {
            if (unitsConsumedObj instanceof Integer) {
                unitsConsumed = (Integer) unitsConsumedObj;
            } else if (unitsConsumedObj instanceof String) {
                unitsConsumed = Integer.parseInt((String) unitsConsumedObj);
            } else {
                result.put("success", false);
                result.put("message", "Units consumed must be a valid number");
                mapper.writeValue(response.getWriter(), result);
                return;
            }
        } catch (NumberFormatException e) {
            result.put("success", false);
            result.put("message", "Units consumed must be a valid number");
            mapper.writeValue(response.getWriter(), result);
            return;
        }

        // Simple validation using utility
        ValidationUtil.ValidationResult validation = ValidationUtil.validateCustomer(
            accountNumber, fullName, telephone, address, unitsConsumed);

        if (!validation.isValid()) {
            result.put("success", false);
            result.put("message", validation.getFirstError());
            mapper.writeValue(response.getWriter(), result);
            return;
        }

        customer.setUnitsConsumed(unitsConsumed);

        boolean success = customerService.registerCustomer(customer);
        result.put("success", success);
        result.put("message", success ? "Customer registered successfully" : "Customer registration failed");

        mapper.writeValue(response.getWriter(), result);
    }
}
