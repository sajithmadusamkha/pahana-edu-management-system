package com.example.pahanaedubackend.controller;

import com.example.pahanaedubackend.factory.ResponseFactory;
import com.example.pahanaedubackend.factory.ServiceFactory;
import com.example.pahanaedubackend.factory.ValidationFactory;
import com.example.pahanaedubackend.model.Item;
import com.example.pahanaedubackend.service.ItemService;
import com.example.pahanaedubackend.util.ValidationUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/create-item")
public class CreateItemServlet extends HttpServlet {
    private final ItemService itemService;
    private final ResponseFactory responseFactory;
    private final ValidationFactory validationFactory;

    // Constructor using Factory Pattern
    public CreateItemServlet() {
        this.itemService = ServiceFactory.getInstance().getItemService();
        this.responseFactory = ResponseFactory.getInstance();
        this.validationFactory = ValidationFactory.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("admin") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            Map<String, Object> errorResponse = responseFactory.createUnauthorizedResponse();
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getWriter(), errorResponse);
            return;
        }

        ObjectMapper mapper = new ObjectMapper();
        Item item = mapper.readValue(request.getInputStream(), Item.class);

        // Validation using factory
        ValidationUtil.ValidationResult validation = validationFactory.validateItem(
            item.getName(), item.getPrice(), item.getQuantity());

        if (!validation.isValid()) {
            Map<String, Object> errorResponse = responseFactory.createValidationErrorResponse(
                validation.getFirstError(), validation.getErrors());
            mapper.writeValue(response.getWriter(), errorResponse);
            return;
        }

        // Trim and format data
        item.setName(item.getName().trim());

        boolean success = itemService.createItem(item);
        Map<String, Object> result = responseFactory.createResponse(
            success,
            "Item created successfully",
            "Item creation failed"
        );
        mapper.writeValue(response.getWriter(), result);
    }
}
