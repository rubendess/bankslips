package com.bankslips.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class BankSlipDTO extends BaseDTO {
    private String id;
    @JsonFormat(pattern="yyyy-MM-dd", timezone = "GMT-3")
    private Date dueDate;
    private int totalInCents;
    private String customer;
    private String status;
    private double fine;

    public BankSlipDTO() {
    }

    public BankSlipDTO(String id) {
        super(id);
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public int getTotalInCents() {
        return totalInCents;
    }

    public void setTotalInCents(int totalInCents) {
        this.totalInCents = totalInCents;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getFine() {
        return fine;
    }

    public void setFine(double fine) {
        this.fine = fine;
    }
}
