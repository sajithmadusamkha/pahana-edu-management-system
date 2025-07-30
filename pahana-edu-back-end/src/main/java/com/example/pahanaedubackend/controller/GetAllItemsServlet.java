package com.example.pahanaedubackend.controller;

import com.example.pahanaedubackend.facade.ControllerFacade;
import com.example.pahanaedubackend.model.Item;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/items")
public class GetAllItemsServlet extends HttpServlet {
    private final ControllerFacade facade;

    // Constructor using Facade Pattern for simplified access
    public GetAllItemsServlet() {
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

        // Get items using facade service access and write response
        List<Item> items = facade.getItemService().getAllItems();
        facade.writeJsonResponse(response, items);
    }
}
