package com.example.pahanaedubackend.servlet;

import com.example.pahanaedubackend.model.Item;
import com.example.pahanaedubackend.service.ItemService;
import com.example.pahanaedubackend.util.ValidationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for CreateItemServlet - Happy Path scenarios only
 */
class CreateItemServletTest {

    private CreateItemServlet servlet;
    private ItemService itemService;

    @BeforeEach
    void setUp() {
        servlet = new CreateItemServlet();
        itemService = new ItemService();
    }

    @Test
    @DisplayName("Create Item Servlet - Initialization")
    void testServletInitialization() {
        // When
        CreateItemServlet newServlet = new CreateItemServlet();
        
        // Then
        assertNotNull(newServlet, "CreateItemServlet should be initialized successfully");
    }

    @Test
    @DisplayName("Create Item - Valid Item Object")
    void testValidItemObject() {
        // Given
        Item item = new Item();
        item.setName("Test Item");
        item.setPrice(25.99);
        item.setQuantity(100);
        
        // When & Then
        assertEquals("Test Item", item.getName(), "Item name should be set correctly");
        assertEquals(25.99, item.getPrice(), 0.01, "Item price should be set correctly");
        assertEquals(100, item.getQuantity(), "Item quantity should be set correctly");
    }

    @Test
    @DisplayName("Create Item - Service Integration")
    void testItemServiceIntegration() {
        // Given
        Item item = new Item();
        item.setName("Service Test Item " + System.currentTimeMillis());
        item.setPrice(15.50);
        item.setQuantity(50);
        
        // When
        boolean result = itemService.createItem(item);
        
        // Then
        assertTrue(result, "Item service should create item successfully");
    }

    @Test
    @DisplayName("Create Item - Validation Integration")
    void testValidationIntegration() {
        // Given
        String name = "Valid Item";
        double price = 99.99;
        int quantity = 25;
        
        // When
        ValidationUtil.ValidationResult result = ValidationUtil.validateItem(name, price, quantity);
        
        // Then
        assertTrue(result.isValid(), "Valid item data should pass validation");
        assertTrue(result.getErrors().isEmpty(), "Valid item should have no validation errors");
    }

    @Test
    @DisplayName("Create Item - Multiple Valid Items")
    void testMultipleValidItems() {
        // Given
        String timestamp = String.valueOf(System.currentTimeMillis());
        
        Item item1 = new Item();
        item1.setName("Item One " + timestamp);
        item1.setPrice(10.99);
        item1.setQuantity(100);
        
        Item item2 = new Item();
        item2.setName("Item Two " + timestamp);
        item2.setPrice(20.99);
        item2.setQuantity(200);
        
        // When
        boolean result1 = itemService.createItem(item1);
        boolean result2 = itemService.createItem(item2);
        
        // Then
        assertTrue(result1, "First item creation should succeed");
        assertTrue(result2, "Second item creation should succeed");
    }

    @Test
    @DisplayName("Create Item - Different Valid Data Sets")
    void testDifferentValidDataSets() {
        // Given
        String timestamp = String.valueOf(System.currentTimeMillis());
        
        // Test data set 1: Minimum valid values
        Item item1 = new Item();
        item1.setName("AB");  // Minimum length
        item1.setPrice(0.01);  // Minimum price
        item1.setQuantity(1);  // Minimum quantity
        
        // Test data set 2: Maximum valid values
        Item item2 = new Item();
        item2.setName("Very Long Item Name That Is Still Valid For Testing Purposes " + timestamp);
        item2.setPrice(99999.99);  // High but valid price
        item2.setQuantity(9999);  // High but valid quantity
        
        // Test data set 3: Standard values
        Item item3 = new Item();
        item3.setName("Standard Item " + timestamp);
        item3.setPrice(50.00);
        item3.setQuantity(100);
        
        // When
        boolean result1 = itemService.createItem(item1);
        boolean result2 = itemService.createItem(item2);
        boolean result3 = itemService.createItem(item3);
        
        // Then
        assertTrue(result1, "Item with minimum valid data should be created successfully");
        assertTrue(result2, "Item with maximum valid data should be created successfully");
        assertTrue(result3, "Item with standard valid data should be created successfully");
    }

