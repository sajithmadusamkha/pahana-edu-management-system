package com.example.pahanaedubackend.servlet;

import com.example.pahanaedubackend.model.Customer;
import com.example.pahanaedubackend.service.CustomerService;
import com.example.pahanaedubackend.util.ValidationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for CustomerRegisterServlet - Happy Path scenarios only
 */
class CustomerRegisterServletTest {

    private CustomerRegisterServlet servlet;
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        servlet = new CustomerRegisterServlet();
        customerService = new CustomerService();
    }

    @Test
    @DisplayName("Customer Register Servlet - Initialization")
    void testServletInitialization() {
        // When
        CustomerRegisterServlet newServlet = new CustomerRegisterServlet();
        
        // Then
        assertNotNull(newServlet, "CustomerRegisterServlet should be initialized successfully");
    }

    @Test
    @DisplayName("Customer Registration - Valid Customer Object")
    void testValidCustomerObject() {
        // Given
        Customer customer = new Customer();
        String uniqueAccountNumber = "CUST" + System.currentTimeMillis();
        customer.setAccountNumber(uniqueAccountNumber);
        customer.setFullName("John Doe");
        customer.setTelephone("1234567890");
        customer.setAddress("123 Main Street, City");
        customer.setUnitsConsumed(150);
        
        // When & Then
        assertEquals(uniqueAccountNumber, customer.getAccountNumber(), "Account number should be set correctly");
        assertEquals("John Doe", customer.getFullName(), "Full name should be set correctly");
        assertEquals("1234567890", customer.getTelephone(), "Telephone should be set correctly");
        assertEquals("123 Main Street, City", customer.getAddress(), "Address should be set correctly");
        assertEquals(150, customer.getUnitsConsumed(), "Units consumed should be set correctly");
    }

    @Test
    @DisplayName("Customer Registration - Service Integration")
    void testCustomerServiceIntegration() {
        // Given
        Customer customer = new Customer();
        String uniqueAccountNumber = "SERV" + String.valueOf(Math.random()).substring(2, 8);
        customer.setAccountNumber(uniqueAccountNumber);
        customer.setFullName("Service Test Customer");
        customer.setTelephone("9876543210");
        customer.setAddress("456 Service Street, Test City");
        customer.setUnitsConsumed(200);
        
        // When
        boolean result = customerService.registerCustomer(customer);
        
        // Then
        assertTrue(result, "Customer service should register customer successfully");
    }

    @Test
    @DisplayName("Customer Registration - Validation Integration")
    void testValidationIntegration() {
        // Given
        String accountNumber = "VALID123";
        String fullName = "Valid Customer";
        String telephone = "1234567890";
        String address = "123 Valid Street, Valid City";
        int unitsConsumed = 100;
        
        // When
        ValidationUtil.ValidationResult result = ValidationUtil.validateCustomer(
            accountNumber, fullName, telephone, address, unitsConsumed);
        
        // Then
        assertTrue(result.isValid(), "Valid customer data should pass validation");
        assertTrue(result.getErrors().isEmpty(), "Valid customer should have no validation errors");
    }

    @Test
    @DisplayName("Customer Registration - Multiple Valid Customers")
    void testMultipleValidCustomers() {
        // Given
        String timestamp = String.valueOf(System.currentTimeMillis());
        String uniqueId1 = String.valueOf(Math.random()).substring(2, 8);
        String uniqueId2 = String.valueOf(Math.random()).substring(2, 8);

        Customer customer1 = new Customer();
        customer1.setAccountNumber("CU1" + uniqueId1); // Ensure unique and within 6-12 chars
        customer1.setFullName("Customer One");
        customer1.setTelephone("1111111111");
        customer1.setAddress("111 First Street, City One");
        customer1.setUnitsConsumed(100);

        Customer customer2 = new Customer();
        customer2.setAccountNumber("CU2" + uniqueId2); // Ensure unique and within 6-12 chars
        customer2.setFullName("Customer Two");
        customer2.setTelephone("2222222222");
        customer2.setAddress("222 Second Street, City Two");
        customer2.setUnitsConsumed(200);

        // When
        boolean result1 = customerService.registerCustomer(customer1);
        boolean result2 = customerService.registerCustomer(customer2);

        // Then
        assertTrue(result1, "First customer registration should succeed");
        assertTrue(result2, "Second customer registration should succeed");
    }

    @Test
    @DisplayName("Customer Registration - Different Valid Data Sets")
    void testDifferentValidDataSets() {
        // Given
        String timestamp = String.valueOf(System.currentTimeMillis());
        String uniqueId = String.valueOf(Math.random()).substring(2, 8); // Generate unique 6-digit number

        // Test data set 1
        Customer customer1 = new Customer();
        customer1.setAccountNumber("SH" + uniqueId); // Ensure unique and within 6-12 chars
        customer1.setFullName("AB");  // Minimum length
        customer1.setTelephone("1000000000");
        customer1.setAddress("1234567890");  // Minimum address length (exactly 10 chars)
        customer1.setUnitsConsumed(0);  // Minimum units

        // Test data set 2
        Customer customer2 = new Customer();
        customer2.setAccountNumber("VL" + timestamp.substring(timestamp.length()-6)); // Use last 6 digits + prefix
        customer2.setFullName("Very Long Customer Name That Is Still Valid");
        customer2.setTelephone("9999999999");
        customer2.setAddress("Very Long Address That Contains Multiple Words And Is Still Valid For Testing");
        customer2.setUnitsConsumed(5000);  // High but valid units

        // When
        boolean result1 = customerService.registerCustomer(customer1);
        boolean result2 = customerService.registerCustomer(customer2);

        // Then
        assertTrue(result1, "Customer with minimum valid data should register successfully");
        assertTrue(result2, "Customer with maximum valid data should register successfully");
    }

    @Test
    @DisplayName("Customer Registration - Validation Success Cases")
    void testValidationSuccessCases() {
        // Test Case 1: Standard valid data
        ValidationUtil.ValidationResult result1 = ValidationUtil.validateCustomer(
            "STANDARD123", "Standard Customer", "1234567890", "123 Standard Street," +
                        "City", 150);
        assertTrue(result1.isValid(), "Standard valid data should pass validation");

        // Test Case 2: Alphanumeric account number
        ValidationUtil.ValidationResult result2 = ValidationUtil.validateCustomer(
            "ABC123XYZ", "Alpha Customer", "9876543210", "456 Alpha Avenue, Town",
                300);
        assertTrue(result2.isValid(), "Alphanumeric account number should pass validation");

        // Test Case 3: Long but valid data
        ValidationUtil.ValidationResult result3 = ValidationUtil.validateCustomer(
            "LONGACCOUNT1", "Very Long Customer Name", "5555555555",
            "Very Long Address With Multiple Words And Details", 1000);
        assertTrue(result3.isValid(), "Long but valid data should pass validation");
    }

    @Test
    @DisplayName("Customer Registration - Units Consumed Validation")
    void testUnitsConsumedValidation() {
        // Given
        String accountNumber = "UNITS123";
        String fullName = "Units Test Customer";
        String telephone = "1234567890";
        String address = "123 Units Street, City";
        
        // Test different valid units consumed values
        ValidationUtil.ValidationResult result1 = ValidationUtil.validateCustomer(
            accountNumber, fullName, telephone, address, 0);
        ValidationUtil.ValidationResult result2 = ValidationUtil.validateCustomer(
            accountNumber, fullName, telephone, address, 500);
        ValidationUtil.ValidationResult result3 = ValidationUtil.validateCustomer(
            accountNumber, fullName, telephone, address, 9999);
        
        // Then
        assertTrue(result1.isValid(), "Zero units consumed should be valid");
        assertTrue(result2.isValid(), "Medium units consumed should be valid");
        assertTrue(result3.isValid(), "High units consumed should be valid");
    }

    @Test
    @DisplayName("Customer Registration - Complete Registration Flow")
    void testCompleteRegistrationFlow() {
        // Given
        Customer customer = new Customer();
        String uniqueAccountNumber = "FLOW" + String.valueOf(Math.random()).substring(2, 8);
        customer.setAccountNumber(uniqueAccountNumber); // Already within 6-12 chars
        customer.setFullName("Flow Test Customer");
        customer.setTelephone("1234567890");
        customer.setAddress("123 Flow Street, Flow City, State"); // Ensure at least 10 characters
        customer.setUnitsConsumed(250);

        // Validate first
        ValidationUtil.ValidationResult validation = ValidationUtil.validateCustomer(
            customer.getAccountNumber(), customer.getFullName(),
            customer.getTelephone(), customer.getAddress(), customer.getUnitsConsumed());

        // Then register if valid
        assertTrue(validation.isValid(), "Customer data should be valid");

        boolean registrationResult = customerService.registerCustomer(customer);
        assertTrue(registrationResult, "Customer registration should succeed");
    }
}
