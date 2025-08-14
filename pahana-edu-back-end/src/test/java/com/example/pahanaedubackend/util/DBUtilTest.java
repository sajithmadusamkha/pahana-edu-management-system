package com.example.pahanaedubackend.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for DBUtil - Happy Path scenarios only
 */
class DBUtilTest {

    @Test
    @DisplayName("Get Instance - Happy Path")
    void testGetInstance_Success() {
        // When
        DBUtil instance1 = DBUtil.getInstance();
        DBUtil instance2 = DBUtil.getInstance();
        
        // Then
        assertNotNull(instance1, "DBUtil instance should not be null");
        assertNotNull(instance2, "Second DBUtil instance should not be null");
        assertSame(instance1, instance2, "DBUtil should follow singleton pattern");
    }

    @Test
    @DisplayName("Get Connection - Happy Path")
    void testGetConnection_Success() {
        // Given
        DBUtil dbUtil = DBUtil.getInstance();
        
        // When & Then
        assertDoesNotThrow(() -> {
            Connection connection = dbUtil.getConnection();
            assertNotNull(connection, "Connection should not be null");
            assertFalse(connection.isClosed(), "Connection should be open");
            connection.close(); // Clean up
        }, "Getting connection should not throw exception");
    }

    @Test
    @DisplayName("Multiple Connections - Happy Path")
    void testMultipleConnections_Success() {
        // Given
        DBUtil dbUtil = DBUtil.getInstance();
        
        // When & Then
        assertDoesNotThrow(() -> {
            Connection conn1 = dbUtil.getConnection();
            Connection conn2 = dbUtil.getConnection();
            
            assertNotNull(conn1, "First connection should not be null");
            assertNotNull(conn2, "Second connection should not be null");
            assertNotSame(conn1, conn2, "Each call should return a new connection");
            
            assertFalse(conn1.isClosed(), "First connection should be open");
            assertFalse(conn2.isClosed(), "Second connection should be open");
            
            // Clean up
            conn1.close();
            conn2.close();
        }, "Getting multiple connections should not throw exception");
    }

    @Test
    @DisplayName("Connection Properties - Happy Path")
    void testConnectionProperties_Success() {
        // Given
        DBUtil dbUtil = DBUtil.getInstance();

        // When & Then
        assertDoesNotThrow(() -> {
            Connection connection = dbUtil.getConnection();

            // Test basic connection properties
            assertNotNull(connection.getMetaData(), "Connection metadata should be available");
            assertTrue(connection.isValid(5), "Connection should be valid");
            assertTrue(connection.getAutoCommit(), "Auto-commit should be true by default for JDBC connections");

            connection.close();
        }, "Testing connection properties should not throw exception");
    }

    @Test
    @DisplayName("Connection Close and Reopen - Happy Path")
    void testConnectionCloseAndReopen_Success() {
        // Given
        DBUtil dbUtil = DBUtil.getInstance();
        
        // When & Then
        assertDoesNotThrow(() -> {
            Connection connection1 = dbUtil.getConnection();
            assertFalse(connection1.isClosed(), "First connection should be open");
            
            connection1.close();
            assertTrue(connection1.isClosed(), "First connection should be closed after close()");
            
            Connection connection2 = dbUtil.getConnection();
            assertFalse(connection2.isClosed(), "New connection should be open");
            
            connection2.close();
        }, "Closing and reopening connections should work correctly");
    }

    @Test
    @DisplayName("Singleton Pattern Consistency - Happy Path")
    void testSingletonPatternConsistency_Success() {
        // When
        DBUtil instance1 = DBUtil.getInstance();
        DBUtil instance2 = DBUtil.getInstance();
        DBUtil instance3 = DBUtil.getInstance();
        
        // Then
        assertSame(instance1, instance2, "First and second instances should be same");
        assertSame(instance2, instance3, "Second and third instances should be same");
        assertSame(instance1, instance3, "First and third instances should be same");
    }

    @Test
    @DisplayName("Connection Functionality - Happy Path")
    void testConnectionFunctionality_Success() {
        // Given
        DBUtil dbUtil = DBUtil.getInstance();
        
        // When & Then
        assertDoesNotThrow(() -> {
            Connection connection = dbUtil.getConnection();
            
            // Test that we can create a statement
            assertNotNull(connection.createStatement(), "Should be able to create statement");
            
            // Test that we can prepare a statement
            assertNotNull(connection.prepareStatement("SELECT 1"), "Should be able to prepare statement");
            
            connection.close();
        }, "Basic connection functionality should work");
    }

    @Test
    @DisplayName("Database Connection URL Validation - Happy Path")
    void testDatabaseConnectionURL_Success() {
        // Given
        DBUtil dbUtil = DBUtil.getInstance();
        
        // When & Then
        assertDoesNotThrow(() -> {
            Connection connection = dbUtil.getConnection();
            String url = connection.getMetaData().getURL();
            
            assertNotNull(url, "Connection URL should not be null");
            assertTrue(url.contains("jdbc:mysql"), "URL should contain MySQL JDBC prefix");
            assertTrue(url.contains("pahana_edu"), "URL should contain database name");
            
            connection.close();
        }, "Database URL validation should succeed");
    }
}
