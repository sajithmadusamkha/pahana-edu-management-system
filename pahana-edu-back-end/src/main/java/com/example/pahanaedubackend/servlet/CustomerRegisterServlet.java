package com.example.pahanaedubackend.servlet;

import com.example.pahanaedubackend.facade.ServletFacade;
import com.example.pahanaedubackend.model.Customer;
import com.example.pahanaedubackend.util.ValidationUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/register-customer")
public class CustomerRegisterServlet extends HttpServlet {
    private final ServletFacade facade;

    // Constructor using Facade Pattern for simplified access
    public CustomerRegisterServlet() {
        this.facade = ServletFacade.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Initialize response and validate session using facade
        facade.initializeJsonResponse(response);

        if (!facade.validateAdminSession(request, response)) {
            return; // Response already written by facade
        }

        // Parse request data using facade
        Map<String, Object> data = facade.parseJsonRequest(request, Map.class);

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
                facade.writeStandardResponse(response, false, null, "Units consumed must be a valid number");
                return;
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            facade.writeStandardResponse(response, false, null, "Units consumed must be a valid number");
            return;
        }

        // Validation using facade
        ValidationUtil.ValidationResult validation = facade.validateCustomer(
            accountNumber, fullName, telephone, address, unitsConsumed);

        if (!validation.isValid()) {
            facade.handleValidationErrors(response, validation);
            return;
        }

        customer.setUnitsConsumed(unitsConsumed);

        // Register customer using facade service access
        boolean success = facade.getCustomerService().registerCustomer(customer);

        // Write response using facade
        facade.writeStandardResponse(response, success,
            "Customer registered successfully",
            "Customer registration failed");
    }
}
