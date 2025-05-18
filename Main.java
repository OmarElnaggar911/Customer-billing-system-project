package com.billing;

import com.billing.ui.MainFrame; // calling for the GUI code and class by importing the package
import javax.swing.SwingUtilities; // libraries for gui and interface
import javax.swing.UIManager;


 //Main class for Customer Billing System
// execution of our program starts  from this class
// the main class intiate MainFrame class which is the Jframe
//Jframe then intialize the ui components and the database class as well to make it live and connected
 
public class Main {
    
     // entry point of the application
   
    public static void main(String[] args) {
        // Set the look and feel to system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Error setting look and feel: " + e.getMessage());
        }
        
       
        SwingUtilities.invokeLater(() -> {
            try {
                // Load SQLite JDBC driver
                Class.forName("org.sqlite.JDBC");
                System.out.println("SQLite JDBC driver loaded");
                
                // Create and display the main frame
                new MainFrame();
                System.out.println("Application started successfully");
            } catch (ClassNotFoundException e) {
                System.out.println("SQLite JDBC driver not found: " + e.getMessage());
                System.out.println("Please ensure sqlite-jdbc JAR is in the classpath");
            } catch (Exception e) {
                System.out.println("Error starting application: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