    @Test
    @DisplayName("Create Item - Price Validation Success Cases")
    void testPriceValidationSuccessCases() {
        // Test Case 1: Minimum valid price
        ValidationUtil.ValidationResult result1 = ValidationUtil.validateItem(
            "Min Price Item", 0.01, 10);
        assertTrue(result1.isValid(), "Minimum valid price should pass validation");
        
        // Test Case 2: Standard price
        ValidationUtil.ValidationResult result2 = ValidationUtil.validateItem(
            "Standard Price Item", 25.99, 50);
        assertTrue(result2.isValid(), "Standard price should pass validation");
        
        // Test Case 3: High but valid price
        ValidationUtil.ValidationResult result3 = ValidationUtil.validateItem(
            "High Price Item", 999.99, 5);
        assertTrue(result3.isValid(), "High but valid price should pass validation");
        
        // Test Case 4: Decimal price
        ValidationUtil.ValidationResult result4 = ValidationUtil.validateItem(
            "Decimal Price Item", 123.45, 20);
        assertTrue(result4.isValid(), "Decimal price should pass validation");
    }

    @Test
    @DisplayName("Create Item - Quantity Validation Success Cases")
    void testQuantityValidationSuccessCases() {
        // Test Case 1: Minimum valid quantity
        ValidationUtil.ValidationResult result1 = ValidationUtil.validateItem(
            "Min Quantity Item", 10.00, 1);
        assertTrue(result1.isValid(), "Minimum valid quantity should pass validation");
        
        // Test Case 2: Standard quantity
        ValidationUtil.ValidationResult result2 = ValidationUtil.validateItem(
            "Standard Quantity Item", 15.00, 50);
        assertTrue(result2.isValid(), "Standard quantity should pass validation");
        
        // Test Case 3: High but valid quantity
        ValidationUtil.ValidationResult result3 = ValidationUtil.validateItem(
            "High Quantity Item", 5.00, 1000);
        assertTrue(result3.isValid(), "High but valid quantity should pass validation");
    }

    @Test
    @DisplayName("Create Item - Name Validation Success Cases")
    void testNameValidationSuccessCases() {
        // Test Case 1: Short but valid name
        ValidationUtil.ValidationResult result1 = ValidationUtil.validateItem(
            "AB", 10.00, 10);
        assertTrue(result1.isValid(), "Short but valid name should pass validation");
        
        // Test Case 2: Standard name
        ValidationUtil.ValidationResult result2 = ValidationUtil.validateItem(
            "Standard Item Name", 20.00, 20);
        assertTrue(result2.isValid(), "Standard name should pass validation");
        
        // Test Case 3: Long but valid name
        ValidationUtil.ValidationResult result3 = ValidationUtil.validateItem(
            "Very Long Item Name That Contains Multiple Words", 30.00, 30);
        assertTrue(result3.isValid(), "Long but valid name should pass validation");
        
        // Test Case 4: Name with numbers
        ValidationUtil.ValidationResult result4 = ValidationUtil.validateItem(
            "Item 123 Version 2", 40.00, 40);
        assertTrue(result4.isValid(), "Name with numbers should pass validation");
    }

    @Test
    @DisplayName("Create Item - Complete Creation Flow")
    void testCompleteCreationFlow() {
        // Given
        Item item = new Item();
        String uniqueName = "Flow Test Item " + System.currentTimeMillis();
        item.setName(uniqueName);
        item.setPrice(75.50);
        item.setQuantity(150);
        
        // Validate first
        ValidationUtil.ValidationResult validation = ValidationUtil.validateItem(
            item.getName(), item.getPrice(), item.getQuantity());
        
        // Then create if valid
        assertTrue(validation.isValid(), "Item data should be valid");
        
        boolean creationResult = itemService.createItem(item);
        assertTrue(creationResult, "Item creation should succeed");
    }

    @Test
    @DisplayName("Create Item - Various Price Formats")
    void testVariousPriceFormats() {
        // Given
        String timestamp = String.valueOf(System.currentTimeMillis());
        
        Item item1 = new Item();
        item1.setName("Whole Price Item " + timestamp);
        item1.setPrice(25.00);  // Whole number price
        item1.setQuantity(10);
        
        Item item2 = new Item();
        item2.setName("Decimal Price Item " + timestamp);
        item2.setPrice(25.99);  // Two decimal places
        item2.setQuantity(20);
        
        Item item3 = new Item();
        item3.setName("Single Decimal Item " + timestamp);
        item3.setPrice(25.5);   // One decimal place
        item3.setQuantity(30);
        
        // When
        boolean result1 = itemService.createItem(item1);
        boolean result2 = itemService.createItem(item2);
        boolean result3 = itemService.createItem(item3);
        
        // Then
        assertTrue(result1, "Whole number price item should be created successfully");
        assertTrue(result2, "Two decimal price item should be created successfully");
        assertTrue(result3, "One decimal price item should be created successfully");
    }
}
