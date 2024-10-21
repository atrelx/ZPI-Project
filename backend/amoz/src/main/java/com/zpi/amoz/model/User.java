package com.zpi.amoz.model;



import jakarta.persistence.*;
import jakarta.persistence.Column;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "User")
public class User {

    @Id
    @Column(name = "UserId", columnDefinition = "CHAR(21)")
    private String userId;

    @Column(name = "Name", nullable = false, length = 30)
    private String name;

    @Column(name = "Surname", nullable = false, length = 30)
    private String surname;

    @Column(name = "Email", nullable = false, unique = true, length = 30)
    private String email;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}