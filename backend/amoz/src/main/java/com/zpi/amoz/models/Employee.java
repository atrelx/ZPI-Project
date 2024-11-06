package com.zpi.amoz.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.zpi.amoz.enums.RoleInCompany;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.type.SqlTypes;

import java.util.UUID;
import java.time.LocalDate;

@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "CHAR(36)")
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID employeeId;

    @OneToOne
    @JoinColumn(name = "userId", nullable = false)
    @JsonBackReference
    private User user;

    @OneToOne
    @JoinColumn(name = "contactPersonId", nullable = false)
    @JsonBackReference
    private ContactPerson contactPerson;


    @ManyToOne
    @JoinColumn(name = "companyId")
    @JsonBackReference
    private Company company;

    @Enumerated(EnumType.STRING)
    private RoleInCompany roleInCompany;

    private LocalDate employmentDate;

    @OneToOne
    @JoinColumn(name = "personId", nullable = false, unique = true)
    @JsonBackReference
    private Person person;

    public UUID getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(UUID employeeId) {
        this.employeeId = employeeId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ContactPerson getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(ContactPerson contactPerson) {
        this.contactPerson = contactPerson;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public RoleInCompany getRoleInCompany() {
        return roleInCompany;
    }

    public void setRoleInCompany(RoleInCompany roleInCompany) {
        this.roleInCompany = roleInCompany;
    }

    public LocalDate getEmploymentDate() {
        return employmentDate;
    }

    public void setEmploymentDate(LocalDate employmentDate) {
        this.employmentDate = employmentDate;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
