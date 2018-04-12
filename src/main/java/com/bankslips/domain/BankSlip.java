package com.bankslips.domain;

import com.bankslips.enums.EnumBankSlipStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
//@JsonIgnoreProperties(ignoreUnknown = true, value = {"hibernateLazyInitializer", "handler"})
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity(name = "bankslip")
public class BankSlip extends BaseEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private UUID id;
    @Temporal(TemporalType.DATE)
    @Column(name = "due_date")
    private Date dueDate;
    @Column(name = "total_in_cents")
    private int totalInCents;
    private String customer;
    private EnumBankSlipStatus status;

    public BankSlip() { }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public EnumBankSlipStatus getStatus() {
        return status;
    }

    public void setStatus(EnumBankSlipStatus status) {
        this.status = status;
    }
}
