package com.example.pahanaedubackend.servlet;

import com.example.pahanaedubackend.model.Admin;
import com.example.pahanaedubackend.service.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for LoginServlet - Happy Path scenarios only
 */
class LoginServletTest {

    private LoginServlet servlet;
    private AdminService adminService;

    @BeforeEach
    void setUp() {
        servlet = new LoginServlet();
        adminService = new AdminService();
    }

    @Test
    @DisplayName("Login Servlet - Initialization")
    void testServletInitialization() {
        // When
        LoginServlet newServlet = new LoginServlet();
        
        // Then
        assertNotNull(newServlet, "LoginServlet should be initialized successfully");
    }

    @Test
    @DisplayName("Login - Admin Service Integration")
    void testAdminServiceIntegration() {
        // Given
        Admin admin = new Admin();
        String uniqueUsername = "logintest" + System.currentTimeMillis();
        admin.setUsername(uniqueUsername);
        admin.setFullName("Login Test User");
        admin.setEmail("logintest@example.com");
        String password = "loginpassword123";
        
        // Register admin first
        boolean registered = adminService.registerAdmin(admin, password);
        assertTrue(registered, "Admin should be registered successfully");
        
        // When - Test login
        Admin loginResult = adminService.login(uniqueUsername, password);
        
        // Then
        assertNotNull(loginResult, "Login should return admin object");
        assertEquals(uniqueUsername, loginResult.getUsername(), "Username should match");
        assertEquals("Login Test User", loginResult.getFullName(), "Full name should match");
        assertEquals("logintest@example.com", loginResult.getEmail(), "Email should match");
    }

    @Test
    @DisplayName("Login - Valid Credentials")
    void testLoginWithValidCredentials() {
        // Given
        Admin admin = new Admin();
        String uniqueUsername = "validuser" + System.currentTimeMillis();
        admin.setUsername(uniqueUsername);
        admin.setFullName("Valid User");
        admin.setEmail("valid@example.com");
        String password = "validpassword123";
        
        // Register admin first
        adminService.registerAdmin(admin, password);
        
        // When
        Admin result = adminService.login(uniqueUsername, password);
        
        // Then
        assertNotNull(result, "Login with valid credentials should succeed");
        assertEquals(uniqueUsername, result.getUsername(), "Returned username should match");
    }

    @Test
    @DisplayName("Login - Multiple Login Attempts")
    void testMultipleLoginAttempts() {
        // Given
        Admin admin = new Admin();
        String uniqueUsername = "multiuser" + System.currentTimeMillis();
        admin.setUsername(uniqueUsername);
        admin.setFullName("Multi User");
        admin.setEmail("multi@example.com");
        String password = "multipassword123";
        
        // Register admin first
        adminService.registerAdmin(admin, password);
        
        // When - Multiple login attempts
        Admin result1 = adminService.login(uniqueUsername, password);
        Admin result2 = adminService.login(uniqueUsername, password);
        Admin result3 = adminService.login(uniqueUsername, password);
        
        // Then
        assertNotNull(result1, "First login should succeed");
        assertNotNull(result2, "Second login should succeed");
        assertNotNull(result3, "Third login should succeed");
        
        assertEquals(uniqueUsername, result1.getUsername(), "First login username should match");
        assertEquals(uniqueUsername, result2.getUsername(), "Second login username should match");
        assertEquals(uniqueUsername, result3.getUsername(), "Third login username should match");
    }

    @Test
    @DisplayName("Login - Admin Object Properties")
    void testAdminObjectProperties() {
        // Given
        Admin admin = new Admin();
        String uniqueUsername = "propuser" + System.currentTimeMillis();
        admin.setUsername(uniqueUsername);
        admin.setFullName("Property Test User");
        admin.setEmail("prop@example.com");
        String password = "proppassword123";
        
        // Register admin first
        adminService.registerAdmin(admin, password);
        
        // When
        Admin loginResult = adminService.login(uniqueUsername, password);
        
        // Then
        assertNotNull(loginResult, "Login result should not be null");
        assertNotNull(loginResult.getUsername(), "Username should not be null");
        assertNotNull(loginResult.getFullName(), "Full name should not be null");
        assertNotNull(loginResult.getEmail(), "Email should not be null");
        assertNotNull(loginResult.getPassword(), "Password should not be null (hashed)");
        
        assertTrue(loginResult.getUsername().length() > 0, "Username should not be empty");
        assertTrue(loginResult.getFullName().length() > 0, "Full name should not be empty");
        assertTrue(loginResult.getEmail().length() > 0, "Email should not be empty");
    }

    @Test
    @DisplayName("Login - Different Admin Accounts")
    void testDifferentAdminAccounts() {
        // Given
        String timestamp = String.valueOf(System.currentTimeMillis());
        
        Admin admin1 = new Admin();
        admin1.setUsername("admin1" + timestamp);
        admin1.setFullName("Admin One");
        admin1.setEmail("admin1@example.com");
        String password1 = "password1";
        
        Admin admin2 = new Admin();
        admin2.setUsername("admin2" + timestamp);
        admin2.setFullName("Admin Two");
        admin2.setEmail("admin2@example.com");
        String password2 = "password2";
        
        // Register both admins
        adminService.registerAdmin(admin1, password1);
        adminService.registerAdmin(admin2, password2);
        
        // When
        Admin login1 = adminService.login("admin1" + timestamp, password1);
        Admin login2 = adminService.login("admin2" + timestamp, password2);
        
        // Then
        assertNotNull(login1, "First admin login should succeed");
        assertNotNull(login2, "Second admin login should succeed");
        
        assertEquals("admin1" + timestamp, login1.getUsername(), "First admin username should match");
        assertEquals("admin2" + timestamp, login2.getUsername(), "Second admin username should match");
        
        assertEquals("Admin One", login1.getFullName(), "First admin full name should match");
        assertEquals("Admin Two", login2.getFullName(), "Second admin full name should match");
    }

    @Test
    @DisplayName("Login - Password Verification")
    void testPasswordVerification() {
        // Given
        Admin admin = new Admin();
        String uniqueUsername = "passuser" + System.currentTimeMillis();
        admin.setUsername(uniqueUsername);
        admin.setFullName("Password User");
        admin.setEmail("pass@example.com");
        String correctPassword = "correctpassword123";
        
        // Register admin
        adminService.registerAdmin(admin, correctPassword);
        
        // When - Login with correct password
        Admin loginResult = adminService.login(uniqueUsername, correctPassword);
        
        // Then
        assertNotNull(loginResult, "Login with correct password should succeed");
        assertEquals(uniqueUsername, loginResult.getUsername(), "Username should match");
    }

    @Test
    @DisplayName("Login - Service Method Integration")
    void testServiceMethodIntegration() {
        // Given
        String uniqueUsername = "serviceuser" + System.currentTimeMillis();
        String password = "servicepassword123";
        
        Admin admin = new Admin();
        admin.setUsername(uniqueUsername);
        admin.setFullName("Service User");
        admin.setEmail("service@example.com");
        
        // When
        boolean registered = adminService.registerAdmin(admin, password);
        Admin loginResult = adminService.login(uniqueUsername, password);
        
        // Then
        assertTrue(registered, "Registration should succeed");
        assertNotNull(loginResult, "Login should succeed after registration");
        assertEquals(uniqueUsername, loginResult.getUsername(), "Login should return correct admin");
    }
}
