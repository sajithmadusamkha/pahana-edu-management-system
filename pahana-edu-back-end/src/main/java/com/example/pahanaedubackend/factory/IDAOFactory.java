package com.example.pahanaedubackend.factory;

import com.example.pahanaedubackend.dao.AdminDAO;
import com.example.pahanaedubackend.dao.BillDAO;
import com.example.pahanaedubackend.dao.CustomerDAO;
import com.example.pahanaedubackend.dao.ItemDAO;
import com.example.pahanaedubackend.dao.UserDAO;

/**
 * Interface for DAO Factory following the Abstract Factory Design Pattern.
 * This interface defines the contract for creating DAO instances.
 * 
 * Benefits of using interface:
 * - True abstraction and polymorphism
 * - Easy to create different factory implementations
 * - Better testability with mock implementations
 * - Follows SOLID principles (Dependency Inversion)
 * - Allows for different DAO creation strategies
 */
public interface IDAOFactory {
    
    /**
     * Get AdminDAO instance
     * 
     * @return AdminDAO instance
     */
    AdminDAO getAdminDAO();
    
    /**
     * Get CustomerDAO instance
     * 
     * @return CustomerDAO instance
     */
    CustomerDAO getCustomerDAO();
    
    /**
     * Get ItemDAO instance
     * 
     * @return ItemDAO instance
     */
    ItemDAO getItemDAO();
    
    /**
     * Get BillDAO instance
     * 
     * @return BillDAO instance
     */
    BillDAO getBillDAO();
    
    /**
     * Get UserDAO instance
     * 
     * @return UserDAO instance
     */
    UserDAO getUserDAO();
}
