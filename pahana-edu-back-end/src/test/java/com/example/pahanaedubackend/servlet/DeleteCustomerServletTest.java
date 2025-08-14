package com.example.pahanaedubackend.servlet;

import com.example.pahanaedubackend.model.Customer;
import com.example.pahanaedubackend.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for DeleteCustomerServlet - Happy Path scenarios only
 */
class DeleteCustomerServletTest {

    private DeleteCustomerServlet servlet;
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        servlet = new DeleteCustomerServlet();
        customerService = new CustomerService();
    }

    @Test
    @DisplayName("Delete Customer Servlet - Initialization")
    void testServletInitialization() {
        // When
        DeleteCustomerServlet newServlet = new DeleteCustomerServlet();
        
        // Then
        assertNotNull(newServlet, "DeleteCustomerServlet should be initialized successfully");
    }

    @Test
    @DisplayName("Delete Customer - Service Integration")
    void testCustomerServiceIntegration() {
        // Given - First create a customer to delete
        Customer customer = new Customer();
        String uniqueAccountNumber = "DEL" + System.currentTimeMillis();
        customer.setAccountNumber(uniqueAccountNumber);
        customer.setFullName("Delete Test Customer");
        customer.setTelephone("1234567890");
        customer.setAddress("123 Delete Street");
        customer.setUnitsConsumed(100);
        
        boolean created = customerService.registerCustomer(customer);
        assertTrue(created, "Customer should be created first");
        
        // When
        boolean result = customerService.deleteCustomer(uniqueAccountNumber);
        
        // Then
        assertTrue(result, "Customer service should delete customer successfully");
    }

    @Test
    @DisplayName("Delete Customer - Multiple Deletions")
    void testMultipleDeletions() {
        // Given - Create multiple customers
        String timestamp = String.valueOf(System.currentTimeMillis());
        
        Customer customer1 = new Customer();
        customer1.setAccountNumber("DEL1" + timestamp);
        customer1.setFullName("Delete Customer One");
        customer1.setTelephone("1111111111");
        customer1.setAddress("111 Delete Street One");
        customer1.setUnitsConsumed(100);
        
        Customer customer2 = new Customer();
        customer2.setAccountNumber("DEL2" + timestamp);
        customer2.setFullName("Delete Customer Two");
        customer2.setTelephone("2222222222");
        customer2.setAddress("222 Delete Street Two");
        customer2.setUnitsConsumed(200);
        
        boolean created1 = customerService.registerCustomer(customer1);
        boolean created2 = customerService.registerCustomer(customer2);
        
        assertTrue(created1, "First customer should be created");
        assertTrue(created2, "Second customer should be created");
        
        // When
        boolean result1 = customerService.deleteCustomer("DEL1" + timestamp);
        boolean result2 = customerService.deleteCustomer("DEL2" + timestamp);
        
        // Then
        assertTrue(result1, "First customer deletion should succeed");
        assertTrue(result2, "Second customer deletion should succeed");
    }

    @Test
    @DisplayName("Delete Customer - Different Account Number Formats")
    void testDifferentAccountNumberFormats() {
        // Given - Create customers with different account number formats
        String timestamp = String.valueOf(System.currentTimeMillis());
        
        Customer customer1 = new Customer();
        customer1.setAccountNumber("SHORT" + timestamp.substring(0, 3));
        customer1.setFullName("Short Account Customer");
        customer1.setTelephone("1234567890");
        customer1.setAddress("123 Short Street");
        customer1.setUnitsConsumed(50);
        
        Customer customer2 = new Customer();
        customer2.setAccountNumber("VERYLONGACC" + timestamp.substring(0, 1));
        customer2.setFullName("Long Account Customer");
        customer2.setTelephone("9876543210");
        customer2.setAddress("456 Long Street");
        customer2.setUnitsConsumed(150);
        
        Customer customer3 = new Customer();
        customer3.setAccountNumber("MIX123" + timestamp.substring(0, 3));
        customer3.setFullName("Mixed Account Customer");
        customer3.setTelephone("5555555555");
        customer3.setAddress("789 Mixed Street");
        customer3.setUnitsConsumed(100);
        
        // Create customers
        customerService.registerCustomer(customer1);
        customerService.registerCustomer(customer2);
        customerService.registerCustomer(customer3);
        
        // When
        boolean result1 = customerService.deleteCustomer(customer1.getAccountNumber());
        boolean result2 = customerService.deleteCustomer(customer2.getAccountNumber());
        boolean result3 = customerService.deleteCustomer(customer3.getAccountNumber());
        
        // Then
        assertTrue(result1, "Short account number deletion should succeed");
        assertTrue(result2, "Long account number deletion should succeed");
        assertTrue(result3, "Mixed account number deletion should succeed");
    }

    @Test
    @DisplayName("Delete Customer - Complete Deletion Flow")
    void testCompleteDeletionFlow() {
        // Given - Create customer
        Customer customer = new Customer();
        String uniqueAccountNumber = "FLOW" + System.currentTimeMillis();
        customer.setAccountNumber(uniqueAccountNumber);
        customer.setFullName("Flow Delete Customer");
        customer.setTelephone("1234567890");
        customer.setAddress("123 Flow Delete Street");
        customer.setUnitsConsumed(200);
        
        boolean created = customerService.registerCustomer(customer);
        assertTrue(created, "Customer should be created successfully");
        
        // Verify customer exists by getting all customers
        List<Customer> customersBefore = customerService.getAllCustomers();
        boolean customerExists = false;
        for (Customer c : customersBefore) {
            if (c.getAccountNumber().equals(uniqueAccountNumber)) {
                customerExists = true;
                break;
            }
        }
        assertTrue(customerExists, "Customer should exist before deletion");
        
        // When
        boolean deleteResult = customerService.deleteCustomer(uniqueAccountNumber);
        
        // Then
        assertTrue(deleteResult, "Customer deletion should succeed");
    }

    @Test
    @DisplayName("Delete Customer - Service Method Verification")
    void testServiceMethodVerification() {
        // Given
        CustomerService service = new CustomerService();
        
        // When & Then
        assertNotNull(service, "CustomerService should be instantiated");
        
        // Test that deleteCustomer method exists and can be called
        assertDoesNotThrow(() -> {
            // This will return false for non-existent customer, but shouldn't throw exception
            boolean result = service.deleteCustomer("NONEXISTENT123");
            // We don't assert the result since the customer doesn't exist
            // We just verify the method can be called without exception
        }, "deleteCustomer method should not throw exception");
    }

    @Test
    @DisplayName("Delete Customer - Sequential Operations")
    void testSequentialOperations() {
        // Given - Create customer
        Customer customer = new Customer();
        String uniqueAccountNumber = "SEQ" + System.currentTimeMillis();
        customer.setAccountNumber(uniqueAccountNumber);
        customer.setFullName("Sequential Test Customer");
        customer.setTelephone("1234567890");
        customer.setAddress("123 Sequential Street");
        customer.setUnitsConsumed(300);
        
        // When - Perform sequential operations
        boolean created = customerService.registerCustomer(customer);
        assertTrue(created, "Customer creation should succeed");
        
        // Verify customer exists
        List<Customer> customers = customerService.getAllCustomers();
        boolean exists = false;
        for (Customer c : customers) {
            if (c.getAccountNumber().equals(uniqueAccountNumber)) {
                exists = true;
                break;
            }
        }
        assertTrue(exists, "Customer should exist after creation");
        
        // Delete customer
        boolean deleted = customerService.deleteCustomer(uniqueAccountNumber);
        assertTrue(deleted, "Customer deletion should succeed");
    }

    @Test
    @DisplayName("Delete Customer - Different Customer Types")
    void testDifferentCustomerTypes() {
        // Given - Create customers with different characteristics
        String timestamp = String.valueOf(System.currentTimeMillis());
        
        // Customer with minimum units
        Customer minCustomer = new Customer();
        minCustomer.setAccountNumber("MIN" + timestamp);
        minCustomer.setFullName("Minimum Customer");
        minCustomer.setTelephone("1000000000");
        minCustomer.setAddress("100 Minimum Street");
        minCustomer.setUnitsConsumed(0);
        
        // Customer with high units
        Customer maxCustomer = new Customer();
        maxCustomer.setAccountNumber("MAX" + timestamp);
        maxCustomer.setFullName("Maximum Customer");
        maxCustomer.setTelephone("9000000000");
        maxCustomer.setAddress("900 Maximum Street");
        maxCustomer.setUnitsConsumed(5000);
        
        // Create customers
        customerService.registerCustomer(minCustomer);
        customerService.registerCustomer(maxCustomer);
        
        // When
        boolean result1 = customerService.deleteCustomer("MIN" + timestamp);
        boolean result2 = customerService.deleteCustomer("MAX" + timestamp);
        
        // Then
        assertTrue(result1, "Minimum units customer deletion should succeed");
        assertTrue(result2, "Maximum units customer deletion should succeed");
    }

    @Test
    @DisplayName("Delete Customer - Batch Deletion")
    void testBatchDeletion() {
        // Given - Create multiple customers for batch deletion
        String timestamp = String.valueOf(System.currentTimeMillis());
        List<String> accountNumbers = new ArrayList<>();
        
        for (int i = 1; i <= 5; i++) {
            Customer customer = new Customer();
            String accountNumber = "BATCH" + i + timestamp;
            customer.setAccountNumber(accountNumber);
            customer.setFullName("Batch Customer " + i);
            customer.setTelephone("123456789" + i);
            customer.setAddress("123 Batch Street " + i);
            customer.setUnitsConsumed(i * 100);
            
            customerService.registerCustomer(customer);
            accountNumbers.add(accountNumber);
        }
        
        // When - Delete all customers
        int successfulDeletions = 0;
        for (String accountNumber : accountNumbers) {
            if (customerService.deleteCustomer(accountNumber)) {
                successfulDeletions++;
            }
        }
        
        // Then
        assertEquals(5, successfulDeletions, "All batch customers should be deleted successfully");
    }
}
