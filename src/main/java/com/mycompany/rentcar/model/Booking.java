package com.mycompany.rentcar.model;

public class Booking {
    private int id;
    private int vehicleId;
    private int customerId;
    private long pickupTs;
    private long returnTs;
    private String status;

    private int rateCents;
    private int depositCents;
    private int taxRateBp;       // tax in basis points (e.g. 1500 = 15.00%)
    private int discountCents;
    private Integer pickupOdometer;
    private Integer returnOdometer;
    private int lateFeeCents;

    private String notes;
    private int createdBy;
    private long createdAt;
    private Long updatedAt; // nullable

    public Booking() {}

    public Booking(int id, int vehicleId, int customerId, long pickupTs, long returnTs,
                   String status, int rateCents, int depositCents, int taxRateBp,
                   int discountCents, Integer pickupOdometer, Integer returnOdometer,
                   int lateFeeCents, String notes, int createdBy, long createdAt, Long updatedAt) {
        this.id = id;
        this.vehicleId = vehicleId;
        this.customerId = customerId;
        this.pickupTs = pickupTs;
        this.returnTs = returnTs;
        this.status = status;
        this.rateCents = rateCents;
        this.depositCents = depositCents;
        this.taxRateBp = taxRateBp;
        this.discountCents = discountCents;
        this.pickupOdometer = pickupOdometer;
        this.returnOdometer = returnOdometer;
        this.lateFeeCents = lateFeeCents;
        this.notes = notes;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // ---- Getters & Setters ----
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getVehicleId() { return vehicleId; }
    public void setVehicleId(int vehicleId) { this.vehicleId = vehicleId; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public long getPickupTs() { return pickupTs; }
    public void setPickupTs(long pickupTs) { this.pickupTs = pickupTs; }

    public long getReturnTs() { return returnTs; }
    public void setReturnTs(long returnTs) { this.returnTs = returnTs; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getRateCents() { return rateCents; }
    public void setRateCents(int rateCents) { this.rateCents = rateCents; }

    public int getDepositCents() { return depositCents; }
    public void setDepositCents(int depositCents) { this.depositCents = depositCents; }

    public int getTaxRateBp() { return taxRateBp; }
    public void setTaxRateBp(int taxRateBp) { this.taxRateBp = taxRateBp; }

    public int getDiscountCents() { return discountCents; }
    public void setDiscountCents(int discountCents) { this.discountCents = discountCents; }

    public Integer getPickupOdometer() { return pickupOdometer; }
    public void setPickupOdometer(Integer pickupOdometer) { this.pickupOdometer = pickupOdometer; }

    public Integer getReturnOdometer() { return returnOdometer; }
    public void setReturnOdometer(Integer returnOdometer) { this.returnOdometer = returnOdometer; }

    public int getLateFeeCents() { return lateFeeCents; }
    public void setLateFeeCents(int lateFeeCents) { this.lateFeeCents = lateFeeCents; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public int getCreatedBy() { return createdBy; }
    public void setCreatedBy(int createdBy) { this.createdBy = createdBy; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public Long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Long updatedAt) { this.updatedAt = updatedAt; }

    public Object getCarId() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public String getCarModel() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public String getBookingDate() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public String getReturnDate() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void setActualReturnDate(String toString) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}