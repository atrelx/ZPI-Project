package com.zpi.amoz.models;

import com.zpi.amoz.ids.CustomerB2CId;
import com.zpi.amoz.ids.CustomerId;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "CustomerB2C")
public class CustomerB2C {
    @EmbeddedId
    private CustomerId customerId;

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
        return customerId.getCustomer();
    }

    public void setCustomer(Customer customer) {
        this.customerId.setCustomer(customer);
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}

