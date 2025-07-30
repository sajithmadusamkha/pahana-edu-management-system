package com.example.pahanaedubackend.factory;

import com.example.pahanaedubackend.util.ValidationUtil;

/**
 * Interface for Validation Factory following the Abstract Factory Design Pattern.
 * This interface defines the contract for creating validation objects and operations.
 * 
 * Benefits of using interface:
 * - True abstraction and polymorphism
 * - Easy to create different validation implementations
 * - Better testability with mock implementations
 * - Follows SOLID principles (Dependency Inversion)
 * - Allows for different validation strategies (e.g., annotation-based, rule-based)
 */
public interface IValidationFactory {
    
    /**
     * Validate based on entity type and parameters
     * This method acts as a dispatcher to specific validation methods
     * 
     * @param entityType Type of entity to validate ("customer", "item", "admin", "login")
     * @param params Parameters for validation
     * @return ValidationResult with validation status and errors
     */
    ValidationUtil.ValidationResult validate(String entityType, Object... params);
    
    /**
     * Direct customer validation method
     * 
     * @param accountNumber Customer account number
     * @param fullName Customer full name
     * @param telephone Customer telephone
     * @param address Customer address
     * @param unitsConsumed Units consumed
     * @return ValidationResult
     */
    ValidationUtil.ValidationResult validateCustomer(String accountNumber, String fullName, 
                                                   String telephone, String address, int unitsConsumed);
    
    /**
     * Direct item validation method
     * 
     * @param name Item name
     * @param price Item price
     * @param quantity Item quantity
     * @return ValidationResult
     */
    ValidationUtil.ValidationResult validateItem(String name, double price, int quantity);
    
    /**
     * Direct login validation method
     * 
     * @param username Username
     * @param password Password
     * @return ValidationResult
     */
    ValidationUtil.ValidationResult validateLogin(String username, String password);
}
