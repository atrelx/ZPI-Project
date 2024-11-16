package com.zpi.amoz.models;

import com.zpi.amoz.models.Customer;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class CustomerId implements Serializable {

    @OneToOne
    @JoinColumn(name = "customerId")
    private Customer customer;


    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public CustomerId() {}

    public CustomerId(Customer customer) {
            this.customer = customer;
        }

}


