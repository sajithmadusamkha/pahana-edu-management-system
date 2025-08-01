package com.example.pahanaedubackend.servlet;

import com.example.pahanaedubackend.model.Admin;
import com.example.pahanaedubackend.service.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for AdminRegisterServlet - Happy Path scenarios only
 * Note: These tests focus on the business logic and integration aspects
 */
class AdminRegisterServletTest {

    private AdminRegisterServlet servlet;
    private AdminService adminService;

    @BeforeEach
    void setUp() {
        servlet = new AdminRegisterServlet();
        adminService = new AdminService();
    }

    @Test
    @DisplayName("Admin Registration - Servlet Initialization")
    void testServletInitialization() {
        // When
        AdminRegisterServlet newServlet = new AdminRegisterServlet();

        // Then
        assertNotNull(newServlet, "Servlet should be initialized successfully");
    }

    @Test
    @DisplayName("Admin Registration - Valid Admin Object Creation")
    void testAdminObjectCreation() {
        // Given
        Admin admin = new Admin();
        admin.setUsername("testuser");
        admin.setFullName("Test User");
        admin.setEmail("test@example.com");

        // When & Then
        assertEquals("testuser", admin.getUsername(), "Username should be set correctly");
        assertEquals("Test User", admin.getFullName(), "Full name should be set correctly");
        assertEquals("test@example.com", admin.getEmail(), "Email should be set correctly");
    }

    @Test
    @DisplayName("Admin Registration - Service Integration Test")
    void testServiceIntegration() {
        // Given
        AdminService adminService = new AdminService();
        Admin admin = new Admin();
        admin.setUsername("servicetest" + System.currentTimeMillis());
        admin.setFullName("Service Test User");
        admin.setEmail("servicetest@example.com");
        String password = "testpassword123";
        
        // When
        boolean result = adminService.registerAdmin(admin, password);
        
        // Then
        assertTrue(result, "Admin service should register admin successfully");
        assertNotNull(admin.getPassword(), "Password should be hashed and set");
    }

    @Test
    @DisplayName("Admin Registration - Multiple Valid Registrations")
    void testMultipleValidRegistrations() {
        // Given
        AdminService adminService = new AdminService();
        
        Admin admin1 = new Admin();
        admin1.setUsername("user1" + System.currentTimeMillis());
        admin1.setFullName("User One");
        admin1.setEmail("user1@example.com");
        
        Admin admin2 = new Admin();
        admin2.setUsername("user2" + System.currentTimeMillis());
        admin2.setFullName("User Two");
        admin2.setEmail("user2@example.com");
        
        // When
        boolean result1 = adminService.registerAdmin(admin1, "password1");
        boolean result2 = adminService.registerAdmin(admin2, "password2");
        
        // Then
        assertTrue(result1, "First admin registration should succeed");
        assertTrue(result2, "Second admin registration should succeed");
    }

    @Test
    @DisplayName("Admin Registration - Password Hashing Verification")
    void testPasswordHashingVerification() {
        // Given
        AdminService adminService = new AdminService();
        Admin admin = new Admin();
        admin.setUsername("hashtest" + System.currentTimeMillis());
        admin.setFullName("Hash Test User");
        admin.setEmail("hashtest@example.com");
        String plainPassword = "plainPassword123";
        
        // When
        adminService.registerAdmin(admin, plainPassword);
        
        // Then
        assertNotNull(admin.getPassword(), "Password should be set");
        assertNotEquals(plainPassword, admin.getPassword(), "Password should be hashed");
        assertTrue(admin.getPassword().length() > 0, "Hashed password should not be empty");
    }

    @Test
    @DisplayName("Admin Registration - Successful Registration Flow")
    void testSuccessfulRegistrationFlow() {
        // Given
        Admin admin = new Admin();
        String uniqueUsername = "flowtest" + System.currentTimeMillis();
        admin.setUsername(uniqueUsername);
        admin.setFullName("Flow Test User");
        admin.setEmail("flowtest@example.com");
        String password = "flowpassword123";

        // When
        boolean registrationResult = adminService.registerAdmin(admin, password);

        // Then - Verify registration was successful
        assertTrue(registrationResult, "Registration should be successful");

        // Verify login works with registered admin
        Admin loginResult = adminService.login(uniqueUsername, password);
        assertNotNull(loginResult, "Should be able to login with registered admin");
        assertEquals(uniqueUsername, loginResult.getUsername(), "Logged in admin should have correct username");
    }
}
