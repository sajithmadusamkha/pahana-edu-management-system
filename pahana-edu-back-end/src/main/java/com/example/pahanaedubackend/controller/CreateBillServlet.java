package com.example.pahanaedubackend.controller;

import com.example.pahanaedubackend.model.BillItem;
import com.example.pahanaedubackend.service.BillService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/bills-create")
public class CreateBillServlet extends HttpServlet {
    private final BillService billService = new BillService();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("admin") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"success\":false,\"message\":\"Unauthorized: Admin login required\"}");
            return;
        }

        try {
            // Parse JSON request body
            JsonNode jsonNode = mapper.readTree(request.getInputStream());
            String customerAccountNumber = jsonNode.get("customerAccountNumber").asText();
            List<BillItem> items = mapper.convertValue(jsonNode.get("items"),
                    mapper.getTypeFactory().constructCollectionType(List.class, BillItem.class));

            // Simple validation
            // Validate customer account number
            if (customerAccountNumber == null || customerAccountNumber.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"success\":false,\"message\":\"Customer account number is required\"}");
                return;
            }

            if (customerAccountNumber.trim().length() < 6 || customerAccountNumber.trim().length() > 12) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"success\":false,\"message\":\"Customer account number must be between 6 and 12 characters\"}");
                return;
            }

            // Validate items list
            if (items == null || items.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"success\":false,\"message\":\"At least one item is required for the bill\"}");
                return;
            }

            if (items.size() > 50) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"success\":false,\"message\":\"Cannot add more than 50 items to a single bill\"}");
                return;
            }

            // Validate each bill item
            for (int i = 0; i < items.size(); i++) {
                BillItem item = items.get(i);

                if (item.getItemId() <= 0) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"success\":false,\"message\":\"Invalid item ID at position " + (i + 1) + "\"}");
                    return;
                }

                if (item.getQuantity() <= 0) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"success\":false,\"message\":\"Item quantity must be greater than 0 at position " + (i + 1) + "\"}");
                    return;
                }

                if (item.getQuantity() > 1000) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"success\":false,\"message\":\"Item quantity cannot exceed 1,000 at position " + (i + 1) + "\"}");
                    return;
                }
            }

            // Call service
            int billId = billService.createBill(customerAccountNumber.trim().toUpperCase(), items);

            if (billId > 0) {
                response.getWriter().write("{\"success\":true,\"message\":\"Bill created and stock updated\",\"billId\":" + billId + "}");
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"success\":false,\"message\":\"Insufficient stock or error occurred\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\":false,\"message\":\"Server error\"}");
        }
    }
}
