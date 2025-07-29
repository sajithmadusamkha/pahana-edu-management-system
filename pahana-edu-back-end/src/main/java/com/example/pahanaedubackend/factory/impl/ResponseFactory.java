package com.example.pahanaedubackend.factory.impl;

import com.example.pahanaedubackend.factory.IResponseFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Factory class for creating standardized response objects using the Factory Design Pattern.
 * This class implements the Singleton pattern and provides consistent response formatting
 * across the entire application.
 *
 * Benefits:
 * - Standardized response format
 * - Consistent error handling
 * - Easy to modify response structure globally
 * - Reduces code duplication in controllers
 * - Centralized response creation logic
 */
public class ResponseFactory implements IResponseFactory {
    
    // Singleton instance
    private static ResponseFactory instance;
    
    /**
     * Private constructor to prevent direct instantiation
     */
    private ResponseFactory() {
        // Private constructor for singleton
    }
    
    /**
     * Get the singleton instance of ResponseFactory
     * Thread-safe implementation using double-checked locking
     * 
     * @return ResponseFactory instance
     */
    public static ResponseFactory getInstance() {
        if (instance == null) {
            synchronized (ResponseFactory.class) {
                if (instance == null) {
                    instance = new ResponseFactory();
                }
            }
        }
        return instance;
    }
    
    /**
     * Create a basic response with success status and message
     * 
     * @param success Whether the operation was successful
     * @param successMessage Message to show on success
     * @param errorMessage Message to show on error
     * @return Map containing the response
     */
    public Map<String, Object> createResponse(boolean success, String successMessage, String errorMessage) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", success ? successMessage : errorMessage);
        return response;
    }
    
    /**
     * Create a success response with data
     * 
     * @param message Success message
     * @param data Data to include in response
     * @return Map containing the success response
     */
    public Map<String, Object> createSuccessResponse(String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", message);
        if (data != null) {
            response.put("data", data);
        }
        return response;
    }
    
    /**
     * Create a success response without data
     * 
     * @param message Success message
     * @return Map containing the success response
     */
    public Map<String, Object> createSuccessResponse(String message) {
        return createSuccessResponse(message, null);
    }
    
    /**
     * Create an error response
     * 
     * @param message Error message
     * @return Map containing the error response
     */
    public Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        return response;
    }
    
    /**
     * Create an error response with error code
     * 
     * @param message Error message
     * @param errorCode Error code
     * @return Map containing the error response
     */
    public Map<String, Object> createErrorResponse(String message, String errorCode) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        response.put("errorCode", errorCode);
        return response;
    }
    
    /**
     * Create a validation error response
     * 
     * @param message Primary error message
     * @param errors List of validation errors
     * @return Map containing the validation error response
     */
    public Map<String, Object> createValidationErrorResponse(String message, List<String> errors) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        response.put("errors", errors);
        return response;
    }
    
    /**
     * Create an unauthorized response
     * 
     * @return Map containing the unauthorized response
     */
    public Map<String, Object> createUnauthorizedResponse() {
        return createErrorResponse("Unauthorized: Admin login required", "UNAUTHORIZED");
    }
    
    /**
     * Create a forbidden response
     * 
     * @param message Forbidden message
     * @return Map containing the forbidden response
     */
    public Map<String, Object> createForbiddenResponse(String message) {
        return createErrorResponse(message, "FORBIDDEN");
    }
    
    /**
     * Create a not found response
     * 
     * @param resource The resource that was not found
     * @return Map containing the not found response
     */
    public Map<String, Object> createNotFoundResponse(String resource) {
        return createErrorResponse(resource + " not found", "NOT_FOUND");
    }
    
    /**
     * Reset the factory instance (useful for testing)
     * This method should only be used in test environments
     */
    public static void resetInstance() {
        instance = null;
    }
}
