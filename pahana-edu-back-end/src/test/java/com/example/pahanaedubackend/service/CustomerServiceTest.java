package com.example.pahanaedubackend.service;

import com.example.pahanaedubackend.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for CustomerService - Happy Path scenarios only
 */
class CustomerServiceTest {

    private CustomerService customerService;
    private Customer testCustomer;

    @BeforeEach
    void setUp() {
        customerService = new CustomerService();
        testCustomer = new Customer();
        // Use timestamp to ensure unique account number for each test run
        String uniqueAccountNumber = "ACC" + System.currentTimeMillis();
        testCustomer.setAccountNumber(uniqueAccountNumber);
        testCustomer.setFullName("John Smith");
        testCustomer.setTelephone("1234567890");
        testCustomer.setAddress("123 Main Street");
        testCustomer.setUnitsConsumed(100);
    }

    @Test
    @DisplayName("Customer Registration - Happy Path")
    void testRegisterCustomer_Success() {
        // When
        boolean result = customerService.registerCustomer(testCustomer);
        
        // Then
        assertTrue(result, "Customer registration should be successful");
    }

    @Test
    @DisplayName("Get All Customers - Happy Path")
    void testGetAllCustomers_Success() {
        // Given
        customerService.registerCustomer(testCustomer);
        
        // When
        List<Customer> customers = customerService.getAllCustomers();
        
        // Then
        assertNotNull(customers, "Customer list should not be null");
        assertFalse(customers.isEmpty(), "Customer list should not be empty");
    }

    @Test
    @DisplayName("Customer Update - Happy Path")
    void testUpdateCustomer_Success() {
        // Given
        customerService.registerCustomer(testCustomer);
        
        // Update customer details
        testCustomer.setFullName("John Smith Updated");
        testCustomer.setTelephone("9876543210");
        testCustomer.setAddress("456 Updated Street");
        testCustomer.setUnitsConsumed(150);
        
        // When
        boolean result = customerService.updateCustomer(testCustomer);
        
        // Then
        assertTrue(result, "Customer update should be successful");
    }

    @Test
    @DisplayName("Customer Delete - Happy Path")
    void testDeleteCustomer_Success() {
        // Given
        customerService.registerCustomer(testCustomer);
        
        // When
        boolean result = customerService.deleteCustomer(testCustomer.getAccountNumber());
        
        // Then
        assertTrue(result, "Customer deletion should be successful");
    }

    @Test
    @DisplayName("Customer Registration with Valid Data")
    void testRegisterCustomer_WithValidData() {
        // Given
        Customer customer = new Customer();
        String uniqueAccountNumber = "ACC" + System.currentTimeMillis() + "A";
        customer.setAccountNumber(uniqueAccountNumber);
        customer.setFullName("Jane Doe");
        customer.setTelephone("5551234567");
        customer.setAddress("789 Oak Avenue");
        customer.setUnitsConsumed(75);

        // When
        boolean result = customerService.registerCustomer(customer);

        // Then
        assertTrue(result, "Registration with valid data should succeed");
    }

    @Test
    @DisplayName("Customer Update with New Information")
    void testUpdateCustomer_WithNewInformation() {
        // Given
        Customer customer = new Customer();
        String uniqueAccountNumber = "ACC" + System.currentTimeMillis() + "B";
        customer.setAccountNumber(uniqueAccountNumber);
        customer.setFullName("Bob Johnson");
        customer.setTelephone("5559876543");
        customer.setAddress("321 Pine Street");
        customer.setUnitsConsumed(200);

        customerService.registerCustomer(customer);

        // Update with new information
        customer.setFullName("Robert Johnson");
        customer.setTelephone("5551111111");
        customer.setAddress("321 Pine Street, Apt 2B");
        customer.setUnitsConsumed(250);

        // When
        boolean result = customerService.updateCustomer(customer);

        // Then
        assertTrue(result, "Customer update with new information should succeed");
    }

    @Test
    @DisplayName("Customer Delete by Account Number")
    void testDeleteCustomer_ByAccountNumber() {
        // Given
        Customer customer = new Customer();
        String uniqueAccountNumber = "ACC" + System.currentTimeMillis() + "C";
        customer.setAccountNumber(uniqueAccountNumber);
        customer.setFullName("Alice Brown");
        customer.setTelephone("5552222222");
        customer.setAddress("654 Elm Street");
        customer.setUnitsConsumed(125);

        customerService.registerCustomer(customer);

        // When
        boolean result = customerService.deleteCustomer(uniqueAccountNumber);

        // Then
        assertTrue(result, "Customer deletion by account number should succeed");
    }

    @Test
    @DisplayName("Get Recent Customers - Happy Path")
    void testGetRecentCustomers_Success() {
        // Given
        customerService.registerCustomer(testCustomer);
        int limit = 5;
        
        // When
        List<?> recentCustomers = customerService.getRecentCustomers(limit);
        
        // Then
        assertNotNull(recentCustomers, "Recent customers list should not be null");
    }
}
