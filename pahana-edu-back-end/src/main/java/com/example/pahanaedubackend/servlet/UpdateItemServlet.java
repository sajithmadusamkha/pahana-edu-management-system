package com.example.pahanaedubackend.servlet;

import com.example.pahanaedubackend.facade.ServletFacade;
import com.example.pahanaedubackend.model.Item;
import com.example.pahanaedubackend.util.ValidationUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/items-update")
public class UpdateItemServlet extends HttpServlet {
    private final ServletFacade facade;

    // Constructor using Facade Pattern for simplified access
    public UpdateItemServlet() {
        this.facade = ServletFacade.getInstance();
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Initialize response and validate session using facade
        facade.initializeJsonResponse(response);

        if (!facade.validateAdminSession(request, response)) {
            return; // Response already written by facade
        }

        // Parse request data using facade (using reader for PUT requests)
        Item item = facade.parseJsonRequest(request, Item.class);

        // Validate item ID
        if (item.getId() <= 0) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            facade.writeStandardResponse(response, false, null, "Valid item ID is required");
            return;
        }

        // Validation using facade
        ValidationUtil.ValidationResult validation = facade.validateItem(
            item.getName(), item.getPrice(), item.getQuantity());

        if (!validation.isValid()) {
            facade.handleValidationErrors(response, validation);
            return;
        }

        // Trim and format data
        item.setName(item.getName().trim());

        // Update item using facade service access
        boolean updated = facade.getItemService().updateItem(item);

        // Write response using facade
        facade.writeStandardResponse(response, updated,
            "Item updated successfully",
            "Failed to update item");
    }
}
