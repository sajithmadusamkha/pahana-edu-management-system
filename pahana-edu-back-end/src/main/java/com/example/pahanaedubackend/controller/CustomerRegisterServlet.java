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

@WebServlet("/register-customer")
public class CustomerRegisterServlet extends HttpServlet {
    private final CustomerService customerService;
    private final IResponseFactory responseFactory;
    private final IValidationFactory validationFactory;

    // Constructor using Standard Factory Pattern with Interfaces
    public CustomerRegisterServlet() {
        FactoryProvider provider = FactoryProvider.getInstance();
        this.customerService = provider.getServiceFactory().getCustomerService();
        this.responseFactory = provider.getResponseFactory();
        this.validationFactory = provider.getValidationFactory();
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
        Map<String, Object> data = mapper.readValue(request.getInputStream(), Map.class);

        String accountNumber = (String) data.get("accountNumber");
        String fullName = (String) data.get("fullName");
        String telephone = (String) data.get("telephone");
        String address = (String) data.get("address");

        Customer customer = new Customer();
        customer.setAccountNumber(accountNumber.trim().toUpperCase());
        customer.setFullName(fullName.trim());
        customer.setTelephone(telephone.trim());
        customer.setAddress(address.trim());

        // Handle unitsConsumed which can be either Integer or String
        Object unitsConsumedObj = data.get("unitsConsumed");
        int unitsConsumed;

        try {
            if (unitsConsumedObj instanceof Integer) {
                unitsConsumed = (Integer) unitsConsumedObj;
            } else if (unitsConsumedObj instanceof String) {
                unitsConsumed = Integer.parseInt((String) unitsConsumedObj);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                Map<String, Object> errorResponse = responseFactory.createErrorResponse("Units consumed must be a valid number");
                mapper.writeValue(response.getWriter(), errorResponse);
                return;
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Map<String, Object> errorResponse = responseFactory.createErrorResponse("Units consumed must be a valid number");
            mapper.writeValue(response.getWriter(), errorResponse);
            return;
        }

        // Validation using factory
        ValidationUtil.ValidationResult validation = validationFactory.validateCustomer(
            accountNumber, fullName, telephone, address, unitsConsumed);

        if (!validation.isValid()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Map<String, Object> errorResponse = responseFactory.createValidationErrorResponse(
                validation.getFirstError(), validation.getErrors());
            mapper.writeValue(response.getWriter(), errorResponse);
            return;
        }

        customer.setUnitsConsumed(unitsConsumed);

        boolean success = customerService.registerCustomer(customer);
        Map<String, Object> result = responseFactory.createResponse(
            success,
            "Customer registered successfully",
            "Customer registration failed"
        );

        mapper.writeValue(response.getWriter(), result);
    }
}
