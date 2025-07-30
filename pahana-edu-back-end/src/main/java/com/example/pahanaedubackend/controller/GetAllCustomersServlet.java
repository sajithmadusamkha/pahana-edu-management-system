package com.example.pahanaedubackend.controller;

import com.example.pahanaedubackend.facade.ControllerFacade;
import com.example.pahanaedubackend.model.Customer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/customers")
public class GetAllCustomersServlet extends HttpServlet {
    private final ControllerFacade facade;

    // Constructor using Facade Pattern for simplified access
    public GetAllCustomersServlet() {
        this.facade = ControllerFacade.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Initialize response and validate session using facade
        facade.initializeJsonResponse(response);

        if (!facade.validateAdminSession(request, response)) {
            return; // Response already written by facade
        }

        // Get customers using facade service access and write response
        List<Customer> customers = facade.getCustomerService().getAllCustomers();
        facade.writeJsonResponse(response, customers);
    }
}
