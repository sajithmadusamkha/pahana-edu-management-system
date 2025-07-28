package com.example.pahanaedubackend.factory;

import com.example.pahanaedubackend.dao.AdminDAO;
import com.example.pahanaedubackend.dao.CustomerDAO;
import com.example.pahanaedubackend.dao.ItemDAO;
import com.example.pahanaedubackend.dao.BillDAO;
import com.example.pahanaedubackend.dao.UserDAO;
import com.example.pahanaedubackend.service.AdminService;
import com.example.pahanaedubackend.service.CustomerService;
import com.example.pahanaedubackend.service.ItemService;
import com.example.pahanaedubackend.service.BillService;
import com.example.pahanaedubackend.util.ValidationUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Map;

/**
 * Test class for Factory Pattern implementation
 * Tests all factory classes and their functionality
 */
public class FactoryPatternTest {

    @BeforeEach
    void setUp() {
        // Reset factory instances before each test
        DAOFactory.resetInstance();
        ServiceFactory.resetInstance();
        ResponseFactory.resetInstance();
        ValidationFactory.resetInstance();
    }

    @Test
    void testDAOFactorySingleton() {
        // Test that DAOFactory returns the same instance
        DAOFactory factory1 = DAOFactory.getInstance();
        DAOFactory factory2 = DAOFactory.getInstance();
        
        assertSame(factory1, factory2, "DAOFactory should return the same instance (Singleton)");
    }

    @Test
    void testDAOFactoryCreatesDAOs() {
        DAOFactory factory = DAOFactory.getInstance();
        
        // Test that factory creates DAO instances
        AdminDAO adminDAO = factory.getAdminDAO();
        CustomerDAO customerDAO = factory.getCustomerDAO();
        ItemDAO itemDAO = factory.getItemDAO();
        BillDAO billDAO = factory.getBillDAO();
        UserDAO userDAO = factory.getUserDAO();
        
        assertNotNull(adminDAO, "AdminDAO should not be null");
        assertNotNull(customerDAO, "CustomerDAO should not be null");
        assertNotNull(itemDAO, "ItemDAO should not be null");
        assertNotNull(billDAO, "BillDAO should not be null");
        assertNotNull(userDAO, "UserDAO should not be null");
    }

    @Test
    void testDAOFactoryReturnsSameInstances() {
        DAOFactory factory = DAOFactory.getInstance();
        
        // Test that factory returns the same DAO instances (cached)
        AdminDAO adminDAO1 = factory.getAdminDAO();
        AdminDAO adminDAO2 = factory.getAdminDAO();
        
        assertSame(adminDAO1, adminDAO2, "DAOFactory should return the same AdminDAO instance");
    }

    @Test
    void testServiceFactorySingleton() {
        // Test that ServiceFactory returns the same instance
        ServiceFactory factory1 = ServiceFactory.getInstance();
        ServiceFactory factory2 = ServiceFactory.getInstance();
        
        assertSame(factory1, factory2, "ServiceFactory should return the same instance (Singleton)");
    }

    @Test
    void testServiceFactoryCreatesServices() {
        ServiceFactory factory = ServiceFactory.getInstance();
        
        // Test that factory creates Service instances
        AdminService adminService = factory.getAdminService();
        CustomerService customerService = factory.getCustomerService();
        ItemService itemService = factory.getItemService();
        BillService billService = factory.getBillService();
        
        assertNotNull(adminService, "AdminService should not be null");
        assertNotNull(customerService, "CustomerService should not be null");
        assertNotNull(itemService, "ItemService should not be null");
        assertNotNull(billService, "BillService should not be null");
    }

    @Test
    void testResponseFactorySingleton() {
        // Test that ResponseFactory returns the same instance
        ResponseFactory factory1 = ResponseFactory.getInstance();
        ResponseFactory factory2 = ResponseFactory.getInstance();
        
        assertSame(factory1, factory2, "ResponseFactory should return the same instance (Singleton)");
    }

    @Test
    void testResponseFactoryCreateResponse() {
        ResponseFactory factory = ResponseFactory.getInstance();
        
        // Test basic response creation
        Map<String, Object> successResponse = factory.createResponse(true, "Success", "Error");
        assertEquals(true, successResponse.get("success"));
        assertEquals("Success", successResponse.get("message"));
        
        Map<String, Object> errorResponse = factory.createResponse(false, "Success", "Error");
        assertEquals(false, errorResponse.get("success"));
        assertEquals("Error", errorResponse.get("message"));
    }

    @Test
    void testResponseFactorySuccessResponse() {
        ResponseFactory factory = ResponseFactory.getInstance();
        
        // Test success response creation
        Map<String, Object> response = factory.createSuccessResponse("Operation successful");
        assertEquals(true, response.get("success"));
        assertEquals("Operation successful", response.get("message"));
        assertNull(response.get("data"));
        
        // Test success response with data
        String testData = "test data";
        Map<String, Object> responseWithData = factory.createSuccessResponse("Success", testData);
        assertEquals(true, responseWithData.get("success"));
        assertEquals("Success", responseWithData.get("message"));
        assertEquals(testData, responseWithData.get("data"));
    }

