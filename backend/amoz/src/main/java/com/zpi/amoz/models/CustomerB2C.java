package com.zpi.amoz.models;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
public class CustomerB2C {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "CHAR(36)")
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID customerB2CID;

    @OneToOne
    @JoinColumn(name = "customerId", nullable = false, unique = true)
    private Customer customer;

    @OneToOne
    @JoinColumn(name = "personId", nullable = false, unique = true)
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
