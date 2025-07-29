package com.example.pahanaedubackend.factory;

import java.util.List;
import java.util.Map;

/**
 * Interface for Response Factory following the Abstract Factory Design Pattern.
 * This interface defines the contract for creating standardized response objects.
 * 
 * Benefits of using interface:
 * - True abstraction and polymorphism
 * - Easy to create different response formats (JSON, XML, etc.)
 * - Better testability with mock implementations
 * - Follows SOLID principles (Dependency Inversion)
 * - Allows for different response creation strategies
 */
public interface IResponseFactory {
    
    /**
     * Create a basic response with success status and message
     * 
     * @param success Whether the operation was successful
     * @param successMessage Message to show on success
     * @param errorMessage Message to show on error
     * @return Map containing the response
     */
    Map<String, Object> createResponse(boolean success, String successMessage, String errorMessage);
    
    /**
     * Create a success response with data
     * 
     * @param message Success message
     * @param data Data to include in response
     * @return Map containing the success response
     */
    Map<String, Object> createSuccessResponse(String message, Object data);
    
    /**
     * Create a success response without data
     * 
     * @param message Success message
     * @return Map containing the success response
     */
    Map<String, Object> createSuccessResponse(String message);
    
    /**
     * Create an error response
     * 
     * @param message Error message
     * @return Map containing the error response
     */
    Map<String, Object> createErrorResponse(String message);
    
    /**
     * Create an error response with error code
     * 
     * @param message Error message
     * @param errorCode Error code
     * @return Map containing the error response
     */
    Map<String, Object> createErrorResponse(String message, String errorCode);
    
    /**
     * Create a validation error response
     * 
     * @param message Primary error message
     * @param errors List of validation errors
     * @return Map containing the validation error response
     */
    Map<String, Object> createValidationErrorResponse(String message, List<String> errors);
    
    /**
     * Create an unauthorized response
     * 
     * @return Map containing the unauthorized response
     */
    Map<String, Object> createUnauthorizedResponse();
    
    /**
     * Create a forbidden response
     * 
     * @param message Forbidden message
     * @return Map containing the forbidden response
     */
    Map<String, Object> createForbiddenResponse(String message);
    
    /**
     * Create a not found response
     * 
     * @param resource The resource that was not found
     * @return Map containing the not found response
     */
    Map<String, Object> createNotFoundResponse(String resource);
}