    @Test
    void testResponseFactoryErrorResponse() {
        ResponseFactory factory = ResponseFactory.getInstance();
        
        // Test error response creation
        Map<String, Object> response = factory.createErrorResponse("Operation failed");
        assertEquals(false, response.get("success"));
        assertEquals("Operation failed", response.get("message"));
        
        // Test error response with error code
        Map<String, Object> responseWithCode = factory.createErrorResponse("Access denied", "FORBIDDEN");
        assertEquals(false, responseWithCode.get("success"));
        assertEquals("Access denied", responseWithCode.get("message"));
        assertEquals("FORBIDDEN", responseWithCode.get("errorCode"));
    }

    @Test
    void testResponseFactoryValidationErrorResponse() {
        ResponseFactory factory = ResponseFactory.getInstance();
        
        // Test validation error response
        Map<String, Object> response = factory.createValidationErrorResponse(
            "Validation failed", 
            Arrays.asList("Field1 is required", "Field2 is invalid")
        );
        
        assertEquals(false, response.get("success"));
        assertEquals("Validation failed", response.get("message"));
        assertNotNull(response.get("errors"));
    }

    @Test
    void testResponseFactorySpecialResponses() {
        ResponseFactory factory = ResponseFactory.getInstance();
        
        // Test unauthorized response
        Map<String, Object> unauthorizedResponse = factory.createUnauthorizedResponse();
        assertEquals(false, unauthorizedResponse.get("success"));
        assertEquals("Unauthorized: Admin login required", unauthorizedResponse.get("message"));
        assertEquals("UNAUTHORIZED", unauthorizedResponse.get("errorCode"));
        
        // Test not found response
        Map<String, Object> notFoundResponse = factory.createNotFoundResponse("User");
        assertEquals(false, notFoundResponse.get("success"));
        assertEquals("User not found", notFoundResponse.get("message"));
        assertEquals("NOT_FOUND", notFoundResponse.get("errorCode"));
    }

    @Test
    void testValidationFactorySingleton() {
        // Test that ValidationFactory returns the same instance
        ValidationFactory factory1 = ValidationFactory.getInstance();
        ValidationFactory factory2 = ValidationFactory.getInstance();
        
        assertSame(factory1, factory2, "ValidationFactory should return the same instance (Singleton)");
    }

    @Test
    void testValidationFactoryCustomerValidation() {
        ValidationFactory factory = ValidationFactory.getInstance();
        
        // Test valid customer data
        ValidationUtil.ValidationResult result = factory.validateCustomer(
            "ACC123456", "John Doe", "1234567890", "123 Main St", 100
        );
        
        assertTrue(result.isValid(), "Valid customer data should pass validation");
        
        // Test invalid customer data
        ValidationUtil.ValidationResult invalidResult = factory.validateCustomer(
            "", "", "", "", -1
        );
        
        assertFalse(invalidResult.isValid(), "Invalid customer data should fail validation");
        assertFalse(invalidResult.getErrors().isEmpty(), "Should have validation errors");
    }

    @Test
    void testValidationFactoryItemValidation() {
        ValidationFactory factory = ValidationFactory.getInstance();
        
        // Test valid item data
        ValidationUtil.ValidationResult result = factory.validateItem("Test Item", 10.50, 5);
        assertTrue(result.isValid(), "Valid item data should pass validation");
        
        // Test invalid item data
        ValidationUtil.ValidationResult invalidResult = factory.validateItem("", -1.0, -1);
        assertFalse(invalidResult.isValid(), "Invalid item data should fail validation");
    }

    @Test
    void testValidationFactoryLoginValidation() {
        ValidationFactory factory = ValidationFactory.getInstance();
        
        // Test valid login data
        ValidationUtil.ValidationResult result = factory.validateLogin("testuser", "password123");
        assertTrue(result.isValid(), "Valid login data should pass validation");
        
        // Test invalid login data
        ValidationUtil.ValidationResult invalidResult = factory.validateLogin("", "");
        assertFalse(invalidResult.isValid(), "Invalid login data should fail validation");
    }

    @Test
    void testValidationFactoryGenericValidation() {
        ValidationFactory factory = ValidationFactory.getInstance();
        
        // Test generic validation method
        ValidationUtil.ValidationResult customerResult = factory.validate(
            "customer", "ACC123456", "John Doe", "1234567890", "123 Main St", 100
        );
        assertTrue(customerResult.isValid(), "Generic customer validation should work");
        
        ValidationUtil.ValidationResult itemResult = factory.validate(
            "item", "Test Item", 10.50, 5
        );
        assertTrue(itemResult.isValid(), "Generic item validation should work");
        
        ValidationUtil.ValidationResult loginResult = factory.validate(
            "login", "testuser", "password123"
        );
        assertTrue(loginResult.isValid(), "Generic login validation should work");
    }

    @Test
    void testValidationFactoryInvalidEntityType() {
        ValidationFactory factory = ValidationFactory.getInstance();
        
        // Test invalid entity type
        assertThrows(IllegalArgumentException.class, () -> {
            factory.validate("invalid_type", "param1", "param2");
        }, "Should throw exception for invalid entity type");
    }
}
