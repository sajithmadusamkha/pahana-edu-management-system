package com.example.pahanaedubackend.controller;

import com.example.pahanaedubackend.facade.ControllerFacade;
import com.example.pahanaedubackend.model.Admin;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final ControllerFacade facade;

    // Constructor using Facade Pattern for simplified access
    public LoginServlet() {
        this.facade = ControllerFacade.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Initialize response using facade
        facade.initializeJsonResponse(response);

        // Process login using facade - all complexity is hidden
        Admin admin = facade.processLogin(request, response);

        // Add username to response if login successful
        if (admin != null) {
            // Note: The facade already handles session creation and response writing
            // We could extend the facade to handle additional response data if needed
        }
    }
}
