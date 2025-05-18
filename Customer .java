package com.billing.models;

/**
 * Represents a customer with attributes such as name, address, mobile number,
 * paid amount, due amount and payment date.
 */
public class Customer {
    private int id;
    private String name;
    private String address;
    private String mobileNumber;
    private double paidAmount;
    private double dueAmount;
    private String paymentDate;

    /**
     * Constructor to initialize a customer object with attributes.
     */
    public Customer(int id, String name, String address, String mobileNumber, 
                   double paidAmount, double dueAmount, String paymentDate) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.mobileNumber = mobileNumber;
        this.paidAmount = paidAmount;
        this.dueAmount = dueAmount;
        this.paymentDate = paymentDate;
    }
    
    /**
     * main constructor
     */
    public Customer() {
        this.id = 0;
        this.name = "";
        this.address = "";
        this.mobileNumber = "";
        this.paidAmount = 0.0;
        this.dueAmount = 0.0;
        this.paymentDate = "";
    }

    // Getters and setters
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public double getDueAmount() {
        return dueAmount;
    }

    public void setDueAmount(double dueAmount) {
        this.dueAmount = dueAmount;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    /**
     * Displays information of customer
     */
    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", paidAmount=" + paidAmount +
                ", dueAmount=" + dueAmount +
                ", paymentDate='" + paymentDate + '\'' +
                '}';
    }
}
