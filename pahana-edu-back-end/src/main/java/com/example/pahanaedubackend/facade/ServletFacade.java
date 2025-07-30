package com.example.pahanaedubackend.facade;

import com.example.pahanaedubackend.factory.impl.FactoryProvider;
import com.example.pahanaedubackend.factory.IResponseFactory;
import com.example.pahanaedubackend.factory.IServiceFactory;
import com.example.pahanaedubackend.factory.IValidationFactory;
import com.example.pahanaedubackend.model.Admin;
import com.example.pahanaedubackend.service.AdminService;
import com.example.pahanaedubackend.service.BillService;
import com.example.pahanaedubackend.service.CustomerService;
import com.example.pahanaedubackend.service.ItemService;
import com.example.pahanaedubackend.util.ValidationUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

/**
 * ServletFacade implements the Facade Design Pattern to provide a simplified interface
 * for servlet controllers to interact with the complex subsystems of the application.
 * 
 * This facade encapsulates:
 * - Factory management and service access
 * - Session validation and authentication
 * - Request/Response handling and JSON operations
 * - Validation coordination
 * - Error handling and response formatting
 * 
 * Benefits of using Facade Pattern here:
 * - Simplifies controller code by hiding complex subsystem interactions
 * - Reduces code duplication across servlets
 * - Provides a single point of access for common operations
 * - Makes the system easier to maintain and modify
 * - Improves testability by centralizing common logic
 */
public class ServletFacade {
    
    // Singleton instance
    private static ServletFacade instance;
    
    // Factory dependencies
    private final IServiceFactory serviceFactory;
    private final IResponseFactory responseFactory;
    private final IValidationFactory validationFactory;
    
    // JSON mapper for request/response handling
    private final ObjectMapper objectMapper;
    
    /**
     * Private constructor to prevent direct instantiation
     */
    private ServletFacade() {
        FactoryProvider provider = FactoryProvider.getInstance();
        this.serviceFactory = provider.getServiceFactory();
        this.responseFactory = provider.getResponseFactory();
        this.validationFactory = provider.getValidationFactory();
        this.objectMapper = new ObjectMapper();
    }
    
    /**
     * Get the singleton instance of ServletFacade
     * Thread-safe implementation using double-checked locking
     * 
     * @return ServletFacade instance
     */
    public static ServletFacade getInstance() {
        if (instance == null) {
            synchronized (ServletFacade.class) {
                if (instance == null) {
                    instance = new ServletFacade();
                }
            }
        }
        return instance;
    }
    
