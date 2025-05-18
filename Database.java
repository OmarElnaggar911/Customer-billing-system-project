package com.billing.database;

import com.billing.models.Customer; // importing package of the customer class to use it here
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// we created all of the CRUD operations as doctor explained in the slides
 //Handles database operations for the Customer Billing System
 
public class DatabaseHandler {
    private Connection connection;
    private static final String DB_URL = "jdbc:sqlite:database/customer_billing.db";
    
    
     //Constructor to connect database
     
    public DatabaseHandler() {
        try { //in case of error happens to handle it
            // Create database directory
            java.io.File dbDir = new java.io.File("database");
            if (!dbDir.exists()) {
                dbDir.mkdirs();
            }
            
            // Connect to database
            connection = DriverManager.getConnection(DB_URL);
            System.out.println("Database connection established");
            
            // Create tables of database
            createTables();
        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }
    
    
     //Creates necessary tables if they don't exist
     
    private void createTables() {
        String createCustomerTable = "CREATE TABLE IF NOT EXISTS customers ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT NOT NULL,"
                + "address TEXT,"
                + "mobile_number TEXT,"
                + "paid_amount REAL,"
                + "due_amount REAL,"
                + "payment_date TEXT"
                + ");";
        
        try (Statement statement = connection.createStatement()) {
            statement.execute(createCustomerTable);
            System.out.println("Tables created or already exist");
        } catch (SQLException e) {
            System.out.println("Error creating tables: " + e.getMessage());
        }
    }
    
    
     //Saves a customer record to the database
     
    public void saveDatabase(Customer customer) {
        String sql = "INSERT INTO customers (name, address, mobile_number, paid_amount, due_amount, payment_date) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, customer.getName());
            pstmt.setString(2, customer.getAddress());
            pstmt.setString(3, customer.getMobileNumber());
            pstmt.setDouble(4, customer.getPaidAmount());
            pstmt.setDouble(5, customer.getDueAmount());
            pstmt.setString(6, customer.getPaymentDate());
            
            pstmt.executeUpdate();
            System.out.println("Customer record saved successfully");
        } catch (SQLException e) {
            System.out.println("Error saving customer: " + e.getMessage());
        }
    }
    
    
     // Reads customer records from the database
     
    public List<Customer> readDatabase() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Customer customer = new Customer(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("address"),
                    rs.getString("mobile_number"),
                    rs.getDouble("paid_amount"),
                    rs.getDouble("due_amount"),
                    rs.getString("payment_date")
                );
                customers.add(customer);
            }
        } catch (SQLException e) {
            System.out.println("Error reading customers: " + e.getMessage());
        }
        
        return customers;
    }
    
    
     // Reads a specific customer record by ID
     
    public Customer readCustomerById(int id) {
        String sql = "SELECT * FROM customers WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Customer(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("address"),
                    rs.getString("mobile_number"),
                    rs.getDouble("paid_amount"),
                    rs.getDouble("due_amount"),
                    rs.getString("payment_date")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error reading customer by ID: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Finds customers by name
     * not accurate 
     */
    public List<Customer> findCustomersByName(String nameQuery) {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers WHERE name LIKE ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, "%" + nameQuery + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Customer customer = new Customer(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("address"),
                    rs.getString("mobile_number"),
                    rs.getDouble("paid_amount"),
                    rs.getDouble("due_amount"),
                    rs.getString("payment_date")
                );
                customers.add(customer);
            }
        } catch (SQLException e) {
            System.out.println("Error finding customers by name: " + e.getMessage());
        }
        
        return customers;
    }

    
    // Updates a customer record in the database
     
    public void updateDatabase(Customer customer) {
        String sql = "UPDATE customers SET name = ?, address = ?, mobile_number = ?, "
                + "paid_amount = ?, due_amount = ?, payment_date = ? WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, customer.getName());
            pstmt.setString(2, customer.getAddress());
            pstmt.setString(3, customer.getMobileNumber());
            pstmt.setDouble(4, customer.getPaidAmount());
            pstmt.setDouble(5, customer.getDueAmount());
            pstmt.setString(6, customer.getPaymentDate());
            pstmt.setInt(7, customer.getId());
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Customer record updated successfully");
            } else {
                System.out.println("No customer found with ID: " + customer.getId());
            }
        } catch (SQLException e) {
            System.out.println("Error updating customer: " + e.getMessage());
        }
    }
    
    
     // Deletes a customer record from the database
     
    public void deleteCustomer(int id) {
        String sql = "DELETE FROM customers WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Customer deleted successfully");
            } else {
                System.out.println("No customer found with ID: " + id);
            }
        } catch (SQLException e) {
            System.out.println("Error deleting customer: " + e.getMessage());
        }
    }
    
    
     //Closes the database connection
     
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed");
            }
        } catch (SQLException e) {
            System.out.println("Error closing database connection: " + e.getMessage());
        }
    }
}
