package com.zpi.amoz.models;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.UUID;

@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "CHAR(36)")
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID customerId;

    @OneToOne
    @JoinColumn(name = "contactPersonId", nullable = false)
    private ContactPerson contactPerson;

    @Column(nullable = false, length = 255)
    private String nameOnInvoice;

    @ManyToOne
    @JoinColumn(name = "defaultDeliveryAddressId")
    private Address defaultDeliveryAddress;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private CustomerB2B customerB2B;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private CustomerB2C customerB2C;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductOrder> orders;

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public ContactPerson getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(ContactPerson contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getNameOnInvoice() {
        return nameOnInvoice;
    }

    public void setNameOnInvoice(String nameOnInvoice) {
        this.nameOnInvoice = nameOnInvoice;
    }

    public Address getDefaultDeliveryAddress() {
        return defaultDeliveryAddress;
    }

    public void setDefaultDeliveryAddress(Address defaultDeliveryAddress) {
        this.defaultDeliveryAddress = defaultDeliveryAddress;
    }

    public CustomerB2B getCustomerB2B() {
        return customerB2B;
    }

    public void setCustomerB2B(CustomerB2B customerB2B) {
        this.customerB2B = customerB2B;
    }

    public CustomerB2C getCustomerB2C() {
        return customerB2C;
    }

    public void setCustomerB2C(CustomerB2C customerB2C) {
        this.customerB2C = customerB2C;
    }

    public List<ProductOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<ProductOrder> orders) {
        this.orders = orders;
    }
}

