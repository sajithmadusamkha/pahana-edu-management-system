package com.example.pahanaedubackend.factory;

import com.example.pahanaedubackend.dao.AdminDAO;
import com.example.pahanaedubackend.dao.BillDAO;
import com.example.pahanaedubackend.dao.CustomerDAO;
import com.example.pahanaedubackend.dao.ItemDAO;
import com.example.pahanaedubackend.dao.UserDAO;

/**
 * Factory class for creating DAO instances using the Factory Design Pattern.
 * This class implements the Singleton pattern to ensure only one factory instance exists.
 * 
 * Benefits:
 * - Centralized object creation
 * - Easy to modify DAO implementations
 * - Loose coupling between services and DAOs
 * - Consistent object creation across the application
 */
public class DAOFactory {
    
    // Singleton instance
    private static DAOFactory instance;
    
    // DAO instances (can be cached for better performance)
    private AdminDAO adminDAO;
    private CustomerDAO customerDAO;
    private ItemDAO itemDAO;
    private BillDAO billDAO;
    private UserDAO userDAO;
    
    /**
     * Private constructor to prevent direct instantiation
     */
    private DAOFactory() {
        // Initialize DAO instances
        this.adminDAO = new AdminDAO();
        this.customerDAO = new CustomerDAO();
        this.itemDAO = new ItemDAO();
        this.billDAO = new BillDAO();
        this.userDAO = new UserDAO();
    }
    
    /**
     * Get the singleton instance of DAOFactory
     * Thread-safe implementation using double-checked locking
     * 
     * @return DAOFactory instance
     */
    public static DAOFactory getInstance() {
        if (instance == null) {
            synchronized (DAOFactory.class) {
                if (instance == null) {
                    instance = new DAOFactory();
                }
            }
        }
        return instance;
    }
    
    /**
     * Get AdminDAO instance
     * 
     * @return AdminDAO instance
     */
    public AdminDAO getAdminDAO() {
        return adminDAO;
    }
    
    /**
     * Get CustomerDAO instance
     * 
     * @return CustomerDAO instance
     */
    public CustomerDAO getCustomerDAO() {
        return customerDAO;
    }
    
    /**
     * Get ItemDAO instance
     * 
     * @return ItemDAO instance
     */
    public ItemDAO getItemDAO() {
        return itemDAO;
    }
    
    /**
     * Get BillDAO instance
     * 
     * @return BillDAO instance
     */
    public BillDAO getBillDAO() {
        return billDAO;
    }
    
    /**
     * Get UserDAO instance
     * 
     * @return UserDAO instance
     */
    public UserDAO getUserDAO() {
        return userDAO;
    }
    
    /**
     * Reset the factory instance (useful for testing)
     * This method should only be used in test environments
     */
    public static void resetInstance() {
        instance = null;
    }
}
