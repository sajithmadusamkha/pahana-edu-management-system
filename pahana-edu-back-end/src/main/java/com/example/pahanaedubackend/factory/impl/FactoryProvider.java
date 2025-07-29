package com.example.pahanaedubackend.factory.impl;

import com.example.pahanaedubackend.factory.IDAOFactory;
import com.example.pahanaedubackend.factory.IResponseFactory;
import com.example.pahanaedubackend.factory.IServiceFactory;
import com.example.pahanaedubackend.factory.IValidationFactory;

/**
 * Central Factory Provider following the Abstract Factory Design Pattern.
 * This class provides a single access point to all factory instances in the application.
 * 
 * Benefits:
 * - Single point of access for all factories
 * - Easy to swap factory implementations
 * - Centralized factory management
 * - Better testability with dependency injection
 * - Follows SOLID principles
 */
public class FactoryProvider {
    
    // Singleton instance
    private static FactoryProvider instance;
    
    // Factory instances
    private IDAOFactory daoFactory;
    private IServiceFactory serviceFactory;
    private IResponseFactory responseFactory;
    private IValidationFactory validationFactory;
    
    /**
     * Private constructor to prevent direct instantiation
     */
    private FactoryProvider() {
        initializeFactories();
    }
    
    /**
     * Get the singleton instance of FactoryProvider
     * Thread-safe implementation using double-checked locking
     * 
     * @return FactoryProvider instance
     */
    public static FactoryProvider getInstance() {
        if (instance == null) {
            synchronized (FactoryProvider.class) {
                if (instance == null) {
                    instance = new FactoryProvider();
                }
            }
        }
        return instance;
    }
    
    /**
     * Initialize all factory instances
     * This method can be modified to use different implementations
     * based on configuration or environment
     */
    private void initializeFactories() {
        this.daoFactory = DAOFactory.getInstance();
        this.serviceFactory = ServiceFactory.getInstance();
        this.responseFactory = ResponseFactory.getInstance();
        this.validationFactory = ValidationFactory.getInstance();
    }
    
    /**
     * Get DAO Factory instance
     * 
     * @return IDAOFactory instance
     */
    public IDAOFactory getDAOFactory() {
        return daoFactory;
    }
    
    /**
     * Get Service Factory instance
     * 
     * @return IServiceFactory instance
     */
    public IServiceFactory getServiceFactory() {
        return serviceFactory;
    }
    
    /**
     * Get Response Factory instance
     * 
     * @return IResponseFactory instance
     */
    public IResponseFactory getResponseFactory() {
        return responseFactory;
    }
    
    /**
     * Get Validation Factory instance
     * 
     * @return IValidationFactory instance
     */
    public IValidationFactory getValidationFactory() {
        return validationFactory;
    }
    
    /**
     * Set DAO Factory implementation (useful for testing or different implementations)
     * 
     * @param daoFactory DAO Factory implementation
     */
    public void setDAOFactory(IDAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    /**
     * Set Service Factory implementation (useful for testing or different implementations)
     * 
     * @param serviceFactory Service Factory implementation
     */
    public void setServiceFactory(IServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }
    
    /**
     * Set Response Factory implementation (useful for testing or different implementations)
     * 
     * @param responseFactory Response Factory implementation
     */
    public void setResponseFactory(IResponseFactory responseFactory) {
        this.responseFactory = responseFactory;
    }
    
    /**
     * Set Validation Factory implementation (useful for testing or different implementations)
     * 
     * @param validationFactory Validation Factory implementation
     */
    public void setValidationFactory(IValidationFactory validationFactory) {
        this.validationFactory = validationFactory;
    }
    
    /**
     * Reset the factory provider instance (useful for testing)
     * This method should only be used in test environments
     */
    public static void resetInstance() {
        instance = null;
    }
    
    /**
     * Reset all factory instances to their default implementations
     * Useful for testing or reinitializing the system
     */
    public void resetFactories() {
        DAOFactory.resetInstance();
        ServiceFactory.resetInstance();
        ResponseFactory.resetInstance();
        ValidationFactory.resetInstance();
        initializeFactories();
    }
}
