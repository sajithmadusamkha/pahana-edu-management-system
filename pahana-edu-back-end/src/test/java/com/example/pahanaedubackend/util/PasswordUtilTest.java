package com.example.pahanaedubackend.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for PasswordUtil - Happy Path scenarios only
 */
class PasswordUtilTest {

    @Test
    @DisplayName("Hash Password - Happy Path")
    void testHashPassword_Success() {
        // Given
        String plainPassword = "password123";
        
        // When
        String hashedPassword = PasswordUtil.hashPassword(plainPassword);
        
        // Then
        assertNotNull(hashedPassword, "Hashed password should not be null");
        assertNotEquals(plainPassword, hashedPassword, "Hashed password should be different from plain password");
        assertTrue(hashedPassword.length() > 0, "Hashed password should not be empty");
        assertEquals(64, hashedPassword.length(), "SHA-256 hash should be 64 characters long");
    }

    @Test
    @DisplayName("Hash Password Consistency - Same Input Same Output")
    void testHashPassword_Consistency() {
        // Given
        String plainPassword = "testPassword456";
        
        // When
        String hash1 = PasswordUtil.hashPassword(plainPassword);
        String hash2 = PasswordUtil.hashPassword(plainPassword);
        
        // Then
        assertEquals(hash1, hash2, "Same password should produce same hash");
    }

    @Test
    @DisplayName("Check Password - Happy Path")
    void testCheckPassword_Success() {
        // Given
        String plainPassword = "mySecurePassword";
        String hashedPassword = PasswordUtil.hashPassword(plainPassword);
        
        // When
        boolean result = PasswordUtil.checkPassword(plainPassword, hashedPassword);
        
        // Then
        assertTrue(result, "Password check should return true for correct password");
    }

    @Test
    @DisplayName("Check Password with Different Passwords")
    void testCheckPassword_WithDifferentPasswords() {
        // Given
        String password1 = "password123";
        String password2 = "differentPassword456";
        String hashedPassword1 = PasswordUtil.hashPassword(password1);
        
        // When
        boolean result = PasswordUtil.checkPassword(password2, hashedPassword1);
        
        // Then
        assertFalse(result, "Password check should return false for incorrect password");
    }

    @Test
    @DisplayName("Hash Password with Special Characters")
    void testHashPassword_WithSpecialCharacters() {
        // Given
        String plainPassword = "P@ssw0rd!#$%";
        
        // When
        String hashedPassword = PasswordUtil.hashPassword(plainPassword);
        
        // Then
        assertNotNull(hashedPassword, "Should handle special characters");
        assertEquals(64, hashedPassword.length(), "Hash length should be consistent");
        assertTrue(PasswordUtil.checkPassword(plainPassword, hashedPassword), "Should verify correctly");
    }

    @Test
    @DisplayName("Hash Password with Numbers")
    void testHashPassword_WithNumbers() {
        // Given
        String plainPassword = "123456789";
        
        // When
        String hashedPassword = PasswordUtil.hashPassword(plainPassword);
        
        // Then
        assertNotNull(hashedPassword, "Should handle numeric passwords");
        assertEquals(64, hashedPassword.length(), "Hash length should be consistent");
        assertTrue(PasswordUtil.checkPassword(plainPassword, hashedPassword), "Should verify correctly");
    }

    @Test
    @DisplayName("Hash Password with Long String")
    void testHashPassword_WithLongString() {
        // Given
        String plainPassword = "ThisIsAVeryLongPasswordThatShouldStillWorkCorrectlyWithTheHashingFunction";
        
        // When
        String hashedPassword = PasswordUtil.hashPassword(plainPassword);
        
        // Then
        assertNotNull(hashedPassword, "Should handle long passwords");
        assertEquals(64, hashedPassword.length(), "Hash length should be consistent regardless of input length");
        assertTrue(PasswordUtil.checkPassword(plainPassword, hashedPassword), "Should verify correctly");
    }

    @Test
    @DisplayName("Check Password - Multiple Verifications")
    void testCheckPassword_MultipleVerifications() {
        // Given
        String plainPassword = "testMultiple123";
        String hashedPassword = PasswordUtil.hashPassword(plainPassword);
        
        // When & Then
        assertTrue(PasswordUtil.checkPassword(plainPassword, hashedPassword), "First verification should succeed");
        assertTrue(PasswordUtil.checkPassword(plainPassword, hashedPassword), "Second verification should succeed");
        assertTrue(PasswordUtil.checkPassword(plainPassword, hashedPassword), "Third verification should succeed");
    }

    @Test
    @DisplayName("Hash Different Passwords Produce Different Hashes")
    void testHashPassword_DifferentInputsDifferentOutputs() {
        // Given
        String password1 = "password1";
        String password2 = "password2";
        
        // When
        String hash1 = PasswordUtil.hashPassword(password1);
        String hash2 = PasswordUtil.hashPassword(password2);
        
        // Then
        assertNotEquals(hash1, hash2, "Different passwords should produce different hashes");
    }
}
