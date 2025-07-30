package com.example.pahanaedubackend.factory;

import com.example.pahanaedubackend.service.AdminService;
import com.example.pahanaedubackend.service.BillService;
import com.example.pahanaedubackend.service.CustomerService;
import com.example.pahanaedubackend.service.ItemService;

/**
 * Interface for Service Factory following the Abstract Factory Design Pattern.
 * This interface defines the contract for creating Service instances.
 * 
 * Benefits of using interface:
 * - True abstraction and polymorphism
 * - Easy to create different factory implementations
 * - Better testability with mock implementations
 * - Follows SOLID principles (Dependency Inversion)
 * - Allows for different service creation strategies (e.g., pooling, caching)
 */
public interface IServiceFactory {
    
    /**
     * Get AdminService instance
     * 
     * @return AdminService instance
     */
    AdminService getAdminService();
    
    /**
     * Get CustomerService instance
     * 
     * @return CustomerService instance
     */
    CustomerService getCustomerService();
    
    /**
     * Get ItemService instance
     * 
     * @return ItemService instance
     */
    ItemService getItemService();
    
    /**
     * Get BillService instance
     * 
     * @return BillService instance
     */
    BillService getBillService();
}
