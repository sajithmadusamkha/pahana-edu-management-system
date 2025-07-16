package com.example.pahanaedubackend.controller;

import com.example.pahanaedubackend.model.User;
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
    private final UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> loginData = mapper.readValue(request.getInputStream(), Map.class);

        String username = loginData.get("username");
        String password = loginData.get("password");

        User user = userService.authenticate(username, password);

        Map<String, Object> responseMap = new HashMap<>();
        if (user != null) {
            HttpSession session = request.getSession(true);
            session.setAttribute("username", user.getUsername());
            session.setAttribute("role", user.getRole());

            responseMap.put("success", true);
            responseMap.put("role", user.getRole());
            responseMap.put("message", "Login successful");
        } else {
            responseMap.put("success", false);
            responseMap.put("message", "Invalid username or password");
        }

        mapper.writeValue(response.getWriter(), responseMap);
    }
}
