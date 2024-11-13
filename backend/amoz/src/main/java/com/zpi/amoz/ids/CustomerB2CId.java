package com.zpi.amoz.ids;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class CustomerB2CId implements Serializable {

    private UUID customerId;

    public CustomerB2CId() {}

    public CustomerB2CId(UUID customerId) {
        this.customerId = customerId;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerB2CId that = (CustomerB2CId) o;
        return customerId.equals(that.customerId);
    }

    @Override
    public int hashCode() {
        return customerId.hashCode();
    }
}