    /**
     * Initialize HTTP response with standard JSON settings
     * 
     * @param response HttpServletResponse to initialize
     */
    public void initializeJsonResponse(HttpServletResponse response) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
    }
    
    /**
     * Validate admin session and return appropriate response if invalid
     * 
     * @param request HttpServletRequest containing session
     * @param response HttpServletResponse for error response
     * @return true if session is valid, false if invalid (response already written)
     * @throws IOException if response writing fails
     */
    public boolean validateAdminSession(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("admin") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            Map<String, Object> errorResponse = responseFactory.createUnauthorizedResponse();
            objectMapper.writeValue(response.getWriter(), errorResponse);
            return false;
        }
        return true;
    }
    
    /**
     * Parse JSON request body to Map
     * 
     * @param request HttpServletRequest containing JSON data
     * @return Map containing parsed JSON data
     * @throws IOException if parsing fails
     */
    public Map<String, String> parseJsonRequest(HttpServletRequest request) throws IOException {
        return objectMapper.readValue(request.getInputStream(), Map.class);
    }
    
    /**
     * Parse JSON request body to specific object type
     * 
     * @param request HttpServletRequest containing JSON data
     * @param valueType Class type to parse to
     * @param <T> Type parameter
     * @return Parsed object of specified type
     * @throws IOException if parsing fails
     */
    public <T> T parseJsonRequest(HttpServletRequest request, Class<T> valueType) throws IOException {
        return objectMapper.readValue(request.getInputStream(), valueType);
    }
    
    /**
     * Write JSON response
     * 
     * @param response HttpServletResponse to write to
     * @param data Data to serialize as JSON
     * @throws IOException if writing fails
     */
    public void writeJsonResponse(HttpServletResponse response, Object data) throws IOException {
        objectMapper.writeValue(response.getWriter(), data);
    }
    
    /**
     * Handle validation errors and write appropriate response
     * 
     * @param response HttpServletResponse to write to
     * @param validation ValidationResult containing errors
     * @throws IOException if writing fails
     */
    public void handleValidationErrors(HttpServletResponse response, ValidationUtil.ValidationResult validation) 
            throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        Map<String, Object> errorResponse = responseFactory.createValidationErrorResponse(
            validation.getFirstError(), validation.getErrors());
        writeJsonResponse(response, errorResponse);
    }
    
    /**
     * Create and write a standard success/error response
     * 
     * @param response HttpServletResponse to write to
     * @param success Whether operation was successful
     * @param successMessage Message for success case
     * @param errorMessage Message for error case
     * @throws IOException if writing fails
     */
    public void writeStandardResponse(HttpServletResponse response, boolean success, 
                                    String successMessage, String errorMessage) throws IOException {
        Map<String, Object> result = responseFactory.createResponse(success, successMessage, errorMessage);
        if (!success) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        writeJsonResponse(response, result);
    }
    
    // Service access methods - providing simplified access to all services
    
    /**
     * Get AdminService instance
     * 
     * @return AdminService instance
     */
    public AdminService getAdminService() {
        return serviceFactory.getAdminService();
    }
    
    /**
     * Get CustomerService instance
     * 
     * @return CustomerService instance
     */
    public CustomerService getCustomerService() {
        return serviceFactory.getCustomerService();
    }
    
    /**
     * Get ItemService instance
     * 
     * @return ItemService instance
     */
    public ItemService getItemService() {
        return serviceFactory.getItemService();
    }
    
    /**
     * Get BillService instance
     *
     * @return BillService instance
     */
    public BillService getBillService() {
        return serviceFactory.getBillService();
    }

    /**
     * Get ResponseFactory instance for advanced response creation
     *
     * @return IResponseFactory instance
     */
    public IResponseFactory getResponseFactory() {
        return responseFactory;
    }
    
    // Validation methods - providing simplified access to validation operations
    
    /**
     * Validate login credentials
     * 
     * @param username Username to validate
     * @param password Password to validate
     * @return ValidationResult
     */
    public ValidationUtil.ValidationResult validateLogin(String username, String password) {
        return validationFactory.validateLogin(username, password);
    }
    
    /**
     * Validate customer data
     * 
     * @param accountNumber Customer account number
     * @param fullName Customer full name
     * @param telephone Customer telephone
     * @param address Customer address
     * @param unitsConsumed Units consumed
     * @return ValidationResult
     */
    public ValidationUtil.ValidationResult validateCustomer(String accountNumber, String fullName, 
                                                           String telephone, String address, int unitsConsumed) {
        return validationFactory.validateCustomer(accountNumber, fullName, telephone, address, unitsConsumed);
    }
    
    /**
     * Validate item data
     * 
     * @param name Item name
     * @param price Item price
     * @param quantity Item quantity
     * @return ValidationResult
     */
    public ValidationUtil.ValidationResult validateItem(String name, double price, int quantity) {
        return validationFactory.validateItem(name, price, quantity);
    }
    
    /**
     * Process login operation with complete error handling
     * 
     * @param request HttpServletRequest containing login data
     * @param response HttpServletResponse for writing result
     * @return Admin object if login successful, null otherwise
     * @throws IOException if response writing fails
     */
    public Admin processLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, String> data = parseJsonRequest(request);
        String username = data.get("username");
        String password = data.get("password");
        
        // Validate input
        ValidationUtil.ValidationResult validation = validateLogin(username, password);
        if (!validation.isValid()) {
            handleValidationErrors(response, validation);
            return null;
        }
        
        // Attempt login
        Admin admin = getAdminService().login(username.trim(), password);
        if (admin != null) {
            // Create session
            HttpSession session = request.getSession(true);
            session.setAttribute("admin", admin);

            Map<String, Object> successResponse = responseFactory.createSuccessResponse("Login successful");
            successResponse.put("username", admin.getUsername());
            writeJsonResponse(response, successResponse);
            return admin;
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            Map<String, Object> errorResponse = responseFactory.createErrorResponse("Invalid username or password");
            writeJsonResponse(response, errorResponse);
            return null;
        }
    }
    
    /**
     * Reset the facade instance (useful for testing)
     */
    public static void resetInstance() {
        instance = null;
    }
}
