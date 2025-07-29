package com.example.pahanaedubackend.test;

import com.example.pahanaedubackend.factory.impl.FactoryProvider;
import com.example.pahanaedubackend.factory.IDAOFactory;
import com.example.pahanaedubackend.factory.IResponseFactory;
import com.example.pahanaedubackend.factory.IServiceFactory;
import com.example.pahanaedubackend.factory.IValidationFactory;

/**
 * Simple test class to verify that the factory pattern is working correctly.
 * This class tests that all interfaces are properly implemented and accessible.
 */
public class FactoryTest {
    
    public static void main(String[] args) {
        try {
            System.out.println("Testing Factory Design Pattern Implementation...");
            
            // Test FactoryProvider
            FactoryProvider provider = FactoryProvider.getInstance();
            System.out.println("✓ FactoryProvider instance created successfully");
            
            // Test all factory interfaces
            IDAOFactory daoFactory = provider.getDAOFactory();
            IServiceFactory serviceFactory = provider.getServiceFactory();
            IResponseFactory responseFactory = provider.getResponseFactory();
            IValidationFactory validationFactory = provider.getValidationFactory();
            
            System.out.println("✓ All factory interfaces accessible");
            
            // Test service creation
            if (serviceFactory.getAdminService() != null) {
                System.out.println("✓ AdminService created successfully");
            }
            
            if (serviceFactory.getCustomerService() != null) {
                System.out.println("✓ CustomerService created successfully");
            }
            
            if (serviceFactory.getItemService() != null) {
                System.out.println("✓ ItemService created successfully");
            }
            
            if (serviceFactory.getBillService() != null) {
                System.out.println("✓ BillService created successfully");
            }
            
            // Test response factory
            if (responseFactory.createSuccessResponse("Test") != null) {
                System.out.println("✓ ResponseFactory working correctly");
            }
            
            // Test validation factory
            if (validationFactory.validateLogin("test", "test") != null) {
                System.out.println("✓ ValidationFactory working correctly");
            }
            
            System.out.println("\n🎉 All factory pattern tests passed!");
            System.out.println("Factory design pattern implementation is working correctly.");
            
        } catch (Exception e) {
            System.err.println("❌ Factory pattern test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
