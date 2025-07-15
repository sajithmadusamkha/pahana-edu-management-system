package com.example.pahanaedubackend.controller;

import com.example.pahanaedubackend.service.CustomerService;
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

@WebServlet("/login")
public class CustomerLoginServlet extends HttpServlet {

    private final CustomerService customerService = new CustomerService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Set response headers
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> loginRequest = mapper.readValue(request.getInputStream(), Map.class);

        String accountNumber = loginRequest.get("accountNumber");
        String password = loginRequest.get("password");

        boolean success = customerService.login(accountNumber, password);

        if (success) {
            HttpSession session = request.getSession(true);
            session.setAttribute("accountNumber", accountNumber);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "Login successful");

            mapper.writeValue(response.getWriter(), responseMap);
        } else {
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", false);
            responseMap.put("message", "Invalid account number or password");

            mapper.writeValue(response.getWriter(), responseMap);
        }
    }
}

