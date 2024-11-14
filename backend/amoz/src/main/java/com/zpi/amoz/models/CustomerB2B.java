package com.zpi.amoz.models;

import com.zpi.amoz.ids.CustomerId;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "CustomerB2B")
public class CustomerB2B {
    @EmbeddedId
    private CustomerId customerId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerId", insertable = false, updatable = false)
    private Customer customer;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "addressId", nullable = false)
    private Address address;

    @Column(nullable = false, length = 255)
    private String nameOnInvoice;

    @Column(nullable = false, unique = true, length = 30)
    private String companyNumber;

    public CustomerId getId() {
        return customerId;
    }

    public void setId(CustomerId customerId) {
        this.customerId = customerId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        this.customerId.setCustomer(customer);
    }

    public String getNameOnInvoice() {
        return nameOnInvoice;
    }

    public void setNameOnInvoice(String nameOnInvoice) {
        this.nameOnInvoice = nameOnInvoice;
    }

    public String getCompanyNumber() {
        return companyNumber;
    }

    public void setCompanyNumber(String companyNumber) {
        this.companyNumber = companyNumber;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}

