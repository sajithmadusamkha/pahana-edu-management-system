package com.example.pahanaedubackend.controller;

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

@WebServlet("/items-update")
public class UpdateItemServlet extends HttpServlet {
    private final ItemService itemService = new ItemService();

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("admin") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"success\":false,\"message\":\"Unauthorized: Admin login required\"}");
            return;
        }

        ObjectMapper mapper = new ObjectMapper();
        Item item = mapper.readValue(request.getReader(), Item.class);

        // Simple validation
        // Validate item ID
        if (item.getId() <= 0) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\":false,\"message\":\"Valid item ID is required\"}");
            return;
        }

        // Validate using utility
        ValidationUtil.ValidationResult validation = ValidationUtil.validateItem(
            item.getName(), item.getPrice(), item.getQuantity());

        if (!validation.isValid()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\":false,\"message\":\"" + validation.getFirstError() + "\"}");
            return;
        }

        // Trim and format data
        item.setName(item.getName().trim());

        boolean updated = itemService.updateItem(item);

        if (updated) {
            response.getWriter().write("{\"success\":true,\"message\":\"Item updated successfully\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\":false,\"message\":\"Failed to update item\"}");
        }
    }
}
