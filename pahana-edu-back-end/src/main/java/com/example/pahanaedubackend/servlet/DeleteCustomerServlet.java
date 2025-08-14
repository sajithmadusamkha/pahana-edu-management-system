package com.example.pahanaedubackend.servlet;

import com.example.pahanaedubackend.factory.impl.FactoryProvider;
import com.example.pahanaedubackend.factory.IResponseFactory;
import com.example.pahanaedubackend.service.CustomerService;
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

@WebServlet("/delete-customer")
public class DeleteCustomerServlet extends HttpServlet {
    private final CustomerService customerService;
    private final IResponseFactory responseFactory;

    // Constructor using Standard Factory Pattern with Interfaces
    public DeleteCustomerServlet() {
        FactoryProvider provider = FactoryProvider.getInstance();
        this.customerService = provider.getServiceFactory().getCustomerService();
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

        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> data = mapper.readValue(request.getInputStream(), Map.class);
        String accountNumber = data.get("accountNumber");

        boolean success = customerService.deleteCustomer(accountNumber);

        Map<String, Object> result = new HashMap<>();
        result.put("success", success);
        result.put("message", success ? "Customer deleted successfully" : "Customer deletion failed");
        mapper.writeValue(response.getWriter(), result);
    }
}
