package com.example.pahanaedubackend.controller;

import com.example.pahanaedubackend.HelloServlet;
import com.example.pahanaedubackend.service.CustomerService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/register-customer")
public class CustomerRegisterServlet extends HelloServlet {
    private final CustomerService customerService = new CustomerService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accountNumber = request.getParameter("accountNumber");
        String name = request.getParameter("name");
        String address = request.getParameter("address");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");

        boolean success = customerService.registerCustomer(accountNumber, name, address, phone, password);

        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        if (success) {
            response.getWriter().println("Customer registered successfully.");
        } else {
            response.getWriter().println("Failed to register customer.");
        }
    }
}
