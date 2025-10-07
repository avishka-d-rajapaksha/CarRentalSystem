package com.mycompany.rentcar.model;

public class Payment {
    private int id;
    private int bookingId;    // Link to Booking
    private double amount;
    private long paymentDate; // Timestamp
    private String method;    // e.g., Cash, Card

    // Constructors
    public Payment() {}

    public Payment(int id, int bookingId, double amount, long paymentDate, String method) {
        this.id = id;
        this.bookingId = bookingId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.method = method;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public long getPaymentDate() { return paymentDate; }
    public void setPaymentDate(long paymentDate) { this.paymentDate = paymentDate; }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }
}