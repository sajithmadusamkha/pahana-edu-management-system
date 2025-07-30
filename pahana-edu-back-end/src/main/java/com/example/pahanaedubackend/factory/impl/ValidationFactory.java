package com.example.pahanaedubackend.factory.impl;

import com.example.pahanaedubackend.factory.IValidationFactory;
import com.example.pahanaedubackend.util.ValidationUtil;

/**
 * Factory class for creating validation objects using the Factory Design Pattern.
 * This class implements the Singleton pattern and provides a unified interface
 * for validation operations across the application.
 *
 * Benefits:
 * - Centralized validation logic
 * - Easy to extend with new validation types
 * - Consistent validation interface
 * - Loose coupling between controllers and validation logic
 * - Easy to modify validation implementations
 */
public class ValidationFactory implements IValidationFactory {
    
    // Singleton instance
    private static ValidationFactory instance;
    
    /**
     * Private constructor to prevent direct instantiation
     */
    private ValidationFactory() {
        // Private constructor for singleton
    }
    
    /**
     * Get the singleton instance of ValidationFactory
     * Thread-safe implementation using double-checked locking
     * 
     * @return ValidationFactory instance
     */
    public static ValidationFactory getInstance() {
        if (instance == null) {
            synchronized (ValidationFactory.class) {
                if (instance == null) {
                    instance = new ValidationFactory();
                }
            }
        }
        return instance;
    }
    
    /**
     * Validate based on entity type and parameters
     * This method acts as a dispatcher to specific validation methods
     * 
     * @param entityType Type of entity to validate ("customer", "item", "admin", "login")
     * @param params Parameters for validation
     * @return ValidationResult with validation status and errors
     */
    public ValidationUtil.ValidationResult validate(String entityType, Object... params) {
        switch (entityType.toLowerCase()) {
            case "customer":
                return validateCustomerInternal(params);
            case "item":
                return validateItemInternal(params);
            case "login":
                return validateLoginInternal(params);
            default:
                throw new IllegalArgumentException("Unknown entity type: " + entityType);
        }
    }
    
    /**
     * Direct customer validation method (public interface method)
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
        return ValidationUtil.validateCustomer(accountNumber, fullName, telephone, address, unitsConsumed);
    }

    /**
     * Validate customer data (internal method)
     * Expected parameters: accountNumber, fullName, telephone, address, unitsConsumed
     *
     * @param params Customer validation parameters
     * @return ValidationResult
     */
    private ValidationUtil.ValidationResult validateCustomerInternal(Object... params) {
        if (params.length != 5) {
            throw new IllegalArgumentException("Customer validation requires 5 parameters: accountNumber, fullName, telephone, address, unitsConsumed");
        }
        
        String accountNumber = (String) params[0];
        String fullName = (String) params[1];
        String telephone = (String) params[2];
        String address = (String) params[3];
        Integer unitsConsumed = (Integer) params[4];
        
        return ValidationUtil.validateCustomer(accountNumber, fullName, telephone, address, unitsConsumed);
    }

    /**
     * Direct item validation method (public interface method)
     *
     * @param name Item name
     * @param price Item price
     * @param quantity Item quantity
     * @return ValidationResult
     */
    public ValidationUtil.ValidationResult validateItem(String name, double price, int quantity) {
        return ValidationUtil.validateItem(name, price, quantity);
    }

    /**
     * Direct login validation method (public interface method)
     *
     * @param username Username
     * @param password Password
     * @return ValidationResult
     */
    public ValidationUtil.ValidationResult validateLogin(String username, String password) {
        return ValidationUtil.validateLogin(username, password);
    }

    /**
     * Validate item data (internal method)
     * Expected parameters: name, price, quantity
     *
     * @param params Item validation parameters
     * @return ValidationResult
     */
    private ValidationUtil.ValidationResult validateItemInternal(Object... params) {
        if (params.length != 3) {
            throw new IllegalArgumentException("Item validation requires 3 parameters: name, price, quantity");
        }
        
        String name = (String) params[0];
        Double price = (Double) params[1];
        Integer quantity = (Integer) params[2];
        
        return ValidationUtil.validateItem(name, price, quantity);
    }
    
    /**
     * Validate login credentials (internal method)
     * Expected parameters: username, password
     *
     * @param params Login validation parameters
     * @return ValidationResult
     */
    private ValidationUtil.ValidationResult validateLoginInternal(Object... params) {
        if (params.length != 2) {
            throw new IllegalArgumentException("Login validation requires 2 parameters: username, password");
        }
        
        String username = (String) params[0];
        String password = (String) params[1];
        
        return ValidationUtil.validateLogin(username, password);
    }

    /**
     * Reset the factory instance (useful for testing)
     * This method should only be used in test environments
     */
    public static void resetInstance() {
        instance = null;
    }
}
