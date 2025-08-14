package com.example.pahanaedubackend.servlet;

import com.example.pahanaedubackend.facade.ServletFacade;
import com.example.pahanaedubackend.model.Admin;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/register-admin")
public class AdminRegisterServlet extends HttpServlet {
    private final ServletFacade facade;

    // Constructor using Facade Pattern for simplified access
    public AdminRegisterServlet() {
        this.facade = ServletFacade.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Initialize response using facade
        facade.initializeJsonResponse(response);

        // Parse request data using facade
        Map<String, String> data = facade.parseJsonRequest(request);

        Admin admin = new Admin();
        admin.setUsername(data.get("username"));
        admin.setFullName(data.get("fullName"));
        admin.setEmail(data.get("email"));

        String password = data.get("password");

        // Register admin using facade service access
        boolean success = facade.getAdminService().registerAdmin(admin, password);

        // Write response using facade
        facade.writeStandardResponse(response, success,
            "Admin registered successfully",
            "Registration failed");
    }
}
