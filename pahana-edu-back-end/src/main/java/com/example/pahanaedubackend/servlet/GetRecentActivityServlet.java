package com.example.pahanaedubackend.servlet;

import com.example.pahanaedubackend.facade.ServletFacade;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/recent-activity")
public class GetRecentActivityServlet extends HttpServlet {
    private final ServletFacade facade;

    // Constructor using Facade Pattern for simplified access
    public GetRecentActivityServlet() {
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

        // Get limit parameter, default to 10
        int limit = 10;
        String limitParam = request.getParameter("limit");
        if (limitParam != null) {
            try {
                limit = Integer.parseInt(limitParam);
                if (limit < 1 || limit > 50) {
                    limit = 10; // Default to 10 if out of range
                }
            } catch (NumberFormatException e) {
                limit = 10; // Default to 10 if invalid
            }
        }

        // Get recent activity data from different sources
        List<Map<String, Object>> recentBills = facade.getBillService().getRecentBills(limit / 3);
        List<Map<String, Object>> recentCustomers = facade.getCustomerService().getRecentCustomers(limit / 3);
        List<Map<String, Object>> recentItems = facade.getItemService().getRecentItems(limit / 3);
        
        // Combine all activities into a single list
        List<Map<String, Object>> activities = new ArrayList<>();
        
        // Add recent bills
        for (Map<String, Object> bill : recentBills) {
            Map<String, Object> activity = new HashMap<>();
            activity.put("type", "order");
            activity.put("icon", "bi-receipt");
            activity.put("color", "text-success");
            activity.put("message", "Order #" + bill.get("id") + " placed for $" + 
                String.format("%.2f", (Double) bill.get("totalAmount")));
            activity.put("details", "Customer: " + bill.get("customerName"));
            activity.put("timestamp", bill.get("billDate"));
            activities.add(activity);
        }
        
        // Add recent customers
        for (Map<String, Object> customer : recentCustomers) {
            Map<String, Object> activity = new HashMap<>();
            activity.put("type", "customer");
            activity.put("icon", "bi-person-plus");
            activity.put("color", "text-primary");
            activity.put("message", "Customer " + customer.get("fullName") + " registered");
            activity.put("details", "Account: " + customer.get("accountNumber"));
            activity.put("timestamp", System.currentTimeMillis()); // Placeholder timestamp
            activities.add(activity);
        }
        
        // Add recent items
        for (Map<String, Object> item : recentItems) {
            Map<String, Object> activity = new HashMap<>();
            activity.put("type", "item");
            activity.put("icon", "bi-box-seam");
            activity.put("color", "text-info");
            activity.put("message", "Item \"" + item.get("name") + "\" added to inventory");
            activity.put("details", "Price: $" + String.format("%.2f", (Double) item.get("price")) + 
                ", Stock: " + item.get("quantity"));
            activity.put("timestamp", System.currentTimeMillis()); // Placeholder timestamp
            activities.add(activity);
        }
        
        // Create response with activity data
        Map<String, Object> result = new HashMap<>();
        result.put("activities", activities);
        result.put("limit", limit);
        
        // Write response using facade
        facade.writeJsonResponse(response, result);
    }
}
