package com.example.pahanaedubackend.service;

import com.example.pahanaedubackend.model.Admin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for AdminService - Happy Path scenarios only
 */
class AdminServiceTest {

    private AdminService adminService;
    private Admin testAdmin;

    @BeforeEach
    void setUp() {
        adminService = new AdminService();
        testAdmin = new Admin();
        // Use timestamp to ensure unique username for each test run
        String uniqueUsername = "testadmin" + System.currentTimeMillis();
        testAdmin.setUsername(uniqueUsername);
        testAdmin.setFullName("Test Admin");
        testAdmin.setEmail("test@admin.com");
    }

    @Test
    @DisplayName("Admin Registration - Happy Path")
    void testRegisterAdmin_Success() {
        // Given
        String plainPassword = "password123";
        
        // When
        boolean result = adminService.registerAdmin(testAdmin, plainPassword);
        
        // Then
        assertTrue(result, "Admin registration should be successful");
        assertNotNull(testAdmin.getPassword(), "Password should be hashed and set");
        assertNotEquals(plainPassword, testAdmin.getPassword(), "Password should be hashed, not plain text");
    }

    @Test
    @DisplayName("Admin Login - Happy Path")
    void testLogin_Success() {
        // Given
        String plainPassword = "password123";
        
        // First register the admin
        adminService.registerAdmin(testAdmin, plainPassword);
        
        // When
        Admin loggedInAdmin = adminService.login(testAdmin.getUsername(), plainPassword);
        
        // Then
        assertNotNull(loggedInAdmin, "Login should return admin object");
        assertEquals(testAdmin.getUsername(), loggedInAdmin.getUsername(), "Username should match");
        assertEquals(testAdmin.getFullName(), loggedInAdmin.getFullName(), "Full name should match");
        assertEquals(testAdmin.getEmail(), loggedInAdmin.getEmail(), "Email should match");
    }

    @Test
    @DisplayName("Admin Registration with Valid Data")
    void testRegisterAdmin_WithValidData() {
        // Given
        Admin admin = new Admin();
        String uniqueUsername = "admin" + System.currentTimeMillis();
        admin.setUsername(uniqueUsername);
        admin.setFullName("John Doe");
        admin.setEmail("john.doe@example.com");
        String password = "securePassword123";

        // When
        boolean result = adminService.registerAdmin(admin, password);

        // Then
        assertTrue(result, "Registration with valid data should succeed");
        assertEquals(uniqueUsername, admin.getUsername(), "Username should be preserved");
        assertEquals("John Doe", admin.getFullName(), "Full name should be preserved");
        assertEquals("john.doe@example.com", admin.getEmail(), "Email should be preserved");
    }

    @Test
    @DisplayName("Admin Login with Correct Credentials")
    void testLogin_WithCorrectCredentials() {
        // Given
        Admin admin = new Admin();
        String uniqueUsername = "logintest" + System.currentTimeMillis();
        admin.setUsername(uniqueUsername);
        admin.setFullName("Login Test User");
        admin.setEmail("logintest@example.com");
        String password = "testPassword456";

        // Register first
        adminService.registerAdmin(admin, password);

        // When
        Admin result = adminService.login(uniqueUsername, password);

        // Then
        assertNotNull(result, "Login should succeed with correct credentials");
        assertEquals(uniqueUsername, result.getUsername(), "Returned admin should have correct username");
        assertEquals("Login Test User", result.getFullName(), "Returned admin should have correct full name");
        assertEquals("logintest@example.com", result.getEmail(), "Returned admin should have correct email");
    }
}
