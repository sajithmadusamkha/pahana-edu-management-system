package com.example.pahanaedubackend.controller;

import com.example.pahanaedubackend.facade.ControllerFacade;
import com.example.pahanaedubackend.model.Item;
import com.example.pahanaedubackend.util.ValidationUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/create-item")
public class CreateItemServlet extends HttpServlet {
    private final ControllerFacade facade;

    // Constructor using Facade Pattern for simplified access
    public CreateItemServlet() {
        this.facade = ControllerFacade.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Initialize response and validate session using facade
        facade.initializeJsonResponse(response);

        if (!facade.validateAdminSession(request, response)) {
            return; // Response already written by facade
        }

        // Parse request data using facade
        Item item = facade.parseJsonRequest(request, Item.class);

        // Validation using facade
        ValidationUtil.ValidationResult validation = facade.validateItem(
            item.getName(), item.getPrice(), item.getQuantity());

        if (!validation.isValid()) {
            facade.handleValidationErrors(response, validation);
            return;
        }

        // Trim and format data
        item.setName(item.getName().trim());

        // Create item using facade service access
        boolean success = facade.getItemService().createItem(item);

        // Write response using facade
        facade.writeStandardResponse(response, success,
            "Item created successfully",
            "Item creation failed");
    }
}
