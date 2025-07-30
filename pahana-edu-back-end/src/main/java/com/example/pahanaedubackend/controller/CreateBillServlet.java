package com.example.pahanaedubackend.controller;

import com.example.pahanaedubackend.facade.ControllerFacade;
import com.example.pahanaedubackend.model.BillItem;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/bills-create")
public class CreateBillServlet extends HttpServlet {
    private final ControllerFacade facade;
    private final ObjectMapper mapper = new ObjectMapper();

    // Constructor using Facade Pattern for simplified access
    public CreateBillServlet() {
        this.facade = ControllerFacade.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        // Initialize response and validate session using facade
        facade.initializeJsonResponse(response);

        if (!facade.validateAdminSession(request, response)) {
            return; // Response already written by facade
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
                facade.writeStandardResponse(response, false, null, "Customer account number is required");
                return;
            }

            if (customerAccountNumber.trim().length() < 6 || customerAccountNumber.trim().length() > 12) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                facade.writeStandardResponse(response, false, null, "Customer account number must be between 6 and 12 characters");
                return;
            }

            // Validate items list
            if (items == null || items.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                facade.writeStandardResponse(response, false, null, "At least one item is required for the bill");
                return;
            }

            if (items.size() > 50) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                facade.writeStandardResponse(response, false, null, "Cannot add more than 50 items to a single bill");
                return;
            }

            // Validate each bill item
            for (int i = 0; i < items.size(); i++) {
                BillItem item = items.get(i);

                if (item.getItemId() <= 0) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    facade.writeStandardResponse(response, false, null, "Invalid item ID at position " + (i + 1));
                    return;
                }

                if (item.getQuantity() <= 0) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    facade.writeStandardResponse(response, false, null, "Item quantity must be greater than 0 at position " + (i + 1));
                    return;
                }

                if (item.getQuantity() > 1000) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    facade.writeStandardResponse(response, false, null, "Item quantity cannot exceed 1,000 at position " + (i + 1));
                    return;
                }
            }

            // Call service using facade
            int billId = facade.getBillService().createBill(customerAccountNumber.trim().toUpperCase(), items);

            if (billId > 0) {
                Map<String, Object> successResponse = facade.getResponseFactory().createSuccessResponse("Bill created and stock updated");
                successResponse.put("billId", billId);
                facade.writeJsonResponse(response, successResponse);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                facade.writeStandardResponse(response, false, null, "Insufficient stock or error occurred");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            facade.writeStandardResponse(response, false, null, "Server error");
        }
    }
}
