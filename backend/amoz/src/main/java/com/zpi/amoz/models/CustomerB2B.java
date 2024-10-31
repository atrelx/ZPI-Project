package com.zpi.amoz.models;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
public class CustomerB2B {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID customerB2BId;

    @OneToOne
    @JoinColumn(name = "customerId", nullable = false, unique = true)
    private Customer customer;

    @Column(nullable = false, length = 255)
    private String addressOnInvoice;

    @Column(nullable = false, unique = true, length = 30)
    private String companyNumber;

    public UUID getCustomerB2BId() {
        return customerB2BId;
    }

    public void setCustomerB2BId(UUID customerB2BId) {
        this.customerB2BId = customerB2BId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getAddressOnInvoice() {
        return addressOnInvoice;
    }

    public void setAddressOnInvoice(String addressOnInvoice) {
        this.addressOnInvoice = addressOnInvoice;
    }

    public String getCompanyNumber() {
        return companyNumber;
    }

    public void setCompanyNumber(String companyNumber) {
        this.companyNumber = companyNumber;
    }
}

