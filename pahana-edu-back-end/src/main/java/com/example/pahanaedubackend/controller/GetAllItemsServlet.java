package com.example.pahanaedubackend.controller;

import com.example.pahanaedubackend.factory.ResponseFactory;
import com.example.pahanaedubackend.factory.ServiceFactory;
import com.example.pahanaedubackend.model.Item;
import com.example.pahanaedubackend.service.ItemService;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/items")
public class GetAllItemsServlet extends HttpServlet {
    private final ItemService itemService;
    private final ResponseFactory responseFactory;

    // Constructor using Factory Pattern
    public GetAllItemsServlet() {
        this.itemService = ServiceFactory.getInstance().getItemService();
        this.responseFactory = ResponseFactory.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
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

        List<Item> items = itemService.getAllItems();

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getWriter(), items);
    }
}
