package com.mycompany.rentcar.model;

public class Customer {
    private int id;
    private String fullName;
    private String licenseNo;
    private String phone;
    private String email;
    private String address;
    private boolean blacklisted;
    private String notes;
    private long createdAt;

    // Constructors
    public Customer() {}

    public Customer(int id, String fullName, String licenseNo, String phone, String email,
                    String address, boolean blacklisted, String notes, long createdAt) {
        this.id = id;
        this.fullName = fullName;
        this.licenseNo = licenseNo;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.blacklisted = blacklisted;
        this.notes = notes;
        this.createdAt = createdAt;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getLicenseNo() { return licenseNo; }
    public void setLicenseNo(String licenseNo) { this.licenseNo = licenseNo; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public boolean isBlacklisted() { return blacklisted; }
    public void setBlacklisted(boolean blacklisted) { this.blacklisted = blacklisted; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return fullName + " (" + licenseNo + ")";
    }

    public void setName(String text) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public String getName() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}