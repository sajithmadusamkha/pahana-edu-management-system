package com.example.pahanaedubackend.controller;

import com.example.pahanaedubackend.factory.ResponseFactory;
import com.example.pahanaedubackend.factory.ServiceFactory;
import com.example.pahanaedubackend.model.Admin;
import com.example.pahanaedubackend.service.AdminService;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/register-admin")
public class AdminRegisterServlet  extends HttpServlet {
    private final AdminService adminService;
    private final ResponseFactory responseFactory;

    // Constructor using Factory Pattern
    public AdminRegisterServlet() {
        this.adminService = ServiceFactory.getInstance().getAdminService();
        this.responseFactory = ResponseFactory.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> data = mapper.readValue(request.getInputStream(), Map.class);

        Admin admin = new Admin();
        admin.setUsername(data.get("username"));
        admin.setFullName(data.get("fullName"));
        admin.setEmail(data.get("email"));

        String password = data.get("password");
        boolean success = adminService.registerAdmin(admin, password);

        Map<String, Object> result = responseFactory.createResponse(
            success,
            "Admin registered successfully",
            "Registration failed"
        );

        mapper.writeValue(response.getWriter(), result);
    }
}
