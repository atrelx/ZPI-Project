package com.zpi.amoz.models;

import jakarta.persistence.*;

@Entity
@Table(name = "CustomerB2C")
public class CustomerB2C {
    @EmbeddedId
    private CustomerId customerId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerId", insertable = false, updatable = false)
    private Customer customer;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "personId", nullable = false, unique = true)
    private Person person;

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

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}

