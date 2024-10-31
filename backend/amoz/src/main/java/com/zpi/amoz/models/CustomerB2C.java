package com.zpi.amoz.models;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
public class CustomerB2C {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID customerB2CID;

    @OneToOne
    @JoinColumn(name = "customerID", nullable = false, unique = true)
    private Customer customer;

    @OneToOne
    @JoinColumn(name = "personID", nullable = false, unique = true)
    private Person person;

    public UUID getCustomerB2CID() {
        return customerB2CID;
    }

    public void setCustomerB2CID(UUID customerB2CID) {
        this.customerB2CID = customerB2CID;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
