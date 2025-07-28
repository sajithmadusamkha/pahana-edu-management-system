package com.example.pahanaedubackend.controller;

import com.example.pahanaedubackend.factory.ResponseFactory;
import com.example.pahanaedubackend.factory.ServiceFactory;
import com.example.pahanaedubackend.factory.ValidationFactory;
import com.example.pahanaedubackend.model.Admin;
import com.example.pahanaedubackend.service.AdminService;
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

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final AdminService adminService;
    private final ResponseFactory responseFactory;
    private final ValidationFactory validationFactory;

    // Constructor using Factory Pattern
    public LoginServlet() {
        this.adminService = ServiceFactory.getInstance().getAdminService();
        this.responseFactory = ResponseFactory.getInstance();
        this.validationFactory = ValidationFactory.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> data = mapper.readValue(request.getInputStream(), Map.class);

        String username = data.get("username");
        String password = data.get("password");

        // Validation using factory
        ValidationUtil.ValidationResult validation = validationFactory.validateLogin(username, password);

        if (!validation.isValid()) {
            Map<String, Object> errorResponse = responseFactory.createValidationErrorResponse(
                validation.getFirstError(), validation.getErrors());
            mapper.writeValue(response.getWriter(), errorResponse);
            return;
        }

        Admin admin = adminService.login(username.trim(), password);
        Map<String, Object> result;

        if (admin != null) {
            HttpSession session = request.getSession(true);
            session.setAttribute("admin", admin);

            result = responseFactory.createSuccessResponse("Login successful");
            result.put("username", admin.getUsername());
        } else {
            result = responseFactory.createErrorResponse("Invalid username or password");
        }

        mapper.writeValue(response.getWriter(), result);
    }
}
