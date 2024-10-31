package com.zpi.amoz.models;

import jakarta.persistence.*;
import jakarta.persistence.Column;

import java.util.List;

@Entity
@Table(name = "User")
public class User {
    @Id
    @Column(name = "UserId", columnDefinition = "CHAR(21)")
    private String userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SystemRole systemRole = SystemRole.USER;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Employee employee;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public SystemRole getSystemRole() {
        return systemRole;
    }

    public void setSystemRole(SystemRole systemRole) {
        this.systemRole = systemRole;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public enum SystemRole {
        USER, ADMIN
    }
}