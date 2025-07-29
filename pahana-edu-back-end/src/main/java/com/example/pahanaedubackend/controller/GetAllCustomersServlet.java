package com.example.pahanaedubackend.controller;

import com.example.pahanaedubackend.factory.impl.FactoryProvider;
import com.example.pahanaedubackend.factory.IResponseFactory;
import com.example.pahanaedubackend.model.Customer;
import com.example.pahanaedubackend.service.CustomerService;
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

@WebServlet("/customers")
public class GetAllCustomersServlet extends HttpServlet {
    private final CustomerService customerService;
    private final IResponseFactory responseFactory;

    // Constructor using Standard Factory Pattern with Interfaces
    public GetAllCustomersServlet() {
        FactoryProvider provider = FactoryProvider.getInstance();
        this.customerService = provider.getServiceFactory().getCustomerService();
        this.responseFactory = provider.getResponseFactory();
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

        List<Customer> customers = customerService.getAllCustomers();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getWriter(), customers);
    }
}
