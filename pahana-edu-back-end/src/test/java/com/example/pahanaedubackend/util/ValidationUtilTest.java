package com.example.pahanaedubackend.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for ValidationUtil - Happy Path scenarios only
 */
class ValidationUtilTest {

    @Test
    @DisplayName("Is Not Empty - Happy Path")
    void testIsNotEmpty_Success() {
        // Given & When & Then
        assertTrue(ValidationUtil.isNotEmpty("valid string"), "Non-empty string should be valid");
        assertTrue(ValidationUtil.isNotEmpty("  valid  "), "String with spaces should be valid after trim");
        assertTrue(ValidationUtil.isNotEmpty("a"), "Single character should be valid");
        assertTrue(ValidationUtil.isNotEmpty("123"), "Numeric string should be valid");
    }

    @Test
    @DisplayName("Is Valid Length - Happy Path")
    void testIsValidLength_Success() {
        // Given & When & Then
        assertTrue(ValidationUtil.isValidLength("hello", 3, 10), "String within range should be valid");
        assertTrue(ValidationUtil.isValidLength("abc", 3, 5), "String at minimum length should be valid");
        assertTrue(ValidationUtil.isValidLength("abcde", 3, 5), "String at maximum length should be valid");
        assertTrue(ValidationUtil.isValidLength("  hello  ", 3, 10), "Trimmed string within range should be valid");
    }

    @Test
    @DisplayName("Matches Pattern - Happy Path")
    void testMatchesPattern_Success() {
        // Given & When & Then
        assertTrue(ValidationUtil.matchesPattern("abc123", "^[a-z0-9]+$"), "Valid alphanumeric pattern should match");
        assertTrue(ValidationUtil.matchesPattern("test@email.com", "^[\\w._%+-]+@[\\w.-]+\\.[A-Za-z]{2,}$"), "Valid email pattern should match");
        assertTrue(ValidationUtil.matchesPattern("1234567890", "^[0-9]{10}$"), "Valid 10-digit pattern should match");
    }

    @Test
    @DisplayName("Is Valid Phone - Happy Path")
    void testIsValidPhone_Success() {
        // Given & When & Then
        assertTrue(ValidationUtil.isValidPhone("1234567890"), "10-digit phone should be valid");
        assertTrue(ValidationUtil.isValidPhone("9876543210"), "Different 10-digit phone should be valid");
        assertTrue(ValidationUtil.isValidPhone("  1234567890  "), "Phone with spaces should be valid after trim");
    }

    @Test
    @DisplayName("Is Valid Account Number - Happy Path")
    void testIsValidAccountNumber_Success() {
        // Given & When & Then
        assertTrue(ValidationUtil.isValidAccountNumber("ABC123"), "6-character alphanumeric should be valid");
        assertTrue(ValidationUtil.isValidAccountNumber("USER123456"), "10-character alphanumeric should be valid");
        assertTrue(ValidationUtil.isValidAccountNumber("ACCOUNT12345"), "12-character alphanumeric should be valid");
        assertTrue(ValidationUtil.isValidAccountNumber("  ABC123  "), "Account number with spaces should be valid after trim");
    }

    @Test
    @DisplayName("Is Valid Integer - Happy Path")
    void testIsValidInteger_Success() {
        // Given & When & Then
        assertTrue(ValidationUtil.isValidInteger(50, 0, 100), "Integer within range should be valid");
        assertTrue(ValidationUtil.isValidInteger(0, 0, 100), "Integer at minimum should be valid");
        assertTrue(ValidationUtil.isValidInteger(100, 0, 100), "Integer at maximum should be valid");
        assertTrue(ValidationUtil.isValidInteger(1000, 500, 2000), "Large integer within range should be valid");
    }

    @Test
    @DisplayName("Is Valid Number - Happy Path")
    void testIsValidNumber_Success() {
        // Given & When & Then
        assertTrue(ValidationUtil.isValidNumber(50.5, 0.0, 100.0), "Double within range should be valid");
        assertTrue(ValidationUtil.isValidNumber(0.01, 0.01, 100.0), "Double at minimum should be valid");
        assertTrue(ValidationUtil.isValidNumber(99.99, 0.0, 100.0), "Double at maximum should be valid");
        assertTrue(ValidationUtil.isValidNumber(1500.75, 1000.0, 2000.0), "Large double within range should be valid");
    }

    @Test
    @DisplayName("Validate Customer - Happy Path")
    void testValidateCustomer_Success() {
        // Given
        String accountNumber = "CUST123";
        String fullName = "John Doe";
        String telephone = "1234567890";
        String address = "123 Main Street, City";
        int unitsConsumed = 150;
        
        // When
        ValidationUtil.ValidationResult result = ValidationUtil.validateCustomer(
            accountNumber, fullName, telephone, address, unitsConsumed);
        
        // Then
        assertTrue(result.isValid(), "Valid customer data should pass validation");
        assertTrue(result.getErrors().isEmpty(), "Valid customer should have no errors");
    }

    @Test
    @DisplayName("Validate Item - Happy Path")
    void testValidateItem_Success() {
        // Given
        String name = "Laptop Computer";
        double price = 999.99;
        int quantity = 50;
        
        // When
        ValidationUtil.ValidationResult result = ValidationUtil.validateItem(name, price, quantity);
        
        // Then
        assertTrue(result.isValid(), "Valid item data should pass validation");
        assertTrue(result.getErrors().isEmpty(), "Valid item should have no errors");
    }

    @Test
    @DisplayName("Validate Login - Happy Path")
    void testValidateLogin_Success() {
        // Given
        String username = "admin123";
        String password = "password123";
        
        // When
        ValidationUtil.ValidationResult result = ValidationUtil.validateLogin(username, password);
        
        // Then
        assertTrue(result.isValid(), "Valid login data should pass validation");
        assertTrue(result.getErrors().isEmpty(), "Valid login should have no errors");
    }

    @Test
    @DisplayName("ValidationResult - Happy Path Operations")
    void testValidationResult_Success() {
        // Given
        ValidationUtil.ValidationResult result = new ValidationUtil.ValidationResult();
        
        // When & Then
        assertTrue(result.isValid(), "New ValidationResult should be valid initially");
        assertTrue(result.getErrors().isEmpty(), "New ValidationResult should have no errors");
        assertNull(result.getFirstError(), "First error should be null when no errors");
        assertEquals("", result.getAllErrors(), "All errors should be empty string when no errors");
    }

    @Test
    @DisplayName("Validate Customer with Different Valid Data")
    void testValidateCustomer_WithDifferentValidData() {
        // Given
        String accountNumber = "USER456789";
        String fullName = "Jane Smith Johnson";
        String telephone = "9876543210";
        String address = "456 Oak Avenue, Suite 100, Downtown";
        int unitsConsumed = 500;
        
        // When
        ValidationUtil.ValidationResult result = ValidationUtil.validateCustomer(
            accountNumber, fullName, telephone, address, unitsConsumed);
        
        // Then
        assertTrue(result.isValid(), "Different valid customer data should pass validation");
        assertEquals(0, result.getErrors().size(), "Should have no validation errors");
    }

    @Test
    @DisplayName("Validate Item with Different Valid Data")
    void testValidateItem_WithDifferentValidData() {
        // Given
        String name = "Office Chair";
        double price = 150.50;
        int quantity = 25;
        
        // When
        ValidationUtil.ValidationResult result = ValidationUtil.validateItem(name, price, quantity);
        
        // Then
        assertTrue(result.isValid(), "Different valid item data should pass validation");
        assertEquals(0, result.getErrors().size(), "Should have no validation errors");
    }
}
