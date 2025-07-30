package com.example.pahanaedubackend.controller;

import com.example.pahanaedubackend.factory.impl.FactoryProvider;
import com.example.pahanaedubackend.factory.IResponseFactory;
import com.example.pahanaedubackend.factory.IValidationFactory;
import com.example.pahanaedubackend.model.Customer;
import com.example.pahanaedubackend.service.CustomerService;
import com.example.pahanaedubackend.util.ValidationUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

@WebServlet("/update-customer")
public class UpdateCustomerServlet extends HttpServlet {
    private final CustomerService customerService;
    private final IResponseFactory responseFactory;
    private final IValidationFactory validationFactory;

    // Constructor using Standard Factory Pattern with Interfaces
    public UpdateCustomerServlet() {
        FactoryProvider provider = FactoryProvider.getInstance();
        this.customerService = provider.getServiceFactory().getCustomerService();
        this.responseFactory = provider.getResponseFactory();
        this.validationFactory = provider.getValidationFactory();
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
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
        Customer customer = mapper.readValue(request.getInputStream(), Customer.class);

        // Validate customer ID
        if (customer.getId() <= 0) {
            Map<String, Object> errorResponse = responseFactory.createErrorResponse("Valid customer ID is required");
            mapper.writeValue(response.getWriter(), errorResponse);
            return;
        }

        // Validation using factory
        ValidationUtil.ValidationResult validation = validationFactory.validateCustomer(
            customer.getAccountNumber(), customer.getFullName(),
            customer.getTelephone(), customer.getAddress(), customer.getUnitsConsumed());

        if (!validation.isValid()) {
            Map<String, Object> errorResponse = responseFactory.createValidationErrorResponse(
                validation.getFirstError(), validation.getErrors());
            mapper.writeValue(response.getWriter(), errorResponse);
            return;
        }

        // Trim and format data
        customer.setAccountNumber(customer.getAccountNumber().trim().toUpperCase());
        customer.setFullName(customer.getFullName().trim());
        customer.setTelephone(customer.getTelephone().trim());
        customer.setAddress(customer.getAddress().trim());

        boolean success = customerService.updateCustomer(customer);
        Map<String, Object> result = responseFactory.createResponse(
            success,
            "Customer updated successfully",
            "Customer update failed"
        );
        mapper.writeValue(response.getWriter(), result);
    }
}
