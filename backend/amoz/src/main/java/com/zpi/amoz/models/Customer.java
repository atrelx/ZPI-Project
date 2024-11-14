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

    @OneToOne
    @JoinColumn(name = "companyId", nullable = false)
    private Company company;

    @OneToOne
    @JoinColumn(name = "defaultDeliveryAddressId")
    private Address defaultDeliveryAddress;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductOrder> orders;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerId", insertable = false, updatable = false)
    private CustomerB2B customerB2B;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerId", insertable = false, updatable = false)
    private CustomerB2C customerB2C;

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

    public Address getDefaultDeliveryAddress() {
        return defaultDeliveryAddress;
    }

    public void setDefaultDeliveryAddress(Address defaultDeliveryAddress) {
        this.defaultDeliveryAddress = defaultDeliveryAddress;
    }

    public List<ProductOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<ProductOrder> orders) {
        this.orders = orders;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}


