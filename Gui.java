package com.billing.ui; // the package responsible on the GUI 

import com.billing.controller.CustomerManager;
import com.billing.models.Customer;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
// we found those imports and library on the internet 


 // Main JFrame class for the Customer Billing System
 
public class MainFrame extends JFrame {
    // UI Components
    private JTabbedPane tabbedPane;
    private JPanel mainPanel, customerPanel, listPanel, searchPanel;
    private JTable customerTable;
    private DefaultTableModel tableModel;
    private JScrollPane tableScrollPane;
    private JTextField nameField, addressField, mobileField, paidAmountField, dueAmountField, paymentDateField;
    private JTextField searchField;
    private JButton addButton, clearButton, updateButton, deleteButton, searchButton;
    private JLabel statusLabel;
    
    // Controller
    private CustomerManager customerManager;
    
    // Selected customer for updating
    private Customer selectedCustomer;
    
    /**
     * Constructor initializes the main frame
     */
    public MainFrame() {
        // Initialize controller
        customerManager = new CustomerManager();
        
        // Configure frame
        setTitle("Customer Billing Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Add window listener to handle closing events
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                customerManager.closeConnection();
                System.exit(0);
            }
        });
        
        // Initialize components
        initComponents();
        
        // Display the frame
        setVisible(true);
    }
    
    /**
     * Initializes UI components , the interface 
     */
    private void initComponents() {
        // Create tabbed pane
        tabbedPane = new JTabbedPane();
        
        // Create panels
        mainPanel = new JPanel(new BorderLayout());
        customerPanel = createCustomerPanel();
        listPanel = createListPanel();
        searchPanel = createSearchPanel();
        
        // Add panels to tabbed pane
        tabbedPane.addTab("Customer Management", customerPanel);
        tabbedPane.addTab("Customer List", listPanel);
        tabbedPane.addTab("Search", searchPanel);
        
        // Create status bar
        statusLabel = new JLabel("Ready");
        statusLabel.setBorder(BorderFactory.createEtchedBorder());
        
        // Add components to main panel
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        mainPanel.add(statusLabel, BorderLayout.SOUTH);
        
        // Add main panel to frame
        add(mainPanel);
        
        // Load customer data
        loadCustomerData();
    }
    
    /**
     * Creates the customer management panel
     */
    private JPanel createCustomerPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField(20);
        formPanel.add(nameField);
        
        formPanel.add(new JLabel("Address:"));
        addressField = new JTextField(20);
        formPanel.add(addressField);
        
        formPanel.add(new JLabel("Mobile Number:"));
        mobileField = new JTextField(20);
        formPanel.add(mobileField);
        
        formPanel.add(new JLabel("Paid Amount:"));
        paidAmountField = new JTextField(20);
        formPanel.add(paidAmountField);
        
        formPanel.add(new JLabel("Due Amount:"));
        dueAmountField = new JTextField(20);
        formPanel.add(dueAmountField);
        
        formPanel.add(new JLabel("Payment Date (yyyy-MM-dd):"));
        paymentDateField = new JTextField(20);
        paymentDateField.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        formPanel.add(paymentDateField);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        addButton = new JButton("Add Customer");
        addButton.addActionListener(e -> addCustomer());
        buttonPanel.add(addButton);
        
        updateButton = new JButton("Update Customer");
        updateButton.addActionListener(e -> updateCustomer());
        updateButton.setEnabled(false);
        buttonPanel.add(updateButton);
        
        clearButton = new JButton("Clear Fields");
        clearButton.addActionListener(e -> clearFields());
        buttonPanel.add(clearButton);
        
        // Add components to panel
        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Creates the customer list panel
     */
    private JPanel createListPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Create table
        String[] columns = {"ID", "Name", "Address", "Mobile", "Paid Amount", "Due Amount", "Payment Date"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        customerTable = new JTable(tableModel);
        tableScrollPane = new JScrollPane(customerTable);
        
        // Add selection listener to table
        customerTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && customerTable.getSelectedRow() != -1) {
                int selectedRow = customerTable.getSelectedRow();
                int id = Integer.parseInt(customerTable.getValueAt(selectedRow, 0).toString());
                selectedCustomer = customerManager.getCustomerById(id);
                if (selectedCustomer != null) {
                    populateFields(selectedCustomer);
                    updateButton.setEnabled(true);
                    tabbedPane.setSelectedIndex(0); // Switch to customer management tab
                }
            }
        });
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton refreshButton = new JButton("Refresh List");
        refreshButton.addActionListener(e -> loadCustomerData());
        buttonPanel.add(refreshButton);
        
        deleteButton = new JButton("Delete Selected");
        deleteButton.addActionListener(e -> deleteCustomer());
        buttonPanel.add(deleteButton);
        
        // Add components to panel
        panel.add(tableScrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Creates the search panel
     */
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Search form
        JPanel searchFormPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        searchFormPanel.add(new JLabel("Customer Name:"));
        searchField = new JTextField(20);
        searchFormPanel.add(searchField);
        
        searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchCustomers());
        searchFormPanel.add(searchButton);
        
        // Search results (reuse table model from list panel)
        JScrollPane searchResultsPane = new JScrollPane(new JTable(tableModel));
        
        // Add components to panel
        panel.add(searchFormPanel, BorderLayout.NORTH);
        panel.add(searchResultsPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Adds a new customer
     */
    private void addCustomer() {
        try {
            String name = nameField.getText().trim();
            String address = addressField.getText().trim();
            String mobile = mobileField.getText().trim();
            double paidAmount = Double.parseDouble(paidAmountField.getText().trim());
            double dueAmount = Double.parseDouble(dueAmountField.getText().trim());
            String paymentDate = paymentDateField.getText().trim();
            
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name cannot be empty", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Customer customer = new Customer();
            customer.setName(name);
            customer.setAddress(address);
            customer.setMobileNumber(mobile);
            customer.setPaidAmount(paidAmount);
            customer.setDueAmount(dueAmount);
            customer.setPaymentDate(paymentDate);
            
            customerManager.addCustomer(customer);
            statusLabel.setText("Customer added successfully");
            
            clearFields();
            loadCustomerData();
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for amounts", 
                    "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Updates an existing customer
     */
    private void updateCustomer() {
        if (selectedCustomer == null) {
            JOptionPane.showMessageDialog(this, "No customer selected", 
                    "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            String name = nameField.getText().trim();
            String address = addressField.getText().trim();
            String mobile = mobileField.getText().trim();
            double paidAmount = Double.parseDouble(paidAmountField.getText().trim());
            double dueAmount = Double.parseDouble(dueAmountField.getText().trim());
            String paymentDate = paymentDateField.getText().trim();
            
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name cannot be empty", 
                        "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            selectedCustomer.setName(name);
            selectedCustomer.setAddress(address);
            selectedCustomer.setMobileNumber(mobile);
            selectedCustomer.setPaidAmount(paidAmount);
            selectedCustomer.setDueAmount(dueAmount);
            selectedCustomer.setPaymentDate(paymentDate);
            
            customerManager.editCustomer(selectedCustomer);
            statusLabel.setText("Customer updated successfully");
            
            clearFields();
            loadCustomerData();
            updateButton.setEnabled(false);
            selectedCustomer = null;
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for amounts", 
                    "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Deletes a customer
     */
    private void deleteCustomer() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "No customer selected", 
                    "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int id = Integer.parseInt(customerTable.getValueAt(selectedRow, 0).toString());
        int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to delete this customer?", 
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            customerManager.deleteCustomer(id);
            statusLabel.setText("Customer deleted successfully");
            loadCustomerData();
            clearFields();
            selectedCustomer = null;
            updateButton.setEnabled(false);
        }
    }
    
    /**
     * Searches for customers by name
     */
    private void searchCustomers() {
        String searchName = searchField.getText().trim();
        if (searchName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a name to search", 
                    "Search Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        List<Customer> results = customerManager.getCustomersByName(searchName);
        populateTable(results);
        statusLabel.setText("Found " + results.size() + " matching customers");
    }
    
    /**
     * Clears all form fields
     */
    private void clearFields() {
        nameField.setText("");
        addressField.setText("");
        mobileField.setText("");
        paidAmountField.setText("");
        dueAmountField.setText("");
        paymentDateField.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        
        selectedCustomer = null;
        updateButton.setEnabled(false);
    }
    
    /**
     * Populates form fields with customer data
     */
    private void populateFields(Customer customer) {
        nameField.setText(customer.getName());
        addressField.setText(customer.getAddress());
        mobileField.setText(customer.getMobileNumber());
        paidAmountField.setText(String.valueOf(customer.getPaidAmount()));
        dueAmountField.setText(String.valueOf(customer.getDueAmount()));
        paymentDateField.setText(customer.getPaymentDate());
    }
    
    /**
     * Loads customer data into the table
     */
    private void loadCustomerData() {
        List<Customer> customers = customerManager.getAllCustomers();
        populateTable(customers);
    }
    
    /**
     * Populates the table with customer data
     */
    private void populateTable(List<Customer> customers) {
        // Clear existing data
        tableModel.setRowCount(0);
        
        // Add customer data
        for (Customer customer : customers) {
            Object[] row = {
                customer.getId(),
                customer.getName(),
                customer.getAddress(),
                customer.getMobileNumber(),
                customer.getPaidAmount(),
                customer.getDueAmount(),
                customer.getPaymentDate()
            };
            tableModel.addRow(row);
        }
    }
}
