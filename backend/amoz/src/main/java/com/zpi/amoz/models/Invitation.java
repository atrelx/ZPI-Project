package com.zpi.amoz.models;

import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "Invitation", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"CompanyID", "EmployeeEmail"})
})
@RequiredArgsConstructor
public class Invitation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "CHAR(36)")
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID invitationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CompanyID", nullable = false)
    private Company company;

    @Column(name = "EmployeeEmail", nullable = false, length = 100)
    private String employeeEmail;

    @Column(name = "Token", nullable = false, columnDefinition = "CHAR(36)", unique = true)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID token = UUID.randomUUID();

    public UUID getInvitationId() {
        return invitationId;
    }

    public void setInvitationId(UUID invitationId) {
        this.invitationId = invitationId;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }

    public UUID getToken() {
        return token;
    }

    public void setToken(UUID token) {
        this.token = token;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
