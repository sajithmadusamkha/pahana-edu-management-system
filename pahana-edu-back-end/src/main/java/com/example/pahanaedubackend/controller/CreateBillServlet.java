package com.example.pahanaedubackend.controller;

import com.example.pahanaedubackend.factory.ResponseFactory;
import com.example.pahanaedubackend.factory.ServiceFactory;
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
import java.util.Map;

@WebServlet("/bills-create")
public class CreateBillServlet extends HttpServlet {
    private final BillService billService;
    private final ResponseFactory responseFactory;
    private final ObjectMapper mapper = new ObjectMapper();

    // Constructor using Factory Pattern
    public CreateBillServlet() {
        this.billService = ServiceFactory.getInstance().getBillService();
        this.responseFactory = ResponseFactory.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("admin") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            Map<String, Object> errorResponse = responseFactory.createUnauthorizedResponse();
            mapper.writeValue(response.getWriter(), errorResponse);
            return;
        }

        try {
            // Parse JSON request body
            JsonNode jsonNode = mapper.readTree(request.getInputStream());
            String customerAccountNumber = jsonNode.get("customerAccountNumber").asText();
            List<BillItem> items = mapper.convertValue(jsonNode.get("items"),
                    mapper.getTypeFactory().constructCollectionType(List.class, BillItem.class));

            // Validate customer account number
            if (customerAccountNumber == null || customerAccountNumber.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                Map<String, Object> errorResponse = responseFactory.createErrorResponse("Customer account number is required");
                mapper.writeValue(response.getWriter(), errorResponse);
                return;
            }

            if (customerAccountNumber.trim().length() < 6 || customerAccountNumber.trim().length() > 12) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                Map<String, Object> errorResponse = responseFactory.createErrorResponse("Customer account number must be between 6 and 12 characters");
                mapper.writeValue(response.getWriter(), errorResponse);
                return;
            }

            // Validate items list
            if (items == null || items.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                Map<String, Object> errorResponse = responseFactory.createErrorResponse("At least one item is required for the bill");
                mapper.writeValue(response.getWriter(), errorResponse);
                return;
            }

            if (items.size() > 50) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                Map<String, Object> errorResponse = responseFactory.createErrorResponse("Cannot add more than 50 items to a single bill");
                mapper.writeValue(response.getWriter(), errorResponse);
                return;
            }

            // Validate each bill item
            for (int i = 0; i < items.size(); i++) {
                BillItem item = items.get(i);

                if (item.getItemId() <= 0) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    Map<String, Object> errorResponse = responseFactory.createErrorResponse("Invalid item ID at position " + (i + 1));
                    mapper.writeValue(response.getWriter(), errorResponse);
                    return;
                }

                if (item.getQuantity() <= 0) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    Map<String, Object> errorResponse = responseFactory.createErrorResponse("Item quantity must be greater than 0 at position " + (i + 1));
                    mapper.writeValue(response.getWriter(), errorResponse);
                    return;
                }

                if (item.getQuantity() > 1000) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    Map<String, Object> errorResponse = responseFactory.createErrorResponse("Item quantity cannot exceed 1,000 at position " + (i + 1));
                    mapper.writeValue(response.getWriter(), errorResponse);
                    return;
                }
            }

            // Call service
            int billId = billService.createBill(customerAccountNumber.trim().toUpperCase(), items);

            if (billId > 0) {
                Map<String, Object> successResponse = responseFactory.createSuccessResponse("Bill created and stock updated");
                successResponse.put("billId", billId);
                mapper.writeValue(response.getWriter(), successResponse);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                Map<String, Object> errorResponse = responseFactory.createErrorResponse("Insufficient stock or error occurred");
                mapper.writeValue(response.getWriter(), errorResponse);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, Object> errorResponse = responseFactory.createErrorResponse("Server error");
            mapper.writeValue(response.getWriter(), errorResponse);
        }
    }
}
