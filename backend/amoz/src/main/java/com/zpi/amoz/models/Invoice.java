package com.zpi.amoz.models;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID invoiceId;

    @Column(nullable = false, unique = true)
    private int invoiceNumber;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amountOnInvoice;

    @Column(nullable = false)
    private LocalDate issueDate = LocalDate.now();

    @OneToOne(mappedBy = "invoice", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private ProductOrder productOrder;

    public UUID getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(UUID invoiceId) {
        this.invoiceId = invoiceId;
    }

    public int getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(int invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public BigDecimal getAmountOnInvoice() {
        return amountOnInvoice;
    }

    public void setAmountOnInvoice(BigDecimal amountOnInvoice) {
        this.amountOnInvoice = amountOnInvoice;
    }

    public ProductOrder getProductOrder() {
        return productOrder;
    }

    public void setProductOrder(ProductOrder productOrder) {
        this.productOrder = productOrder;
    }
    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }
}
