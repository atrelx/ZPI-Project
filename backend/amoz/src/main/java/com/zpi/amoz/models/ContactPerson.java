package com.zpi.amoz.models;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.UUID;

@Entity
public class ContactPerson {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "CHAR(36)")
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID contactPersonId;

    @Column(nullable = false, unique = true, length = 20)
    private String contactNumber;

    @Column(unique = true, length = 100)
    private String emailAddress;

    @OneToOne(mappedBy="contactPerson", cascade = CascadeType.ALL)
    private Customer customer;

    @OneToOne(mappedBy="contactPerson", cascade = CascadeType.ALL)
    private Employee employee;

    public UUID getContactPersonId() {
        return contactPersonId;
    }

    public void setContactPersonId(UUID contactPersonId) {
        this.contactPersonId = contactPersonId;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}

