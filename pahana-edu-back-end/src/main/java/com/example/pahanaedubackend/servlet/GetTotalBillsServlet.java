package com.example.pahanaedubackend.servlet;

import com.example.pahanaedubackend.facade.ServletFacade;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/total-bills")
public class GetTotalBillsServlet extends HttpServlet {
    private final ServletFacade facade;

    // Constructor using Facade Pattern for simplified access
    public GetTotalBillsServlet() {
        this.facade = ServletFacade.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Initialize response and validate session using facade
        facade.initializeJsonResponse(response);

        if (!facade.validateAdminSession(request, response)) {
            return; // Response already written by facade
        }

        // Get total bills count using facade service access
        int totalBills = facade.getBillService().getTotalBillsCount();
        
        // Create response with total count
        Map<String, Object> result = new HashMap<>();
        result.put("totalBills", totalBills);
        
        // Write response using facade
        facade.writeJsonResponse(response, result);
    }
}
