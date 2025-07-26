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
import java.util.HashMap;
import java.util.Map;

@WebServlet("/create-item")
public class CreateItemServlet extends HttpServlet {
    private final ItemService itemService = new ItemService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
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
        Item item = mapper.readValue(request.getInputStream(), Item.class);

        // Simple validation using utility
        ValidationUtil.ValidationResult validation = ValidationUtil.validateItem(
            item.getName(), item.getPrice(), item.getQuantity());

        Map<String, Object> result = new HashMap<>();

        if (!validation.isValid()) {
            result.put("success", false);
            result.put("message", validation.getFirstError());
            mapper.writeValue(response.getWriter(), result);
            return;
        }

        // Trim and format data
        item.setName(item.getName().trim());

        boolean success = itemService.createItem(item);
        result.put("success", success);
        result.put("message", success ? "Item created successfully" : "Item creation failed");
        mapper.writeValue(response.getWriter(), result);
    }
}
