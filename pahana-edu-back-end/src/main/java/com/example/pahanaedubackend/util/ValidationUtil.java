package com.example.pahanaedubackend.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple validation utility class
 * Provides common validation methods without complex design patterns
 */
public class ValidationUtil {

    /**
     * Validates if a string is not null and not empty after trimming
     * @param value String to validate
     * @return true if valid, false otherwise
     */
    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /**
     * Validates string length
     * @param value String to validate
     * @param minLength Minimum length
     * @param maxLength Maximum length
     * @return true if valid, false otherwise
     */
    public static boolean isValidLength(String value, int minLength, int maxLength) {
        if (value == null) return false;
        int length = value.trim().length();
        return length >= minLength && length <= maxLength;
    }

    /**
     * Validates if string matches a regex pattern
     * @param value String to validate
     * @param pattern Regex pattern
     * @return true if matches, false otherwise
     */
    public static boolean matchesPattern(String value, String pattern) {
        if (value == null || pattern == null) return false;
        return value.matches(pattern);
    }

    /**
     * Validates phone number (10 digits)
     * @param phone Phone number to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidPhone(String phone) {
        return phone != null && phone.trim().matches("^[0-9]{10}$");
    }

    /**
     * Validates account number (6-12 alphanumeric characters)
     * @param accountNumber Account number to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidAccountNumber(String accountNumber) {
        if (accountNumber == null) return false;
        String trimmed = accountNumber.trim();
        return trimmed.length() >= 6 && trimmed.length() <= 12 &&
               trimmed.matches("^[A-Za-z0-9]+$");
    }

    /**
     * Validates numeric value within range
     * @param value Value to validate
     * @param min Minimum value (inclusive)
     * @param max Maximum value (inclusive)
     * @return true if valid, false otherwise
     */
    public static boolean isValidNumber(double value, double min, double max) {
        return value >= min && value <= max;
    }

    /**
     * Validates integer value within range
     * @param value Value to validate
     * @param min Minimum value (inclusive)
     * @param max Maximum value (inclusive)
     * @return true if valid, false otherwise
     */
    public static boolean isValidInteger(int value, int min, int max) {
        return value >= min && value <= max;
    }

    /**
     * Simple validation result class
     */
    public static class ValidationResult {
        private boolean valid;
        private List<String> errors;

        public ValidationResult() {
            this.valid = true;
            this.errors = new ArrayList<>();
        }

        public void addError(String error) {
            this.errors.add(error);
            this.valid = false;
        }

        public boolean isValid() {
            return valid;
        }

        public List<String> getErrors() {
            return errors;
        }

        public String getFirstError() {
            return errors.isEmpty() ? null : errors.get(0);
        }

        public String getAllErrors() {
            return String.join(", ", errors);
        }
    }

    /**
     * Validates customer data
     * @param accountNumber Customer account number
     * @param fullName Customer full name
     * @param telephone Customer telephone
     * @param address Customer address
     * @param unitsConsumed Units consumed
     * @return ValidationResult with validation status and errors
     */
    public static ValidationResult validateCustomer(String accountNumber, String fullName,
                                                  String telephone, String address, int unitsConsumed) {
        ValidationResult result = new ValidationResult();

        // Validate account number
        if (!isNotEmpty(accountNumber)) {
            result.addError("Account number is required");
        } else if (!isValidAccountNumber(accountNumber)) {
            result.addError("Account number must be 6-12 alphanumeric characters");
        }

        // Validate full name
        if (!isNotEmpty(fullName)) {
            result.addError("Full name is required");
        } else if (!isValidLength(fullName, 2, 100)) {
            result.addError("Full name must be between 2 and 100 characters");
        }

        // Validate telephone
        if (!isNotEmpty(telephone)) {
            result.addError("Telephone number is required");
        } else if (!isValidPhone(telephone)) {
            result.addError("Telephone number must be exactly 10 digits");
        }

        // Validate address
        if (!isNotEmpty(address)) {
            result.addError("Address is required");
        } else if (!isValidLength(address, 10, 255)) {
            result.addError("Address must be between 10 and 255 characters");
        }

        // Validate units consumed
        if (!isValidInteger(unitsConsumed, 0, 10000)) {
            result.addError("Units consumed must be between 0 and 10,000");
        }

        return result;
    }

    /**
     * Validates item data
     * @param name Item name
     * @param price Item price
     * @param quantity Item quantity
     * @return ValidationResult with validation status and errors
     */
    public static ValidationResult validateItem(String name, double price, int quantity) {
        ValidationResult result = new ValidationResult();

        // Validate name
        if (!isNotEmpty(name)) {
            result.addError("Item name is required");
        } else if (!isValidLength(name, 2, 100)) {
            result.addError("Item name must be between 2 and 100 characters");
        }

        // Validate price
        if (!isValidNumber(price, 0.01, 999999.99)) {
            result.addError("Item price must be between 0.01 and 999,999.99");
        }

        // Validate quantity
        if (!isValidInteger(quantity, 0, 10000)) {
            result.addError("Item quantity must be between 0 and 10,000");
        }

        return result;
    }

    /**
     * Validates login credentials
     * @param username Username
     * @param password Password
     * @return ValidationResult with validation status and errors
     */
    public static ValidationResult validateLogin(String username, String password) {
        ValidationResult result = new ValidationResult();

        // Validate username
        if (!isNotEmpty(username)) {
            result.addError("Username is required");
        } else if (!isValidLength(username, 3, 50)) {
            result.addError("Username must be between 3 and 50 characters");
        }

        // Validate password
        if (!isNotEmpty(password)) {
            result.addError("Password is required");
        } else if (password.length() < 6) {
            result.addError("Password must be at least 6 characters long");
        }

        return result;
    }
}
