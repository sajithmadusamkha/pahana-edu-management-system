package com.example.pahanaedubackend.controller;

import com.example.pahanaedubackend.model.Admin;
import com.example.pahanaedubackend.model.User;
import com.example.pahanaedubackend.service.AdminService;
import com.example.pahanaedubackend.service.UserService;
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
public class LoginServlet extends HttpServlet {
    private final AdminService adminService = new AdminService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> data = mapper.readValue(request.getInputStream(), Map.class);

        String username = data.get("username");
        String password = data.get("password");

        Admin admin = adminService.login(username, password);

        Map<String, Object> result = new HashMap<>();
        if (admin != null) {
            HttpSession session = request.getSession(true);
            session.setAttribute("admin", admin);

            result.put("success", true);
            result.put("message", "Login successful");
            result.put("username", admin.getUsername());
        } else {
            result.put("success", false);
            result.put("message", "Invalid username or password");
        }

        mapper.writeValue(response.getWriter(), result);
    }
}
