package com.example.pahanaedubackend.controller;

import com.example.pahanaedubackend.factory.impl.FactoryProvider;
import com.example.pahanaedubackend.factory.IResponseFactory;
import com.example.pahanaedubackend.service.ItemService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/item-delete")
public class DeleteItemServlet extends HttpServlet {
    private final ItemService itemService;
    private final IResponseFactory responseFactory;

    // Constructor using Standard Factory Pattern with Interfaces
    public DeleteItemServlet() {
        FactoryProvider provider = FactoryProvider.getInstance();
        this.itemService = provider.getServiceFactory().getItemService();
        this.responseFactory = provider.getResponseFactory();
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("admin") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"success\":false,\"message\":\"Unauthorized: Admin login required\"}");
            return;
        }

        int itemId;
        try {
            itemId = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\":false,\"message\":\"Invalid item ID\"}");
            return;
        }

        // Check if item is used in bills before attempting deletion
        if (itemService.isItemUsedInBills(itemId)) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            response.getWriter().write("{\"success\":false,\"message\":\"Cannot delete item: This item is used in existing bills. Please remove it from all bills before deletion.\"}");
            return;
        }

        boolean deleted = itemService.deleteItem(itemId);

        if (deleted) {
            response.getWriter().write("{\"success\":true,\"message\":\"Item deleted successfully\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("{\"success\":false,\"message\":\"Item not found or delete failed\"}");
        }
    }
}
