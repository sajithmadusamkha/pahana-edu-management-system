package com.example.pahanaedubackend.controller;

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
    private final AdminService adminService = new AdminService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> data = mapper.readValue(request.getInputStream(), Map.class);

        String username = data.get("username");
        String fullName = data.get("fullName");
        String password = data.get("password");

        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setFullName(fullName);

        boolean success = adminService.registerAdmin(admin, password);

        Map<String, Object> res = new HashMap<>();
        res.put("success", success);
        res.put("message", success ? "Admin registered successfully" : "Admin registration failed");

        mapper.writeValue(response.getWriter(), res);
    }
}
