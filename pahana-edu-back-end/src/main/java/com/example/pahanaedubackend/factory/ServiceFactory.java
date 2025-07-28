package com.example.pahanaedubackend.factory;

import com.example.pahanaedubackend.service.AdminService;
import com.example.pahanaedubackend.service.BillService;
import com.example.pahanaedubackend.service.CustomerService;
import com.example.pahanaedubackend.service.ItemService;

/**
 * Factory class for creating Service instances using the Factory Design Pattern.
 * This class implements the Singleton pattern to ensure only one factory instance exists.
 * 
 * Benefits:
 * - Centralized service creation
 * - Easy to modify service implementations
 * - Loose coupling between controllers and services
 * - Consistent object creation across the application
 * - Easy to implement service caching or pooling if needed
 */
public class ServiceFactory {
    
    // Singleton instance
    private static ServiceFactory instance;
    
    // Service instances (cached for better performance)
    private AdminService adminService;
    private CustomerService customerService;
    private ItemService itemService;
    private BillService billService;
    
    /**
     * Private constructor to prevent direct instantiation
     */
    private ServiceFactory() {
        // Initialize service instances
        this.adminService = new AdminService();
        this.customerService = new CustomerService();
        this.itemService = new ItemService();
        this.billService = new BillService();
    }
    
    /**
     * Get the singleton instance of ServiceFactory
     * Thread-safe implementation using double-checked locking
     * 
     * @return ServiceFactory instance
     */
    public static ServiceFactory getInstance() {
        if (instance == null) {
            synchronized (ServiceFactory.class) {
                if (instance == null) {
                    instance = new ServiceFactory();
                }
            }
        }
        return instance;
    }
    
    /**
     * Get AdminService instance
     * 
     * @return AdminService instance
     */
    public AdminService getAdminService() {
        return adminService;
    }
    
    /**
     * Get CustomerService instance
     * 
     * @return CustomerService instance
     */
    public CustomerService getCustomerService() {
        return customerService;
    }
    
    /**
     * Get ItemService instance
     * 
     * @return ItemService instance
     */
    public ItemService getItemService() {
        return itemService;
    }
    
    /**
     * Get BillService instance
     * 
     * @return BillService instance
     */
    public BillService getBillService() {
        return billService;
    }
    
    /**
     * Reset the factory instance (useful for testing)
     * This method should only be used in test environments
     */
    public static void resetInstance() {
        instance = null;
    }
}
