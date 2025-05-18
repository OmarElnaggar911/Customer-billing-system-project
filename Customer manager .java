package com.billing.controller;
//separate package for each class but all of them inside the source packages.
//we called the other classes by importing thier packages 

import com.billing.database.DatabaseHandler; //package of database
import com.billing.models.Customer; // customer data package 
import java.util.List; //library

/**
 * a class to manage customer records like adding, editing, and recalling customer information.
 */
public class CustomerManager {
    private DatabaseHandler dbHandler;
    
    /**
     * Constructor initializes CustomerManager object 
     */
    public CustomerManager() {
        dbHandler = new DatabaseHandler(); //connecting the Database
    }
    
    
     //Adds new customer 
     
    public void addCustomer(Customer customer) {
        dbHandler.saveDatabase(customer);
    }
    
    //edit info of existing customer in our databse
    
    public void editCustomer(Customer customer) {
        dbHandler.updateDatabase(customer);
    }
    
    
     // Retrieves all customers
     
    public List<Customer> getAllCustomers() {
        return dbHandler.readDatabase();
    }
    
    
     // Retrieves customer info by name
     
    public List<Customer> getCustomersByName(String name) {
        return dbHandler.findCustomersByName(name);
    }
    
    
     // Retrieves customer by ID
     
    public Customer getCustomerById(int id) {
        return dbHandler.readCustomerById(id);
    }
    
    
     // Deletes  customer from  records of database
     
    public void deleteCustomer(int id) {
        dbHandler.deleteCustomer(id);
    }
    
    
    // Closes database connection
    // we used sql lite for making database
     
    public void closeConnection() {
        dbHandler.closeConnection();
    }
}
