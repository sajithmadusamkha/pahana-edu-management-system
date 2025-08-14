package com.example.pahanaedubackend.servlet;

import com.example.pahanaedubackend.facade.ServletFacade;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/sales-chart-data")
public class GetSalesChartDataServlet extends HttpServlet {
    private final ServletFacade facade;

    // Constructor using Facade Pattern for simplified access
    public GetSalesChartDataServlet() {
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

        // Get days parameter, default to 7 days
        int days = 7;
        String daysParam = request.getParameter("days");
        if (daysParam != null) {
            try {
                days = Integer.parseInt(daysParam);
                if (days < 1 || days > 30) {
                    days = 7; // Default to 7 if out of range
                }
            } catch (NumberFormatException e) {
                days = 7; // Default to 7 if invalid
            }
        }

        // Get sales data using facade service access
        List<Map<String, Object>> salesData = facade.getBillService().getDailySalesData(days);
        
        // Create response with sales data
        Map<String, Object> result = new HashMap<>();
        result.put("salesData", salesData);
        result.put("days", days);
        
        // Write response using facade
        facade.writeJsonResponse(response, result);
    }
}
